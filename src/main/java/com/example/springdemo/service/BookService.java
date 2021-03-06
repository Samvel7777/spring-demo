package com.example.springdemo.service;

import com.example.springdemo.model.Book;
import com.example.springdemo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> findAll(PageRequest pageRequest, Sort sort) {
        return bookRepository.findAll(pageRequest);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
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
