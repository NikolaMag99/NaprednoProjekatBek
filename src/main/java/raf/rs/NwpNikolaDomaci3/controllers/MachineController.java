package raf.rs.NwpNikolaDomaci3.controllers;

import com.sipios.springsearch.anotation.SearchSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import raf.rs.NwpNikolaDomaci3.model.*;
import raf.rs.NwpNikolaDomaci3.repositories.ErrorMessRepository;
import raf.rs.NwpNikolaDomaci3.repositories.MachineRepository;
import raf.rs.NwpNikolaDomaci3.repositories.UserRepository;
import raf.rs.NwpNikolaDomaci3.requests.MachineRequest;
import raf.rs.NwpNikolaDomaci3.services.ErrorMessageService;
import raf.rs.NwpNikolaDomaci3.services.MachineService;
import raf.rs.NwpNikolaDomaci3.services.UserService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@CrossOrigin
@RestController
@RequestMapping("/api/machines/")
public class MachineController {


    private final UserService userService;
    private final UserRepository userRepository;
    private final MachineService machineService;
    private final ErrorMessageService errorService;
    private MachineRepository machineRepository;
    private ErrorMessRepository errorMessRepository;

    LocalDate today = LocalDate.now();
    private LocalDate yearFromNow = today.plusYears(1);
    private LocalDate dayBefore = today.minusDays(5);
    Calendar c = Calendar.getInstance();



    public MachineController(UserService userService, UserRepository userRepository, MachineService machineService, ErrorMessageService errorService, MachineRepository machineRepository, ErrorMessRepository errorMessRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.machineService = machineService;
        this.machineRepository = machineRepository;
        this.errorService = errorService;
        this.errorMessRepository = errorMessRepository;
    }


    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Machines saveMachine(@RequestBody MachineRequest machines, Authentication authentication) {
        String username = getContext().getAuthentication().getName();
        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
        if (userLogedIn.isPresent()) {
            Permission permission = userLogedIn.get().getPermissions();
            if (permission.isCanCreateMachines()) {
                Machines machine = new Machines();
                machine.setActive(machines.getActive());
                machine.setStatus(MachStatus.STOPPED);
                machine.setUser(userLogedIn.get());
                machine.setName(machines.getName());
                machine.setDateFrom(new Date(Calendar.getInstance().getTime().getTime()));
                machine.setDateTo(new java.sql.Date(c.getTimeInMillis()));
                userLogedIn.get().addMachine(machine);
                System.out.println("You have create machine!");
                return machineService.save(machine);
            } else {
                System.out.println("You dont have permission to create machines! ");
            }
        }
        return null;
    }

    private User getCurrentUser() {
        User userDetails =
                (User) getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getEmail());
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Machines> getAllMachinesByUser(@RequestParam("userId") Long id) {
        List<Machines> list = machineService.findAllByUserId(id);
        List<Machines> machinesList = new ArrayList();
        for (Machines m : list) {
            if (m.getActive()) {
                machinesList.add(m);
            }
        }
        return machinesList;
    }

    @GetMapping(value = "/allMachines", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Machines> getAllMachines() {
        return machineService.findAll();
    }

    @GetMapping(value = "/allErrors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ErrorMessage> getAllErrors(Authentication authentication) {
        return errorService.findErrorMessagesForUser(userService.findByEmail(authentication.getName()));
    }

    @GetMapping(value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchMachines(
            @RequestParam(value = "dateFrom", required = false) java.sql.Date dateFrom,
            @RequestParam(value = "dateTo", required = false) java.sql.Date dateTo,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) List<MachStatus> status,
            Authentication authentication
    ) {
        System.out.println(dateFrom);
        System.out.println(name);
        if ((dateFrom != null && dateTo == null) || (dateFrom == null && dateTo != null)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Must have both dates or none");
        }
        return ResponseEntity.ok(machineService.searchByParameters(name, dateFrom, dateTo, status, userService.findByEmail(authentication.getName())));
    }

//    @GetMapping("/read/search")
//    public ResponseEntity<List<Machines>> searchForMachines(@SearchSpec Specification<Machines> specs) {
//        List<Machines> returnList = new ArrayList<>();
//        String username = getContext().getAuthentication().getName();
//        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
//        List<Machines> list = machineRepository.findAll(Specification.where(specs));
//        List<Machines> listMachinesForUser = machineRepository.findAllByUserId(userLogedIn.get().getId());
//        if (userLogedIn.isPresent()) {
//            Permission permission = userLogedIn.get().getPermissions();
//            if (permission.isCanSearchMachines()) {
//                for (Machines machines : listMachinesForUser) {
//                    if (machines.getActive()) {
//                        returnList.add(machines);
//                    }
//                }
//            }
//        }
//        if (returnList.isEmpty()) {
//            System.out.println("No machines");
//            return ResponseEntity.status(403).build();
//        }
//        return new ResponseEntity<>(returnList, HttpStatus.OK);
//    }

//    @GetMapping("/search/date")
//    public ResponseEntity<List<Machines>> searchForMachinesByDate(@RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo) {
//        List<Machines> returnList = new ArrayList<>();
//        String username = getContext().getAuthentication().getName();
//        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
//        if (userLogedIn.isPresent()) {
//            List<Machines> list = machineService.findAll();
//            Permission permission = userLogedIn.get().getPermissions();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate fromDate = LocalDate.parse(dateFrom, formatter);
//            LocalDate toDate = LocalDate.parse(dateTo, formatter);
//            if (permission.isCanSearchMachines()) {
//                for (Machines machines : list) {
//                    if (machines.getDateFrom().isAfter(fromDate) && machines.getDateFrom().isBefore(toDate)) {
//                        returnList.add(machines);
//                    }
//                }
//            }
//        }
//
//        if (returnList.isEmpty()) {
//            System.out.println("No machines");
//            return ResponseEntity.status(403).build();
//        }
//        return new ResponseEntity<>(returnList, HttpStatus.OK);
//    }
//
//    @GetMapping("/search/dateFrom")
//    public ResponseEntity<List<Machines>> searchForMachinesByDateFrom(@RequestParam("dateFrom") String dateFrom) {
//        List<Machines> returnList = new ArrayList<>();
//        String username = getContext().getAuthentication().getName();
//        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
//        if (userLogedIn.isPresent()) {
//            List<Machines> list = machineService.findAll();
//            Permission permission = userLogedIn.get().getPermissions();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate fromDate = LocalDate.parse(dateFrom, formatter);
//            if (permission.isCanSearchMachines()) {
//                for (Machines machines : list) {
//                    if (machines.getDateFrom().isAfter(fromDate)) {
//                        returnList.add(machines);
//                    }
//                }
//            }
//        }
//
//        if (returnList.isEmpty()) {
//            System.out.println("No machines");
//            return ResponseEntity.status(403).build();
//        }
//        return new ResponseEntity<>(returnList, HttpStatus.OK);
//    }
//
//    @GetMapping("/search/dateTo")
//    public ResponseEntity<List<Machines>> searchForMachinesByDateTo(@RequestParam("dateTo") String dateTo) {
//        List<Machines> returnList = new ArrayList<>();
//        String username = getContext().getAuthentication().getName();
//        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
//        if (userLogedIn.isPresent()) {
//            List<Machines> list = machineService.findAll();
//            Permission permission = userLogedIn.get().getPermissions();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate toDate = LocalDate.parse(dateTo, formatter);
//            if (permission.isCanSearchMachines()) {
//                for (Machines machines : list) {
//                    if (machines.getDateFrom().isBefore(toDate)) {
//                        returnList.add(machines);
//                    }
//                }
//            }
//        }
//
//        if (returnList.isEmpty()) {
//            System.out.println("No machines");
//            return ResponseEntity.status(403).build();
//        }
//        return new ResponseEntity<>(returnList, HttpStatus.OK);
//    }

//    @GetMapping("/search/status/{status}")
//    public ResponseEntity<List<Machines>> searchForMachinesByStatus(@PathVariable String status) {
//        List<Machines> returnList = new ArrayList<>();
//        String username = getContext().getAuthentication().getName();
//        Optional<User> userLogedIn = Optional.ofNullable(userService.findByEmail(username));
//        if (userLogedIn.isPresent()) {
//            List<Machines> list = machineService.findAll();
//            Permission permission = userLogedIn.get().getPermissions();
//            if (permission.isCanSearchMachines()) {
//                for (Machines machines : list) {
//                    if (status.equals(MachStatus.RUNNING.toString())) {
//                        if (machines.getStatus().equals(MachStatus.RUNNING)) {
//                            returnList.add(machines);
//                        }
//                    } else {
//                        if (machines.getStatus().equals(MachStatus.STOPPED)) {
//                            returnList.add(machines);
//                        }
//                    }
//                }
//            }
//        }
//
//        if (returnList.isEmpty()) {
//            System.out.println("No machines");
//            return ResponseEntity.status(403).build();
//        }
//        return new ResponseEntity<>(returnList, HttpStatus.OK);
//    }

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

    @PostMapping(value = "/schedule")
    public ResponseEntity<?> scheduleAction(@RequestParam("scheduledTime") java.sql.Date scheduledTime,
                                            @RequestParam("machineId") Long id,
                                            @RequestParam("operation") MachineOperation operation,
                                            Authentication authentication) {
//        Permission permission = userService.findByEmail(authentication.getName()).getPermissions();
//        User currentUser = getCurrentUser();
        Optional<Machines> optionalMachine = machineService.findById(id);
        if (optionalMachine.isPresent()) {
            machineService.schedule(id, scheduledTime, operation, userService.findByEmail(authentication.getName()));
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/start")
    public ResponseEntity<?> startMachine(@RequestParam("machineId") Long id, Authentication authentication) {
        Optional<Machines> machineId = machineService.findById(id);
        try {
            machineService.start(id, userService.findByEmail(authentication.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Machines machines = machineId.get();
        machines.setBusy(true);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/stop")
    public ResponseEntity<?> stopMachine(@RequestParam("machineId") Long id, Authentication authentication) {
        Optional<Machines> machineId = machineService.findById(id);
        Permission permission = userService.findByEmail(authentication.getName()).getPermissions();
        try {
//            if (permission.isCanStopMachines() && machineId.get().getStatus().equals(MachStatus.RUNNING)) {
            machineService.stop(id, userService.findByEmail(authentication.getName()));
            System.out.println("Machine change status from RUNNING TO STOPPED");
//            } else {
//                ErrorMessage errorMessage = new ErrorMessage();
//                errorMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
//                errorMessage.setOperation(MachineOperation.STOP);
//                System.out.println("You dont have permission to stop machines!");
//                errorMessage.setMessage("You dont have permission to stop machines!");
//                errorMessage.setMachines(machineId.get());
//                errorMessRepository.save(errorMessage);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Machines machines = machineId.get();
        machines.setBusy(true);
        return ResponseEntity.ok().build();

    }

    @PostMapping(value = "/restart")
    public ResponseEntity<?> restartMachine(@RequestParam("machineId") Long id, Authentication authentication) throws InterruptedException {
        Optional<Machines> machineId = machineService.findById(id);
        try {
            machineService.restart(id, userService.findByEmail(authentication.getName()));
            System.out.println("Machine change status from STOPPED TO RUNNING");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Machines machines = machineId.get();
        machines.setBusy(true);
        return ResponseEntity.ok().build();
    }

}