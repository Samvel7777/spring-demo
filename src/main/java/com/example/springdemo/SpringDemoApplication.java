package com.example.springdemo;

import com.example.springdemo.model.Book;
import com.example.springdemo.model.User;
import com.example.springdemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringDemoApplication implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 200; i++) {
            bookService.save(Book.builder()
                    .title("title" + i)
                    .description("desc" + i)
                    .user(User.builder()
                            .id(3)
                            .build())
                    .build());
        }
    }
}
