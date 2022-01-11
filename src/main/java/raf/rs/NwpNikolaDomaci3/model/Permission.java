package raf.rs.NwpNikolaDomaci3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Permission {


    public Permission() {
    }

    public Permission(boolean canCreate, boolean canRead, boolean canUpdate, boolean canDelete,
                      boolean canSearchMachines, boolean canStartMachines, boolean canStopMachines, boolean canRestartMachines,
                      boolean canCreateMachines, boolean canDestroyMachines, User user) {
        this.canCreate = canCreate;
        this.canRead = canRead;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
        this.canSearchMachines = canSearchMachines;
        this.canStartMachines = canStartMachines;
        this.canStopMachines = canStopMachines;
        this.canRestartMachines = canRestartMachines;
        this.canCreateMachines = canCreateMachines;
        this.canDestroyMachines = canDestroyMachines;
        this.user = user;
    }

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