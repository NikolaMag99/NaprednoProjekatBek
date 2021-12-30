package raf.rs.NwpNikolaDomaci3.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String lastName;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pass;


    @OneToOne(cascade = CascadeType.ALL)
    private Permission permissions;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "can_create_user", column = @Column(name = "can_create_user")),
//            @AttributeOverride(name = "can_read_user", column = @Column(name = "can_read_user")),
//            @AttributeOverride(name = "can_update_user", column = @Column(name = "can_update_user")),
//            @AttributeOverride(name = "can_delete_user", column = @Column(name = "can_delete_user"))
//    })
//    private Permission permission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Permission getPermissions() {
        return permissions;
    }

    public void setPermissions(Permission permissions) {
        this.permissions = permissions;
    }
}
