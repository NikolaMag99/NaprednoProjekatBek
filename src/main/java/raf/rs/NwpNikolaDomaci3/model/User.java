package raf.rs.NwpNikolaDomaci3.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class User {



    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    @NotBlank(message = "Email is mandatory")
    private String email;
    @Column
    @NotBlank(message = "Username is mandatory")
    private String name;
    @Column
    @NotBlank(message = "LastName is mandatory")
    private String lastName;

    @Column
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pass;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_PERMISSIONS",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )

    private Set<Permission> permissions = new HashSet<>();

    public void addPermission(Permission permission) {
        permissions.add(permission);
        //permission.getUsers().add(this);
    }

    public void addPermissions(Set<Permission> permissions) {
        this.permissions.addAll(permissions);
//        for(Permission p : permission) {
//            p.getUsers().add(this);
//        }
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
        //permission.getUsers().remove(this);
    }


}
