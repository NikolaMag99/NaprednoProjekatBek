package raf.rs.NwpNikolaDomaci3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import raf.rs.NwpNikolaDomaci3.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public User findByEmail(String email);

}

