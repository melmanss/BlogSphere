package com.example.demo.controller;
import com.example.demo.dto.UserCreationRequest;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUserWithPosts(@RequestBody UserCreationRequest request) {
        User user = userService.createUserWithPosts(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        User user = userService.getUserByName(name);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-email-domain")
    public ResponseEntity<List<User>> getUsersByEmailDomain(@RequestParam String domain) {
        List<User> users = userService.getUsersByEmailDomain(domain);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = userService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
}