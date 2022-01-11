package raf.rs.NwpNikolaDomaci3.controllers;

import com.sipios.springsearch.anotation.SearchSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import raf.rs.NwpNikolaDomaci3.model.*;
import raf.rs.NwpNikolaDomaci3.repositories.ErrorMessRepository;
import raf.rs.NwpNikolaDomaci3.repositories.MachineRepository;
import raf.rs.NwpNikolaDomaci3.requests.MachineRequest;
import raf.rs.NwpNikolaDomaci3.services.ErrorService;
import raf.rs.NwpNikolaDomaci3.services.MachineService;
import raf.rs.NwpNikolaDomaci3.services.UserService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/machines/")
public class MachineController {


    private final UserService userService;
    private final MachineService machineService;
    private final ErrorService errorService;
    private MachineRepository machineRepository;
    private ErrorMessRepository errorMessRepository;

    LocalDate today = LocalDate.now();
    private LocalDate yearFromNow = today.plusYears(1);
    private LocalDate dayBefore = today.minusDays(5);


    public MachineController(UserService userService, MachineService machineService,ErrorService errorService, MachineRepository machineRepository, ErrorMessRepository errorMessRepository) {
        this.userService = userService;
        this.machineService = machineService;
        this.machineRepository = machineRepository;
        this.errorService = errorService;
        this.errorMessRepository = errorMessRepository;
    }


    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Machines saveMachine(@RequestBody MachineRequest machines, Authentication authentication) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
        if (userLogedIn.isPresent()) {
            Permission permission = userLogedIn.get().getPermissions();
            if (permission.isCanCreateMachines()) {
                Machines machine = new Machines();
                machine.setActive(machines.getActive());
                machine.setStatus(MachStatus.STOPPED);
                machine.setUser(userLogedIn.get());
                machine.setName(machines.getName());
                machine.setDateFrom(dayBefore);
                machine.setDateTo(yearFromNow);
                userLogedIn.get().addMachine(machine);
                System.out.println("You have create machine!");
                return machineService.save(machine);
            } else {
                System.out.println("You dont have permission to create machines! ");
            }
        }
        return null;
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Machines> getAllMachinesByUser(@RequestParam("userId") Long id) {
        return machineService.findAllByUserId(id);

    }

    @GetMapping(value = "/allMachines", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Machines> getAllMachines() {
        return machineService.findAll();
    }

//    @GetMapping(value = "/allErrors", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<ErrorMessage> getAllErrors(@RequestParam("machineId") Long id) {
//        return errorService.findById(id);
//    }

//    @GetMapping(value = "/allErrors",produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getAllErrors(@RequestParam("machineId") Long id) {
//        Optional<Machines> optionalMachine = machineService.findById(id);
//        List<ErrorMessage> listErrorsForMachine = errorService.findAll();
//        if (optionalMachine.isPresent()) {
//
//            return ResponseEntity.ok(optionalMachine.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/search")
    public ResponseEntity<List<Machines>> searchForMachines(@SearchSpec Specification<Machines> specs) {
        List<Machines> returnList = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
        List<Machines> list = machineRepository.findAll(Specification.where(specs));
        List<Machines> listMachinesForUser = machineRepository.findAllByUserId(userLogedIn.get().getId());
        if (userLogedIn.isPresent()) {
            Permission permission = userLogedIn.get().getPermissions();
            if (permission.isCanSearchMachines()) {
                for (Machines machines : listMachinesForUser) {
                    if (machines.isActive()) {
                        returnList.add(machines);
                    }
                }
            }
        }
        if (returnList.isEmpty()) {
            System.out.println("No machines");
            return ResponseEntity.status(403).build();
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/search/date")
    public ResponseEntity<List<Machines>> searchForMachinesByDate(@RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo) {
        List<Machines> returnList = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
        if (userLogedIn.isPresent()) {
            List<Machines> list = machineService.findAll();
            Permission permission = userLogedIn.get().getPermissions();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromDate = LocalDate.parse(dateFrom, formatter);
            LocalDate toDate = LocalDate.parse(dateTo, formatter);
            if (permission.isCanSearchMachines()) {
                for (Machines machines : list) {
                    if (machines.getDateFrom().isAfter(fromDate) && machines.getDateFrom().isBefore(toDate)) {
                        returnList.add(machines);
                    }
                }
            }
        }

        if (returnList.isEmpty()) {
            System.out.println("No machines");
            return ResponseEntity.status(403).build();
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/search/dateFrom")
    public ResponseEntity<List<Machines>> searchForMachinesByDateFrom(@RequestParam("dateFrom") String dateFrom) {
        List<Machines> returnList = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
        if (userLogedIn.isPresent()) {
            List<Machines> list = machineService.findAll();
            Permission permission = userLogedIn.get().getPermissions();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromDate = LocalDate.parse(dateFrom, formatter);
            if (permission.isCanSearchMachines()) {
                for (Machines machines : list) {
                    if (machines.getDateFrom().isAfter(fromDate)) {
                        returnList.add(machines);
                    }
                }
            }
        }

        if (returnList.isEmpty()) {
            System.out.println("No machines");
            return ResponseEntity.status(403).build();
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/search/dateTo")
    public ResponseEntity<List<Machines>> searchForMachinesByDateTo(@RequestParam("dateTo") String dateTo) {
        List<Machines> returnList = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
        if (userLogedIn.isPresent()) {
            List<Machines> list = machineService.findAll();
            Permission permission = userLogedIn.get().getPermissions();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate toDate = LocalDate.parse(dateTo, formatter);
            if (permission.isCanSearchMachines()) {
                for (Machines machines : list) {
                    if (machines.getDateFrom().isBefore(toDate)) {
                        returnList.add(machines);
                    }
                }
            }
        }

        if (returnList.isEmpty()) {
            System.out.println("No machines");
            return ResponseEntity.status(403).build();
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/search/status/{status}")
    public ResponseEntity<List<Machines>> searchForMachinesByStatus(@PathVariable String status) {
        List<Machines> returnList = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
        if (userLogedIn.isPresent()) {
            List<Machines> list = machineService.findAll();
            Permission permission = userLogedIn.get().getPermissions();
            if (permission.isCanSearchMachines()) {
                for (Machines machines : list) {
                    if (status.equals(MachStatus.RUNNING.toString())) {
                        if (machines.getStatus().equals(MachStatus.RUNNING)) {
                            returnList.add(machines);
                        }
                    } else {
                        if (machines.getStatus().equals(MachStatus.STOPPED)) {
                            returnList.add(machines);
                        }
                    }
                }
            }
        }

        if (returnList.isEmpty()) {
            System.out.println("No machines");
            return ResponseEntity.status(403).build();
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteMachine(@PathVariable Long id, Authentication authentication) {
        Optional<Machines> machineId = machineService.findById(id);
        Permission permission = userService.findByEmail(authentication.getName()).getPermissions();
        if (permission.isCanDestroyMachines() && machineId.get().getStatus().equals(MachStatus.STOPPED)) {
            Machines machines = machineId.get();
            machines.setActive(false);
            System.out.println("OK!");
            machineService.save(machines);
            return ResponseEntity.ok().build();
        }
        System.out.println("You dont have permission to delete!");
        return ResponseEntity.status(403).build();
    }

    @PostMapping(value = "/start")
    public ResponseEntity<?> startMachine(@RequestParam("machineId") Long id, Authentication authentication) {
        Optional<Machines> machineId = machineService.findById(id);
        Permission permission = userService.findByEmail(authentication.getName()).getPermissions();
//        List<Machines> list = machineRepository.findAllByUserId(id);
        if (permission.isCanStartMachines() && machineId.get().getStatus().equals(MachStatus.STOPPED)) {
            Machines machines = machineId.get();
            machines.setStatus(MachStatus.RUNNING);
            machineService.save(machines);
            System.out.println("Machine change status from STOPPED TO RUNNING");
            return ResponseEntity.ok().build();
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
            errorMessage.setOperation(MachineOperation.START);
            System.out.println("You dont have permission to start machines!");
            errorMessage.setMessage("You dont have permission to start machines!");
            errorMessage.setMachines(machineId.get());
            errorMessRepository.save(errorMessage);
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping(value = "/stop")
    public ResponseEntity<?> stopMachine(@RequestParam("userId") Long id, Authentication authentication) {
        Optional<Machines> machineId = machineService.findById(id);
        Permission permission = userService.findByEmail(authentication.getName()).getPermissions();
        if (permission.isCanStopMachines() && machineId.get().getStatus().equals(MachStatus.RUNNING)) {
            Machines machines = machineId.get();
            machines.setStatus(MachStatus.STOPPED);
            machineService.save(machines);
            System.out.println("Machine change status from RUNNING TO STOPPED");
            return ResponseEntity.ok().build();
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
            errorMessage.setOperation(MachineOperation.STOP);
            System.out.println("You dont have permission to stop machines!");
            errorMessage.setMessage("You dont have permission to stop machines!");
            errorMessage.setMachines(machineId.get());
            errorMessRepository.save(errorMessage);
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping(value = "/restart")
    public ResponseEntity<?> restartMachine(@RequestParam("userId") Long id, Authentication authentication) throws InterruptedException {
        Optional<Machines> machineId = machineService.findById(id);
        Permission permission = userService.findByEmail(authentication.getName()).getPermissions();
        if (permission.isCanDestroyMachines() && machineId.get().getStatus().equals(MachStatus.RUNNING)) {
            Machines machines = machineId.get();
            machines.setStatus(MachStatus.STOPPED);
            machineService.save(machines);
            System.out.println("Machine status before sleep");
            System.out.println(machines.getStatus());
            machines.setStatus(MachStatus.RUNNING);
            machineService.save(machines);
            System.out.println("Machine status after 5 sec");
            System.out.println(machines.getStatus());
            return ResponseEntity.ok().build();
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
            errorMessage.setOperation(MachineOperation.RESTART);
            System.out.println("You dont have permission to restart machines!");
            errorMessage.setMessage("You dont have permission to restart machines!");
            errorMessage.setMachines(machineId.get());
            errorMessRepository.save(errorMessage);
            return ResponseEntity.status(403).build();
        }
    }

}