package raf.rs.NwpNikolaDomaci3.controllers;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import raf.rs.NwpNikolaDomaci3.model.User;
import raf.rs.NwpNikolaDomaci3.services.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users/")
public class UserController {


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User saveUser(@RequestBody User user) {
        Optional<User> u = userService.findById((user.getId()));
        if (u.isPresent()) {
            System.out.println("User already exists");
            return null;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
        if (userLogedIn.isPresent()) {
            User U = userLogedIn.get();
            if (U.getPermission().getCan_create_user() == 1) {
                user.setPass(this.passwordEncoder.encode(user.getPass()));
                return this.userService.save(user);
            } else {
                System.out.println(" No permission to create user! ");
            }
        }
        return null;
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsersById(@RequestParam("userId") Long id) {
        Optional<User> optionalStudent = userService.findById(id);
        if (optionalStudent.isPresent()) {
            return ResponseEntity.ok(optionalStudent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/myself", produces = MediaType.APPLICATION_JSON_VALUE)
    public User myself() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(username);
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userLogedIn = userService.findById(id);
        if (userLogedIn.isPresent()) {
            User u = userLogedIn.get();
            if(u.getPermission().getCan_delete_user() == 1){
                Optional<User> user = userService.findById(id);
                if(user.isPresent()){
                    userService.deleteById(id);
                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user) {
        Optional<User> u = userService.findById(user.getId());
        if(!u.isPresent()){
            System.out.println(" User with this id doesn't exists! ");
            return null;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user2 = Optional.ofNullable(userService.findByEmail(username));
        if(u.isPresent()){
            User User2 = user2.get();
            if(User2.getPermission().getCan_update_user() == 1){
                user.setPass(this.passwordEncoder.encode(user.getPass()));
                return userService.save(user);
            }
            else {
                System.out.println(" No permission to update user! ");
            }
        }
        else{
            System.out.println(" No user found ");
            return null;
        }
        return null;

    }

}

