package com.example.springdemo.controller;

//import com.example.springdemo.model.Role;
//import com.example.springdemo.model.User;
//import com.example.springdemo.security.CurrentUser;
import com.example.springdemo.service.BookService;
import com.example.springdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final BookService bookService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(@AuthenticationPrincipal Principal principal, ModelMap modelMap, @RequestParam(name = "msg", required = false) String msg) {
        String username = null;
        if (principal != null) {
            username = principal.getName();
        }
        modelMap.addAttribute("users", userService.findAll());
        modelMap.addAttribute("books", bookService.findAll());
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("username", username);
        return "home";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

//    @GetMapping("/successLogin")
//    public String successLogin(@AuthenticationPrincipal CurrentUser currentUser) {
//        if (currentUser == null) {
//            return "redirect:/";
//        }
//        User user = currentUser.getUser();
//        if (user.getRole() == Role.ADMIN) {
//            return "redirect:/admin";
//        }else{
//            return "redirect:/user";
//        }
//    }

    @GetMapping("/about")
    public String aboutUs() {
        return "about";
    }

}
