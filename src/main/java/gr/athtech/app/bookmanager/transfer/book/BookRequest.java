package gr.athtech.app.bookmanager.transfer.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record BookRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100)
        String title,

        @NotBlank(message = "Category is required")
        @Size(max = 80)
        String category,

        @NotNull(message = "Publish date is required (yyyy-MM-dd)")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate publishDate,

        Integer numberOfPages,

        @NotNull(message = "Author ID is required")
        Long authorId
) {}
