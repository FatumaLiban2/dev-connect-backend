package org.devconnect.devconnectbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDTO {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "Password is required")
    private String newPassword;

    @NotBlank(message = "Confirm new password is required")
    @Size(min = 8, message = "Confirm new password must be at least 8 characters long")
    private String confirmNewPassword;
}
