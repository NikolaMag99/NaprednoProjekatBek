package raf.rs.NwpNikolaDomaci3.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raf.rs.NwpNikolaDomaci3.model.User;
import raf.rs.NwpNikolaDomaci3.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passEncoder;

    private UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passEncoder, UserRepository userRepository) {
        this.passEncoder = passEncoder;

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.findByEmail(username);
        if (myUser == null) {
            throw new UsernameNotFoundException("User name " + username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPass(), new ArrayList<>());
    }

    public User create(User user) {
        user.setPass(this.passEncoder.encode(user.getPass()));
        return this.userRepository.save(user);
    }

    public void deleteById(Long id) {
        this.userRepository.deleteById(id);

    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(Long.valueOf(id.longValue()));
    }


    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }


    public User update(User user) {
        User currUser = this.userRepository.findByEmail(user.getEmail());
        if (currUser == null) {
            throw new UsernameNotFoundException("User name " + user.getEmail() + " not found");
        }
        if (!(user.getPass().equals(currUser.getPass()))) {
            user.setPass(this.passEncoder.encode(user.getPass()));
        }
        return this.userRepository.save(user);
    }


    public Page<User> paginate(Integer page, Integer size) {
        return this.userRepository.findAll(PageRequest.of(page, size));
    }


}
