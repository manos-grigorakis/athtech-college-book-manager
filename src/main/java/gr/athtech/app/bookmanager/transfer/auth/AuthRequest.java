package gr.athtech.app.bookmanager.transfer.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "Email is required")
        @Email
        @Size(max = 320)
        String email,

        @NotBlank(message = "Password is required")
        String password
) {}
