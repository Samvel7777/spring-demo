package com.example.springdemo.controller;

import com.example.springdemo.dto.UserRequestDto;
import com.example.springdemo.model.User;
import com.example.springdemo.service.EmailService;
import com.example.springdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
//import static com.example.springdemo.util.TextUtil.VALID_EMAIL_ADDRESS_REGEX;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Value("${file.upload.dir}")
    private String uploadDir;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @GetMapping("/user")
    public String userPage(ModelMap modelMap, @RequestParam(name = "msg", required = false) String msg) {
        modelMap.addAttribute("users", userService.findAll());
        modelMap.addAttribute("msg", msg);
        return "user";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute @Valid UserRequestDto userRequest, BindingResult bindingResult, ModelMap modelMap, @RequestParam("image") MultipartFile file, Locale locale) throws IOException, MessagingException {
//        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(userRequest.getUsername()).find()) {
//            return "redirect:/user?msg=Email dose not valid!!!";
//        }
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("users", userService.findAll());
            return "user";
        }
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            return "redirect:/user?msg=Password and ConfirmPassword dase note match!!!";
        }
        Optional<User> byUsername = userService.findByUsername(userRequest.getUsername());
        if (byUsername.isPresent()) {
            return "redirect:/user?msg=User already exists!!!";
        }
        String profilePic = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File image = new File(uploadDir, profilePic);
        file.transferTo(image);
        User user = User.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .active(false)
                .token(UUID.randomUUID().toString())
                .profilePic(profilePic)
                .role(userRequest.getRole())
                .build();
        userService.save(user);
        String link = "http://localhost:8081/user/activate?email=" + user.getUsername() + "&token=" + user.getToken();
        emailService.sendHtmlEmail(user.getUsername(),
                "Wlecome", user, link, "email/UserWelcomeMail.html", locale);
        return "redirect:/user?msg=User was added";
    }

    @GetMapping("/user/activate")
    public String activate(@RequestParam("email") String username, @RequestParam("token") String token) {
        Optional<User> byUsername = userService.findByUsername(username);
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            if (user.getToken().equals(token)) {
                user.setActive(true);
                user.setToken("");
                userService.save(user);
                return "redirect:/user?msg=User was activate. Please Login";
            }
        }
        return "redirect:/user?msg=Something went wrong. Please try again!!!";
    }

    @GetMapping("/user/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteById(id);
        String msg = "User was removed";
        return "redirect:/user?msg=" + msg;
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("name") String imageName) throws IOException {
        InputStream in = new FileInputStream(uploadDir + File.separator + imageName);
        return IOUtils.toByteArray(in);
    }
}
