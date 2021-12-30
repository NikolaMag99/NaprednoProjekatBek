package raf.rs.NwpNikolaDomaci3.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.rs.NwpNikolaDomaci3.model.Permission;
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

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setEmail("user" + i + "@gmail.com");
            user.setName("user" + i);
            user.setLastName("user" + i);
            user.setPass(this.passwordEncoder.encode("user" + i));

            Permission permission = new Permission();
            if(i % 2 != 0){
                permission.setCan_create_user(1);
                permission.setCan_delete_user(1);
                permission.setCan_read_user(1);
                permission.setCan_update_user(1);
            }
            else{
                permission.setCan_create_user(1);
                permission.setCan_delete_user(0);
                permission.setCan_read_user(0);
                permission.setCan_update_user(1);
            }

            user.setPermission(permission);

            userRepository.save(user);

            System.out.println("Data loaded!");
        }
    }
}
