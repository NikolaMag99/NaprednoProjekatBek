package raf.rs.NwpNikolaDomaci3.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Embeddable
public class Permission {
    private int can_create_user;
    private int can_read_user;
    private int can_update_user;
    private int can_delete_user;
}