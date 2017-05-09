package me.josephcosentino.controller;

import me.josephcosentino.model.User;
import me.josephcosentino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Joseph Cosentino.
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers(@PageableDefault(size = 50, sort = "id") Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/users/{user_id}")
    public User getUsers(@PathVariable("user_id") Long user_id) {
        return userService.findById(user_id);
    }

    @DeleteMapping("/users/cache")
    public void purgeUsers() {
        userService.clearCache();
    }

    @DeleteMapping("/users/{user_id}/cache")
    public void purgeUser(@PathVariable("user_id") Long user_id) {
        userService.clearCacheById(user_id);
    }

}
