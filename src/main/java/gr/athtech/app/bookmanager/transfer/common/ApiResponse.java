package gr.athtech.app.bookmanager.transfer.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.Date;
import java.util.UUID;

@Value
@ToString
@Builder
public class ApiResponse<T> {
    String transactionId = UUID.randomUUID().toString().toUpperCase();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS")
    Date createdAt = new Date();

    T data;

    ErrorResponse error;
}
