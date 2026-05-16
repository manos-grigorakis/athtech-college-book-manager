package gr.athtech.app.bookmanager.specs;

import gr.athtech.app.bookmanager.model.Author;
import gr.athtech.app.bookmanager.model.Book;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BookSpecs {
    public static Specification<Book> likeTitle(String title) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
    }

    public static Specification<Book> likeCategory(String category) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(
                        root.get("category")), "%" + category.toLowerCase() + "%"));
    }

    public static Specification<Book> equalPublishDate(LocalDate publishDate) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("publishDate"), publishDate));
    }

    public static Specification<Book> equalNumberOfPages(Integer numberOfPages) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("numberOfPages"), numberOfPages));
    }

    /**
     * Searches for a {@link Book} with the provided {@link Author} name (full name and last name)
     * @param authorName The {@link Author} name to search
     * @return The result of the query
     */
    public static Specification<Book> likeAuthorName(String authorName) {
        return ((root, query, criteriaBuilder) -> {
            Join<Book, Author> authorJoin = root.join("author", JoinType.INNER);
            return criteriaBuilder.like(
                    criteriaBuilder.lower(
                            criteriaBuilder.concat(criteriaBuilder.concat(
                                    authorJoin.get("firstName"), " "), authorJoin.get("lastName"))
                    ), "%" + authorName.toLowerCase() + "%");
        });
    }
}
