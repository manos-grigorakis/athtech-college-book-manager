package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.model.UserInfo;
import gr.athtech.app.bookmanager.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserInfoServiceTest {
    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    private final String EMAIL = "john@example.com";

    @Test
    public void loadUserByUsername_shouldLoadUserByUsername() {
        // Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(EMAIL);
        userInfo.setPassword("password");
        when(userInfoRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userInfo));

        // Act
        UserDetails response = userInfoService.loadUserByUsername(EMAIL);

        // Assert
        assertEquals(EMAIL,  response.getUsername());
    }

    @Test
    public void loadUserByEmail_shouldThrowUsernameNotFoundException_whenUsernameNotFound() {
        // Arrange
        when(userInfoRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(UsernameNotFoundException.class, () -> userInfoService.loadUserByUsername(EMAIL));
    }
}
