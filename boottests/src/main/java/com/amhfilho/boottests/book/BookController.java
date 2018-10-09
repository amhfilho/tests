package com.amhfilho.boottests.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class BookController {

    @Autowired
    private BookRepository repository;

    @RequestMapping("/")
    public String findAll(Map<String, Object> model) {
        model.put("books", this.repository.findAll());
        return "welcome";
    }
}
