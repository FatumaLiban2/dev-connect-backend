package org.devconnect.devconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UserResponseDTO user;
    private String tokenType = "Bearer";
}

