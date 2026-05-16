package gr.athtech.app.bookmanager.repository;

import gr.athtech.app.bookmanager.model.Book;
import gr.athtech.app.bookmanager.transfer.book.AuthorTimelineResponse;
import gr.athtech.app.bookmanager.transfer.book.CategoryDistributionResponse;
import gr.athtech.app.bookmanager.transfer.book.GeographicDistributionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    // Solves: N + 1 database queries problem from lazy loading by fetching all books with authors with one query
    @Query("SELECT b FROM Book AS b JOIN FETCH  b.author")
    List<Book> findAllBooksWithAuthors();

    boolean existsByAuthorId(Long id);

    @Query("""
        SELECT b.category, COUNT(b), AVG(b.numberOfPages) 
        FROM Book AS b 
        GROUP BY b.category 
        ORDER BY COUNT(b) DESC
    """)
    List<CategoryDistributionResponse> findCategoryDistribution();

    @Query("""
        SELECT YEAR(b.publishDate), COUNT(b)
        FROM Book AS b
        WHERE b.author.id = :authorId
        GROUP BY YEAR(b.publishDate)
        ORDER BY YEAR(b.publishDate)
    """)
    List<AuthorTimelineResponse> findAuthorTimeline(@Param("authorId") Long authorId);

    @Query("""
        SELECT a.country, COUNT(DISTINCT a.id), COUNT(b)
        FROM Author AS a
        JOIN Book AS b ON b.author.id = a.id
        GROUP BY a.country
   """)
    List<GeographicDistributionResponse> findGeographicDistribution();

}
