package raf.rs.NwpNikolaDomaci3.requests;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String email;
    private String pass;
    private String name;
    private String lastName;

    private boolean readPermission;
    private boolean createPermission;
    private boolean deletePermission;
    private boolean updatePermission;
    private boolean canSearchMachines;
    private boolean canStartMachines;
    private boolean canStopMachines;
    private boolean canRestartMachines;
    private boolean canCreateMachines;
    private boolean canDestroyMachines;
}
