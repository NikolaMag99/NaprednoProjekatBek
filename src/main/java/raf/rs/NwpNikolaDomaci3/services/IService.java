package raf.rs.NwpNikolaDomaci3.services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IService<User, ID> {

    <S extends User> S save(S id);

    Optional<User> findById(ID id);

    List<User> findAll();

    void deleteById(ID id);

    UserDetails loadUserByUsername(String username)throws UsernameNotFoundException;


}
