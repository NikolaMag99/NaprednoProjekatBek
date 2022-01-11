package raf.rs.NwpNikolaDomaci3.responses;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwt;

    private Long id;

    public LoginResponse(String jwt, Long id) {
        this.jwt = jwt;
        this.id = id;
    }


}
