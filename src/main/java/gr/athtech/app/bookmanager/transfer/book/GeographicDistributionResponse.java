package gr.athtech.app.bookmanager.transfer.book;

public record GeographicDistributionResponse(
        String country,
        Long authorsCount,
        Long booksCount
) {}
