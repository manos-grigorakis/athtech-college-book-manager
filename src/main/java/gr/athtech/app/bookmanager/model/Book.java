package gr.athtech.app.bookmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
@Entity
public class Book extends BaseModel {
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "category", nullable = false, length = 80)
    private String category;

    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
