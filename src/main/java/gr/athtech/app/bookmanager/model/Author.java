package gr.athtech.app.bookmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true, exclude = {"books"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "authors")
@Entity
public class Author extends BaseModel {
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "number_of_published_books")
    private Integer numberOfPublishedBooks;

    @OneToMany(mappedBy = "author",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Book> books;

    /**
     * Concatenates the {@link #firstName} and {@link #lastName} of the {@link Author}
     * @return The full name of the {@link Author}
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Computes the {@link #numberOfPublishedBooks} using the Bi-Directional relationship with {@link Book}
     * @return The {@link #numberOfPublishedBooks} if not null, otherwise {@code 0}
     */
    public Integer getNumberOfPublishedBooks() {
        return books != null ? books.size() : 0;
    }
}
