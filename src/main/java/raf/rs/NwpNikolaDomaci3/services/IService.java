package raf.rs.NwpNikolaDomaci3.services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IService<T, ID> {

    <S extends T> S save(S id);

    Optional<T> findById(ID id);

    List<T> findAll();

    void deleteById(ID id);

}
