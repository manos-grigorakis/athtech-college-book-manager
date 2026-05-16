package gr.athtech.app.bookmanager.controller;

import gr.athtech.app.bookmanager.service.BaseService;
import gr.athtech.app.bookmanager.service.BookService;
import gr.athtech.app.bookmanager.transfer.book.*;
import gr.athtech.app.bookmanager.transfer.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/books")
public class BookController extends BaseController<BookResponse, BookRequest> {
    private final BookService bookService;

    @Override
    protected BaseService<BookResponse, Long, BookRequest> getBaseService() {
        return bookService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookResponse>>> search(@ModelAttribute BookSearchRequest searchRequest) {
        return ResponseEntity.ok(
                ApiResponse.<List<BookResponse>>builder().data(bookService.searchBooks(searchRequest)).build());
    }

    @GetMapping("/analytics/category-distribution")
    public ResponseEntity<ApiResponse<List<CategoryDistributionResponse>>> analyticsCategoryDistribution() {
        return ResponseEntity.ok(ApiResponse.<List<CategoryDistributionResponse>>builder().data(
                bookService.findCategoryDistribution()).build());
    }

    @GetMapping("/analytics/author-timeline/{authorId}")
    public ResponseEntity<ApiResponse<List<AuthorTimelineResponse>>> analyticsAuthorProductivity(
            @PathVariable Long authorId) {
        return ResponseEntity.ok(ApiResponse.<List<AuthorTimelineResponse>>builder().data(
                bookService.findAuthorTimeline(authorId)).build());
    }

    @GetMapping("/analytics/geographic-distribution")
    public ResponseEntity<ApiResponse<List<GeographicDistributionResponse>>> analyticsGeographicReach() {
        return ResponseEntity.ok(ApiResponse.<List<GeographicDistributionResponse>>builder().data(
                bookService.findGeographicDistribution()).build());
    }
}
