package gr.athtech.app.bookmanager.security.geoblocking;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@NoArgsConstructor
@Component
public class ClientInfo {
    /**
     * Returns the original client IP address
     * <p>If the request has {@code X-Forwarded-For} header, it will return the first value in the chain</p>
     * Otherwise, the remote address is returned
     * @return Client IP if exist or null
     */
    public String getClientIp() {
        // Get current request bound to this thread
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if(attributes == null) return null;

        HttpServletRequest request = attributes.getRequest();

        String header = request.getHeader("X-Forwarded-For");

        if(header != null && !header.isEmpty()) {
            return header.split(",")[0];
        }

        return request.getRemoteAddr();
    }
}
