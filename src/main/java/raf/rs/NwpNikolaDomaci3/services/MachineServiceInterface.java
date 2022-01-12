package raf.rs.NwpNikolaDomaci3.services;

import raf.rs.NwpNikolaDomaci3.model.MachStatus;
import raf.rs.NwpNikolaDomaci3.model.MachineOperation;
import raf.rs.NwpNikolaDomaci3.model.Machines;
import raf.rs.NwpNikolaDomaci3.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MachineServiceInterface {


//    Optional<Machines> findByIdUser(Long id, User user);

//    List<Machines> searchByParameters(String name, Date dateFrom, Date dateTo, List<MachStatus> status, User user);

    void start(Long id, User user) throws Exception;

    void stop(Long id, User user) throws Exception;

    void restart(Long id, User user) throws Exception;

    void schedule(Long id, java.sql.Date scheduleAt, MachineOperation action, User user);
}
