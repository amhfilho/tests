package com.amhfilho.boottests.book;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Objects;

public class Book {
    private String isbn;
    private String title;
    private Integer publishYear;
    private BigDecimal price;

    public Book(String isbn, String title, Integer publishYear, BigDecimal price) {
        this.isbn = isbn;
        this.title = title;
        this.publishYear = publishYear;
        this.price = price;
    }

    public Book(String isbn) {
        this.isbn = isbn;
    }

    public Book() {
    }

    @Override
    @JsonIgnore
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(getIsbn(), book.getIsbn());
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", publishYear=" + publishYear +
                ", price=" + price +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIsbn());
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public BigDecimal getPrice() {
        return price;
    }

   public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
