package com.amhfilho.boottests;

import com.amhfilho.boottests.book.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class BookResourceHttpTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup(){
        restTemplate.exchange(
                booksUrl(""),
                HttpMethod.POST,
                new HttpEntity<>(new Book("0134757599", "A good book", "A good author")),
                Book.class);

    }

    @After
    public void cleanup(){
        restTemplate.exchange(
                booksUrl("0134757599"),
                HttpMethod.DELETE,
                null,
                Book.class);
    }

    @Test
    public void shouldReturnListOfAllBooks(){
        ResponseEntity<List<Book>> response = restTemplate.exchange(
                booksUrl(""),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>(){});
        List<Book> books = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(books).contains(new Book("0134757599"));
        assertThat(books).doesNotContain(new Book("invalid"));
    }

    private String booksUrl(String isbn) {
        return "http://localhost:"+port+"/books/"+isbn;
    }

    @Test
    public void shouldFindABook(){
        ResponseEntity<Book> response = restTemplate.exchange(
                booksUrl("0134757599"),
                HttpMethod.GET,
                null,
                Book.class
        );
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIsbn()).isEqualTo("0134757599");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void shouldNotFindABook(){
        ResponseEntity<Book> response = restTemplate.exchange(
                booksUrl("invalid"),
                HttpMethod.GET,
                null,
                Book.class
        );
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCreateBook(){
        Book newBook = new Book("12345", "My Book", "My Author");
        HttpEntity<Book> request = new HttpEntity<>(newBook);
        ResponseEntity<Book> response = restTemplate
                .exchange(booksUrl(""), HttpMethod.POST, request, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Book created = response.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getIsbn()).isEqualTo("12345");

        restTemplate.exchange(
                booksUrl("12345"),
                HttpMethod.DELETE,
                null,
                Book.class
        );
    }

    @Test
    public void shouldDeleteBook(){
        Book book = new Book("1","Temporary Book", "Temporary Author");
        restTemplate.exchange(booksUrl(""), HttpMethod.POST, new HttpEntity<Book>(book),Book.class);


        ResponseEntity<Book> deleted = restTemplate.exchange(
                booksUrl("1"),
                HttpMethod.DELETE,
                null,
                Book.class
        );
        assertThat(deleted.getStatusCodeValue()).isEqualTo(200);

        ResponseEntity<List<Book>> books =
                restTemplate.exchange(
                        booksUrl(""),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Book>>(){});
        assertThat(books.getStatusCodeValue()).isEqualTo(200);
        assertThat(books.getBody().size()).isEqualTo(1);
    }

    @Test
    public void shouldUpdateABook(){
        Book book = new Book("0134757599","Updated Book title", "Updated Author");

        ResponseEntity<Book> updated = restTemplate.exchange(
                booksUrl(""),
                HttpMethod.PUT,
                new HttpEntity<>(book),
                Book.class
        );

        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated.getBody().getTitle()).isEqualTo("Updated Book title");
        assertThat(updated.getBody().getAuthor()).isEqualTo("Updated Author");
    }

    @Test
    public void shouldNotFindABookToUpdate(){
        Book invalid = new Book("invalid");
        ResponseEntity<Book> updated = restTemplate.exchange(
                booksUrl(""),
                HttpMethod.PUT,
                new HttpEntity<>(invalid),
                Book.class
        );

        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(updated.getBody()).isNull();

    }
}
