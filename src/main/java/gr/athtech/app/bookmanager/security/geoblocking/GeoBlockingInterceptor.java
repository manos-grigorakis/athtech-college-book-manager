package gr.athtech.app.bookmanager.security.geoblocking;

import gr.athtech.app.bookmanager.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class GeoBlockingInterceptor implements HandlerInterceptor {
    private final ClientInfo clientInfo;
    private final GeoService geoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if(!allowedHttpOperations(request)) return true;

        String result = geoService.getCountryByIp(clientInfo.getClientIp());

        if(result != null && result.equals("GR")) {
            return true;
        }

        throw new ForbiddenException("Geographic Restriction");
    }

    /**
     * Defines the allowed HTTP operations for Geo Blocking
     * <p>Allowed Methods:</p>
     * <ul>
     *     <li>{@link HttpMethod#POST}</li>
     *     <li>{@link HttpMethod#DELETE}</li>
     * </ul>
     *
     * @param request The HTTP request
     * @return {@code true} when request method is on the allowed list, otherwise {@code false}
     */
    private boolean allowedHttpOperations(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.POST.name()) || request.getMethod().equals(
                HttpMethod.DELETE.name());
    }
}
