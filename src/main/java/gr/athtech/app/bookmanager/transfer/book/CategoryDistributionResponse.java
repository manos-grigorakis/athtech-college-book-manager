package gr.athtech.app.bookmanager.transfer.book;

public record CategoryDistributionResponse (
        String category,
        Long booksCount,
        Double averageNumberOfPages
) {}
