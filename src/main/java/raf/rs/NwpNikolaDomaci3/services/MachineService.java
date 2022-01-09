package raf.rs.NwpNikolaDomaci3.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raf.rs.NwpNikolaDomaci3.model.Machines;
import raf.rs.NwpNikolaDomaci3.model.User;
import raf.rs.NwpNikolaDomaci3.repositories.MachineRepository;
import raf.rs.NwpNikolaDomaci3.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MachineService implements IService<Machines, Long> {

    private MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository) {


        this.machineRepository = machineRepository;
    }


    @Override
    public Machines save(Machines machines) {
        return machineRepository.save(machines);
    }

    @Override
    public Optional<Machines> findById(Long id) {
        return machineRepository.findById(id);
    }

    @Override
    public List<Machines> findAll() {
        return (List<Machines>) machineRepository.findAll();
    }


    public List<Machines> findAllByUserId(Long id) {
        return (List<Machines>) this.machineRepository.findAllByUserId(id);
    }

    public Machines create(Machines machines) {
        return this.machineRepository.save(machines);
    }


    public void deleteById(Long id) {
        this.machineRepository.deleteById(id);

    }
}
