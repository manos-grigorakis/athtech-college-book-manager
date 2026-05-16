package gr.athtech.app.bookmanager.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final Bandwidth BANDWIDTH = Bandwidth.builder().
            capacity(20).refillIntervally(20, Duration.ofSeconds(2)).build();

    /**
     * Resolves rate limiting for the given user's email
     * <p>If a Bucket already exists in {@link #cache}, it's returned</p>
     * Otherwise a new Bucket is created using the {@code email} as a key
     * @param email The email of the user used as identification in the {@link #cache}
     * @return The existing bucket or the new bucket for the given email
     */
    public Bucket resolveBucket(String email) {
        return cache.computeIfAbsent(email, this::newBucket);
    }

    /**
     * Creates a new Bucket with limit {@link #BANDWIDTH}
     * @param email The email to be used as a key
     * @return The created Bucket with the limit applied
     */
    private Bucket newBucket(String email) {
        return Bucket.builder().addLimit(BANDWIDTH).build();
    }
}
