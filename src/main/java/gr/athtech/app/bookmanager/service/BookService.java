package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.transfer.book.*;

import java.util.List;

public interface BookService extends BaseService<BookResponse, Long, BookRequest> {
    List<BookResponse> searchBooks(BookSearchRequest searchRequest);

    List<CategoryDistributionResponse> findCategoryDistribution();

    List<AuthorTimelineResponse> findAuthorTimeline(Long authorId);

    List<GeographicDistributionResponse> findGeographicDistribution();
}
