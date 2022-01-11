package raf.rs.NwpNikolaDomaci3.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import raf.rs.NwpNikolaDomaci3.model.ErrorMessage;
import raf.rs.NwpNikolaDomaci3.model.Machines;
import raf.rs.NwpNikolaDomaci3.model.User;

import java.util.List;

@Repository
public interface ErrorMessRepository extends CrudRepository<ErrorMessage, Long> {

//    List<ErrorMessage> findAllByMachineId(Long id);

}