package raf.rs.NwpNikolaDomaci3.controllers;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import raf.rs.NwpNikolaDomaci3.model.User;
import raf.rs.NwpNikolaDomaci3.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users/")
public class UserController {


    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userService.findByEmail(email);
        if (currUser.getCanCreate() == false) {
            return ResponseEntity.status(403).build();
        }
        return new ResponseEntity<>(this.userService.create(user), HttpStatus.OK);
    }

    @GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public User email() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(email);
    }

    @GetMapping(value = "/allUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(username);
        if (user.getCanRead() == false) {
            System.out.println(" No permission to read all users! ");
        } else {
            System.out.println(" No user found ");
            return null;
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<Page<User>> all(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email);

        if (user.getCanRead() == false) {
            return ResponseEntity.status(403).build();
        }

        return new ResponseEntity<>(this.userService.paginate(page, size), HttpStatus.OK);
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userLogedIn = userService.findById(id);
        if (userLogedIn.isPresent()) {
            User user = userLogedIn.get();
            for (int i = 0; i < user.getPermissions().; i++) {
                //user.getRoles().get(i).removeUser(user);
            }
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user) {
        return userService.save(user);

    }

}

