package raf.rs.NwpNikolaDomaci3.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.rs.NwpNikolaDomaci3.model.ErrorMessage;
import raf.rs.NwpNikolaDomaci3.model.Machines;
import raf.rs.NwpNikolaDomaci3.repositories.ErrorMessRepository;
import raf.rs.NwpNikolaDomaci3.repositories.MachineRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ErrorService implements IService<ErrorMessage, Long> {

    private ErrorMessRepository errorMessRepository;

    @Autowired
    public ErrorService(ErrorMessRepository errorMessRepository) {


        this.errorMessRepository = errorMessRepository;
    }


    @Override
    public ErrorMessage save(ErrorMessage message) {
        return errorMessRepository.save(message);
    }

    @Override
    public Optional<ErrorMessage> findById(Long id) {
        return errorMessRepository.findById(id);
    }

    @Override
    public List<ErrorMessage> findAll() {
        return (List<ErrorMessage>) errorMessRepository.findAll();
    }


    public ErrorMessage create(ErrorMessage machines) {
        return this.errorMessRepository.save(machines);
    }


    public void deleteById(Long id) {
        this.errorMessRepository.deleteById(id);

    }
}
