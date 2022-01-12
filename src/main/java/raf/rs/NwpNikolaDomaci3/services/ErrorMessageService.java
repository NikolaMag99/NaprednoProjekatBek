package raf.rs.NwpNikolaDomaci3.services;

import org.springframework.stereotype.Service;
import raf.rs.NwpNikolaDomaci3.model.ErrorMessage;
import raf.rs.NwpNikolaDomaci3.model.User;
import raf.rs.NwpNikolaDomaci3.repositories.ErrorMessRepository;


import java.util.List;
import java.util.Optional;

@Service
public class ErrorMessageService implements ErrorMessageInterfaceService {

    private final ErrorMessRepository errorMessRepository;

    public ErrorMessageService(ErrorMessRepository errorMessRepository) {
        this.errorMessRepository = errorMessRepository;
    }


    @Override
    public List<ErrorMessage> findErrorMessagesForUser(User user) {
        return errorMessRepository.findErrorMessagesByUser(user);
    }

    @Override
    public <S extends ErrorMessage> S save(S id) {
        return errorMessRepository.save(id);
    }

    @Override
    public Optional<ErrorMessage> findById(Long aLong) {
        return errorMessRepository.findById(aLong);
    }

    @Override
    public List<ErrorMessage> findAll() {
        return errorMessRepository.findAll();
    }

    @Override
    public void deleteById(Long aLong) {
        errorMessRepository.deleteById(aLong);
    }
}
