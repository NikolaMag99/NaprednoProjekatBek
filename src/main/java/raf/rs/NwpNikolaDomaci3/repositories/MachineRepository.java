package raf.rs.NwpNikolaDomaci3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raf.rs.NwpNikolaDomaci3.model.MachStatus;
import raf.rs.NwpNikolaDomaci3.model.Machines;
import raf.rs.NwpNikolaDomaci3.model.User;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machines, Long>, JpaSpecificationExecutor<Machines> {
    List<Machines> findAllByUserId(Long id);

    @Query("select m from Machines m where " +
            "(:name is null or lower(m.name) like concat(concat('%',lower(:name)),'%')) and " +
            "((:dateFrom is null and :dateTo is null) or m.dateFrom between :dateFrom and :dateTo) and " +
            "(:isStatusEmpty = true or m.status in (:status)) and " +
            "m.user = :createdBy and " +
            "m.active = true")
    List<Machines> findMachines(
            @Param("name") String name, @Param("dateFrom") java.sql.Date dateFrom, @Param("dateTo") Date dateTo,
            @Param("status")List<MachStatus> status, @Param("isStatusEmpty") boolean isStatusEmpty, @Param("createdBy") User createdBy);


//    Optional<Machines> findMachineByMachineId(@Param("id") Long id, @Param("createdBy") User createdBy);

}

