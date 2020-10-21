package com.example.springdemo.service;

import com.example.springdemo.model.User;
import com.example.springdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (EntityNotFoundException e) {
            log.error("User with {} dose ot exists", username);
            return null;
        }
    }
}
