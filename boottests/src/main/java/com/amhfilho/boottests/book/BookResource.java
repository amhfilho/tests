package com.amhfilho.boottests.book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookResource {

    private static final Logger log = LoggerFactory.getLogger(BookResource.class);

    @GetMapping("")
    public List<Book> getAllBooks(){
        return Arrays.asList(
                new Book("0134757599","Refactoring: Improving the Design of Existing Code",2018,new BigDecimal("140.0")),
                new Book("9780321146533", "Test Driven Development: By Example", 2002, new BigDecimal("131.07"))
                );
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book createBook(@RequestBody Book book){
        //save in the database
        log.info(book.toString());
        return book;
    }
}
