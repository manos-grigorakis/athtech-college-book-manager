package gr.athtech.app.bookmanager.security.config;

import gr.athtech.app.bookmanager.security.geoblocking.GeoBlockingInterceptor;
import gr.athtech.app.bookmanager.security.ratelimit.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final RateLimitInterceptor rateLimitInterceptor;
    private final GeoBlockingInterceptor geoBlockingInterceptor;

    /**
     * Register Interceptors to Spring MVC
     * @param registry Used to register to Spring MVC
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Rate Limit
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login");

        // Geo Blocking
        registry.addInterceptor(geoBlockingInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login");
    }
}
