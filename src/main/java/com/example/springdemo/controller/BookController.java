package com.example.springdemo.controller;

import com.example.springdemo.model.Book;
import com.example.springdemo.service.BookService;
import com.example.springdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;


    @PostMapping("/saveBook")
    public String addUser(@ModelAttribute Book book) {
        String msg = book.getId() > 0 ? "Book was added" : "Book was updated";
        bookService.save(book);
        return "redirect:/?msg=" + msg;
    }

    @GetMapping("/editBook")
    public String editBook(@RequestParam("id") int id, ModelMap modelMap) {
        modelMap.addAttribute("book", bookService.getOne(id));
        modelMap.addAttribute("users", userService.findAll());
        return "editBook";
    }

    @GetMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") int id) {
        bookService.deleteById(id);
        String msg = "Book was removed";
        return "redirect:/?msg=" + msg;
    }
}
