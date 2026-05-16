package gr.athtech.app.bookmanager.security.geoblocking;

import gr.athtech.app.bookmanager.exception.ExternalClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class GeoService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "countryIp", key = "#ip")
    public String getCountryByIp(String ip) {
        try {
            String uri = "https://ipapi.co/" + ip + "/country";
            return restTemplate.getForObject(uri, String.class);
        } catch (Exception e) {
            log.warn("Error from geo client request: {}", e.getMessage());
            throw new ExternalClientException("Geo client unavailable");
        }
    }
}
