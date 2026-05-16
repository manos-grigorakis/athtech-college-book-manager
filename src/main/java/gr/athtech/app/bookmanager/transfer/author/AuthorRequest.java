package gr.athtech.app.bookmanager.transfer.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequest(
    @NotBlank(message = "First name is required")
    @Size(max = 50)
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    String lastName,

    @NotBlank(message = "Country is required")
    @Size(max = 50)
    String country
) {}
