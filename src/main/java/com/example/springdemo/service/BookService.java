package com.example.springdemo.service;

import com.example.springdemo.model.Book;
import com.example.springdemo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void save(Book user) {
        bookRepository.save(user);
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    public Book getOne(int id) {
        return bookRepository.getOne(id);
    }
}
