package raf.rs.NwpNikolaDomaci3.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;


@Data
@Entity
public class ErrorMessage {

    @Id
    @Column(name = "error_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    private String message;

    @Enumerated(EnumType.STRING)
    private MachineOperation operation;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "MACHINE_ID", referencedColumnName = "machine_id")
    private Machines machines;

}
