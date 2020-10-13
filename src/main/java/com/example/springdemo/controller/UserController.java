package com.example.springdemo.controller;

import com.example.springdemo.model.User;
import com.example.springdemo.service.UserService;
import lombok.RequiredArgsConstructor;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Value("${file.upload.dir}")
    private String uploadDir;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user, @RequestParam("image") MultipartFile file) throws IOException {
        Optional<User> byUsername = userService.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            return "redirect:/?msg=User already exists";
        }
        String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File image = new File(uploadDir, name);
        file.transferTo(image);
        user.setProfilePic(name);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/?msg=User was added";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteById(id);
        String msg = "User was removed";
        return "redirect:/?msg=" + msg;
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("name") String imageName) throws IOException {
        InputStream in = new FileInputStream(uploadDir + File.separator + imageName);
        return IOUtils.toByteArray(in);
    }
}
