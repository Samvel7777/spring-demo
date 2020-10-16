package com.example.springdemo.controller;

import com.example.springdemo.model.Book;
import com.example.springdemo.repository.BookRepository;
import com.example.springdemo.service.BookService;
import com.example.springdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    private final BookRepository bookRepository;

    @GetMapping("/book")
    public String bookPage(ModelMap modelMap,
                           @RequestParam(name = "msg", required = false) String msg,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "size", defaultValue = "5") int size,
                           @RequestParam(value = "orderBy", defaultValue = "title") String orderBy,
                           @RequestParam(value = "arder", defaultValue = "ASC") String order) {

        Sort sort = order.equals("ASC") ? Sort.by(Sort.Order.asc(orderBy)) : Sort.by(Sort.Order.desc(orderBy));

        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<Book> books = bookRepository.findAll(pageRequest);
        int totalPages = books.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("users", userService.findAll());
        modelMap.addAttribute("books", books);
        modelMap.addAttribute("msg", msg);
        return "book";
    }

    @PostMapping("/book/save")
    public String addUser(@ModelAttribute Book book) {
        String msg = book.getId() > 0 ? "Book was added" : "Book was updated";
        bookService.save(book);
        return "redirect:/?msg=" + msg;
    }

    @GetMapping("/book/editPage")
    public String editBook(@RequestParam("id") int id, ModelMap modelMap) {
        modelMap.addAttribute("book", bookService.getOne(id));
        modelMap.addAttribute("users", userService.findAll());
        return "editBook";
    }

    @GetMapping("/book/delete")
    public String deleteBook(@RequestParam("id") int id) {
        bookService.deleteById(id);
        String msg = "Book was removed";
        return "redirect:/?msg=" + msg;
    }
}
