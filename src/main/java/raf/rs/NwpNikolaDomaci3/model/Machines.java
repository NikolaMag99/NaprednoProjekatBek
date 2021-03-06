package raf.rs.NwpNikolaDomaci3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Machines {


    @Id
    @Column(name = "machine_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MachStatus status;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    private Boolean active;

    @Column(nullable = false)
    private Boolean busy = false;

    @Column(nullable = false)
    private String name;

    private java.sql.Date dateFrom;

    private java.sql.Date dateTo;

    @OneToMany(mappedBy = "machines", cascade =  CascadeType.ALL)
    @ToString.Exclude
    @JsonManagedReference
    private  List<ErrorMessage> errorMessages;

}
