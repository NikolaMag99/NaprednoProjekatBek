package raf.rs.NwpNikolaDomaci3.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.rs.NwpNikolaDomaci3.model.User;
import raf.rs.NwpNikolaDomaci3.repositories.UserRepository;


@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Data Loading....");

        User user1 = new User("nikola@gmail.com", this.passwordEncoder.encode("nikola"), "NIKOLA", "PAUN", true, true, true, true);
        this.userRepository.save(user1);

        User user2 = new User("stefan@gmail.com", this.passwordEncoder.encode("stefan"), "STEFAN", "STEFI");
        this.userRepository.save(user2);

        User user3 = new User("pegaz@gmail.com", this.passwordEncoder.encode("pegaz"), "PEGAZ", "PEGI");
        this.userRepository.save(user3);

        System.out.println("Data loaded!");
    }
}
