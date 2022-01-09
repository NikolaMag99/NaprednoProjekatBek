package raf.rs.NwpNikolaDomaci3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean canCreate;

    private boolean canDelete;

    private boolean canUpdate;

    private boolean canRead;

    private boolean canSearchMachines;

    private boolean canStartMachines;

    private boolean canStopMachines;

    private boolean canRestartMachines;

    private boolean canCreateMachines;

    private boolean canDestroyMachines;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    @JsonIgnore
    private User user;
}