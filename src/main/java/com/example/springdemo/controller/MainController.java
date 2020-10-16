package com.example.springdemo.controller;

import com.example.springdemo.service.BookService;
import com.example.springdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final BookService bookService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
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

//    @GetMapping("/successLogin")
//    public String successLogin(@AuthenticationPrincipal CurrentUser currentUser) {
//        if (currentUser == null) {
//            return "redirect:/";
//        }
//        User user = currentUser.getUser();
//        if (user.getRole() == Role.ADMIN) {
//            return "redirect:/admin";
//        } else {
//            return "redirect:/user";
//        }
//    }

}
