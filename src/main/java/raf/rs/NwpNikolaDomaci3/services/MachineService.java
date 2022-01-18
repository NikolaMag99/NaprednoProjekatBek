package raf.rs.NwpNikolaDomaci3.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import raf.rs.NwpNikolaDomaci3.model.*;
import raf.rs.NwpNikolaDomaci3.repositories.ErrorMessRepository;
import raf.rs.NwpNikolaDomaci3.repositories.MachineRepository;

import java.sql.Date;
import java.util.*;

@Service
public class MachineService implements IService<Machines, Long>, MachineServiceInterface {

    private MachineRepository machineRepository;

    private ErrorMessRepository errorMessRepository;

    private final TaskScheduler taskScheduler;

    @Autowired
    public MachineService(MachineRepository machineRepository, TaskScheduler taskScheduler, ErrorMessRepository errorMessRepository) {


        this.machineRepository = machineRepository;
        this.taskScheduler = taskScheduler;
        this.errorMessRepository = errorMessRepository;
    }


    @Override
    public Machines save(Machines machines) {
        return machineRepository.save(machines);
    }

    @Override
    public Optional<Machines> findById(Long id) {
        return machineRepository.findById(id);
    }

    @Override
    public List<Machines> findAll() {
        return (List<Machines>) machineRepository.findAll();
    }


    public List<Machines> findAllByUserId(Long id) {
        return (List<Machines>) this.machineRepository.findAllByUserId(id);
    }

    public Machines create(Machines machines) {
        return this.machineRepository.save(machines);
    }


    public void deleteById(Long id) {
        this.machineRepository.deleteById(id);

    }


    @Override
    public List<Machines> searchByParameters(String name, java.sql.Date dateFrom, java.sql.Date dateTo, List<MachStatus> status, User user) {
        return machineRepository.findMachinesByParameters(name, dateFrom, dateTo, status, CollectionUtils.isEmpty(status), user);
    }

    @Override
    @Async
    public void start(Long id, User user) throws Exception {
        Permission permission = user.getPermissions();
        Machines machines = checkMachine(id, user);
        if (permission.isCanStartMachines() && machines.getStatus().equals(MachStatus.STOPPED)) {
            machines.setBusy(true);
            machines = machineRepository.save(machines);
            Thread.sleep(10000);
            machines.setStatus(MachStatus.RUNNING);
            machines.setBusy(false);
            machineRepository.save(machines);
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
            errorMessage.setOperation(MachineOperation.START);
            System.out.println("You dont have permission to start machines!");
            errorMessage.setMessage("You dont have permission to start machines!");
            errorMessage.setMachines(machines);
            errorMessRepository.save(errorMessage);
        }
    }

    @Override
    @Async
    public void stop(Long id, User user) throws Exception {
        Permission permission = user.getPermissions();
        Machines machines = checkMachine(id, user);
        if (permission.isCanStopMachines() && machines.getStatus().equals(MachStatus.RUNNING)) {
            machines.setBusy(true);
            machines = machineRepository.save(machines);
            Thread.sleep(10000);
            machines.setStatus(MachStatus.STOPPED);
            machines.setBusy(false);
            machineRepository.save(machines);
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
            errorMessage.setOperation(MachineOperation.STOP);
            System.out.println("You dont have permission to stop machines!");
            errorMessage.setMessage("You dont have permission to stop machines!");
            errorMessage.setMachines(machines);
            errorMessRepository.save(errorMessage);
        }
    }

    @Override
    @Async
    public void restart(Long id, User user) throws Exception {
        Permission permission = user.getPermissions();
        Machines machines = checkMachine(id, user);
        Optional<Machines> machine = machineRepository.findById(id);
        if (permission.isCanRestartMachines() && machines.getStatus().equals(MachStatus.RUNNING) && !machine.get().getBusy()) {
            machines.setBusy(true);
            machines = machineRepository.save(machines);
            Thread.sleep(5000);
            machines.setStatus(MachStatus.STOPPED);
            machines = machineRepository.save(machines);
            Thread.sleep(5000);
            machines.setBusy(false);
            machines.setStatus(MachStatus.RUNNING);
            machineRepository.save(machines);
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
            errorMessage.setOperation(MachineOperation.RESTART);
            System.out.println("You dont have permission to restart machines!");
            errorMessage.setMessage("You dont have permission to restart machines!");
            errorMessage.setMachines(machines);
            errorMessRepository.save(errorMessage);
        }
    }

    @Override
    @Async
    public void schedule(Long id, java.sql.Date scheduleAt, MachineOperation operation, User user) {
        this.taskScheduler.schedule(() -> {
            try {
                switch (operation) {
                    case STOP:
                        this.stop(id, user);
                        break;
                    case START:
                        this.start(id, user);
                        break;
                    case RESTART:
                        this.restart(id, user);
                        break;
                    default:
                        throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setMessage(e.getMessage());
                errorMessage.setOperation(operation);
                errorMessage.setMachines(findById(id).orElse(null));
                errorMessage.setDate(scheduleAt);
                errorMessRepository.save(errorMessage);
            }
        }, scheduleAt);
    }

    private Machines checkMachine(Long id, User user) throws Exception {
        Machines machine = machineRepository.findById(id).orElse(null);
        if (machine.getBusy()) {
            System.out.println("Masina je zauzeta");
            ResponseEntity.status(403).build();
        }
        if (!Objects.equals(user.getId(), machine.getUser().getId())) {
            System.out.println("Nije isti user");
            ResponseEntity.status(403).build();
        }
        return machine;
    }
}
