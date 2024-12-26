package com.master.faez.telbot.services;

import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    public Book save(Book book) {
        return bookRepository.save(book);
    }
    public void DeleteById(Integer id) {
        bookRepository.deleteById(id);
    }
    public List<String> findAllBooksNames() {
        List<Book> books = bookRepository.findAll();
        List<String> bookNames = new ArrayList<>();
        books.forEach(book -> {
            bookNames.add(book.getName());
        });
        return bookNames;
    }
}
