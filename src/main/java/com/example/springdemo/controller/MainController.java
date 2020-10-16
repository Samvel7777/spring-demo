package com.example.springdemo.controller;

import com.example.springdemo.model.Book;
import com.example.springdemo.model.Role;
import com.example.springdemo.model.User;
import com.example.springdemo.security.CurrentUser;
import com.example.springdemo.service.BookService;
import com.example.springdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final BookService bookService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(ModelMap modelMap, @RequestParam(name = "msg", required = false) String msg,
                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "size", defaultValue = "20") int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Book> books = bookService.findAll(pageRequest);

        int totalPages = books.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }

        modelMap.addAttribute("books", books);
        modelMap.addAttribute("users", userService.findAll());
        modelMap.addAttribute("msg", msg);
        return "home";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/about")
    public String aboutUs() {
        return "about";
    }

    @GetMapping("/successLogin")
    public String successLogin(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "redirect:/";
        }
        User user = currentUser.getUser();
        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin";
        } else {
            return "redirect:/user";
        }
    }

}
