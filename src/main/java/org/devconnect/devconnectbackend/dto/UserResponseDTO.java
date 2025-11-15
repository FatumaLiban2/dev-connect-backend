package org.devconnect.devconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devconnect.devconnectbackend.model.User.UserStatus;
import org.devconnect.devconnectbackend.model.User.UserRole;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private UserRole userRole;
    private UserStatus userStatus;
    private LocalDateTime createdAt;
    private LocalDateTime lastSeen;
    private boolean isActive;
}
