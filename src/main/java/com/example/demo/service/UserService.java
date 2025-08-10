
package com.example.demo.service;

import com.example.demo.dto.UserCreationRequest;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User createUserWithPosts(UserCreationRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);

        request.getPosts().forEach(postDto -> {
            Post post = new Post();
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setUser(savedUser);
            postRepository.save(post);
        });

        if ("error".equalsIgnoreCase(savedUser.getName())) {
            throw new RuntimeException("Помилка! Спрацював Rollback!");
        }

        return savedUser;
    }

    @Transactional(readOnly = true)
    public User getUserByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Користувача з іменем " + name + " не знайдено"));
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByEmailDomain(String domain) {
        if (!domain.startsWith("@")) {
            domain = "@" + domain;
        }
        return userRepository.findByEmailEndingWith(domain);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}