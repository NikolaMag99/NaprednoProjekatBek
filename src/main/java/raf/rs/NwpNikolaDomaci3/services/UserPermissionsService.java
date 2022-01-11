package raf.rs.NwpNikolaDomaci3.services;

import org.springframework.stereotype.Service;
import raf.rs.NwpNikolaDomaci3.model.Permission;
import raf.rs.NwpNikolaDomaci3.repositories.UserPermissionsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserPermissionsService implements IService<Permission, Long> {

    private final UserPermissionsRepository repository;

    public UserPermissionsService(UserPermissionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Permission save(Permission permissions) {
        return repository.save(permissions);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Permission> findAll() {
        return (List<Permission>) repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
