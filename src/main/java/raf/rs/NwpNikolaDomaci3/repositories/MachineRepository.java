package raf.rs.NwpNikolaDomaci3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import raf.rs.NwpNikolaDomaci3.model.Machines;
import raf.rs.NwpNikolaDomaci3.model.User;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machines, Long>, JpaSpecificationExecutor<Machines> {
    List<Machines> findAllByUserId(Long id);


}

