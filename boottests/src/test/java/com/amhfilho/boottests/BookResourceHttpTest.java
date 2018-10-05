package com.amhfilho.boottests;

import com.amhfilho.boottests.book.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class BookResourceHttpTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnListOfAllBooks(){
        ResponseEntity<List<Book>> response = restTemplate.exchange(
                booksUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>(){});
        List<Book> books = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(books).contains(new Book("0134757599"));
        assertThat(books).contains(new Book("9780321146533"));
        assertThat(books).doesNotContain(new Book("invalid"));
        assertThat(books.size()).isEqualTo(2);
    }

    private String booksUrl() {
        return "http://localhost:"+port+"/books/";
    }

    @Test
    public void shouldCreateBook(){
        Book newBook = new Book("12345", "My Book", 2018, new BigDecimal("20.0"));
        HttpEntity<Book> request = new HttpEntity<>(newBook);
        ResponseEntity<Book> response = restTemplate
                .exchange(booksUrl(), HttpMethod.POST, request, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Book created = response.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getIsbn()).isEqualTo("12345");
    }
}
