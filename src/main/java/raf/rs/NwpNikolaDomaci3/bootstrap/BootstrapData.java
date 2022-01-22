package raf.rs.NwpNikolaDomaci3.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.rs.NwpNikolaDomaci3.model.*;
import raf.rs.NwpNikolaDomaci3.repositories.ErrorMessRepository;
import raf.rs.NwpNikolaDomaci3.repositories.MachineRepository;
import raf.rs.NwpNikolaDomaci3.repositories.UserPermissionsRepository;
import raf.rs.NwpNikolaDomaci3.repositories.UserRepository;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;



@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MachineRepository machineRepository;

    private final ErrorMessRepository errorMessRepository;

    private final UserPermissionsRepository userPermissionsRepository;


    LocalDate today = LocalDate.now();
    LocalDate year = today.plusYears(1);
    Permission permission = new Permission();
    Permission permission2 = new Permission();
    java.sql.Date logicalDate;
    Calendar c = Calendar.getInstance();



    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder, MachineRepository machineRepository, ErrorMessRepository errorMessRepository,  UserPermissionsRepository userPermissionsRepository) throws ParseException {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.machineRepository = machineRepository;
        this.errorMessRepository = errorMessRepository;
        this.userPermissionsRepository = userPermissionsRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setEmail("user@gmail.com");
        user.setName("user");
        user.setLastName("user");
        user.setPass(this.passwordEncoder.encode("user"));
        userRepository.save(user);
        permission.setCanCreate(true);
        permission.setCanUpdate(true);
        permission.setCanDelete(true);
        permission.setCanRead(true);
        permission.setCanStartMachines(true);
        permission.setCanCreateMachines(true);
        permission.setCanDestroyMachines(true);
        permission.setCanRestartMachines(true);
        permission.setCanSearchMachines(true);
        permission.setCanStopMachines(true);
        permission.setUser(user);
        userPermissionsRepository.save(permission);
        user.setPermissions(permission);
        userRepository.save(user);

        User user2 = new User();
        user2.setEmail("user2@gmail.com");
        user2.setName("user2");
        user2.setLastName("user2");
        user2.setPass(this.passwordEncoder.encode("user2"));
        userRepository.save(user2);
        permission2.setCanCreate(true);
        permission2.setCanUpdate(true);
        permission2.setCanDelete(true);
        permission2.setCanRead(true);
        permission2.setCanStartMachines(true);
        permission2.setCanCreateMachines(true);
        permission2.setCanDestroyMachines(false);
        permission2.setCanRestartMachines(true);
        permission2.setCanSearchMachines(true);
        permission2.setCanStopMachines(true);
        permission2.setUser(user2);
        userPermissionsRepository.save(permission2);
        user2.setPermissions(permission2);
        userRepository.save(user2);


        Machines machines = new Machines();
        machines.setStatus(MachStatus.RUNNING);
        machines.setName("Masina 1");
        machines.setDateFrom(new Date(Calendar.getInstance().getTime().getTime()));
        machines.setDateTo(new java.sql.Date(c.getTimeInMillis()));
        machines.setActive(true);
        machines.setUser(user);
        machineRepository.save(machines);

        Machines machines2 = new Machines();
        machines2.setStatus(MachStatus.STOPPED);
        machines2.setName("Masina 2");
        machines2.setDateFrom(new Date(Calendar.getInstance().getTime().getTime()));
        machines2.setDateTo(new java.sql.Date(c.getTimeInMillis()));
        machines2.setActive(true);
        machines2.setUser(user2);
        machineRepository.save(machines2);

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setOperation(MachineOperation.STOP);
        errorMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        errorMessage.setMessage("Failed to stop machine");
        errorMessage.setMachines(machines);
        errorMessRepository.save(errorMessage);


        System.out.println("Data loaded!");

    }
}
