package com.komarova.spring_eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String matchingPassword;

}
