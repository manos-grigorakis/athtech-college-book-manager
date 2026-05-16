package gr.athtech.app.bookmanager;

import gr.athtech.app.bookmanager.security.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SpringBootTest
class BookManagerApplicationTests {
    @Test
    void contextLoads() {
    }

}
