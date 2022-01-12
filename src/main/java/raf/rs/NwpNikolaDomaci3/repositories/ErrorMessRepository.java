package raf.rs.NwpNikolaDomaci3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raf.rs.NwpNikolaDomaci3.model.ErrorMessage;
import raf.rs.NwpNikolaDomaci3.model.Machines;
import raf.rs.NwpNikolaDomaci3.model.User;

import java.util.List;

@Repository
public interface ErrorMessRepository extends JpaRepository<ErrorMessage, Long> {

    @Query("select em from ErrorMessage em where em.machines.user = :user")
    List<ErrorMessage> findErrorMessagesByUser(@Param("user") User user);

}