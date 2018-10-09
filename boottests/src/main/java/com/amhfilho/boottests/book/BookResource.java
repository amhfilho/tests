package com.amhfilho.boottests.book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("books")
public class BookResource {
    private static final Logger log = LoggerFactory.getLogger(BookResource.class);

    @Autowired
    private BookRepository repository;

    @GetMapping("")
    public List<Book> getAllBooks(){
        return repository.findAll();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> findById(@PathVariable String isbn){
        Optional<Book> optional = repository.findById(isbn);
        if(optional.isPresent()){
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    public Book create(@RequestBody Book book){
        //save in the database
        log.info("Creating book: {}", book.toString());
        repository.save(book);
        return book;
    }

    @DeleteMapping("/{isbn}")
    @Transactional
    public Book delete(@PathVariable String isbn){
        log.info("Deleting book {}", isbn);
        if(repository.existsById(isbn)) {
            repository.deleteById(isbn);
        }
        return new Book(isbn);
    }

    @PutMapping("")
    @Transactional
    public ResponseEntity<?> update(@RequestBody Book book){
        log.info("Updating book {}", book.toString());
        if(repository.existsById(book.getIsbn())){
            book = repository.save(book);
            return new ResponseEntity<Book>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
