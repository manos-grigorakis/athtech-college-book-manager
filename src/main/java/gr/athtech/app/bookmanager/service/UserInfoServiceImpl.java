package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.model.UserInfo;
import gr.athtech.app.bookmanager.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByEmail(username).orElseThrow(() -> {
            log.warn("Email {} not found", username);
            return new UsernameNotFoundException("User with email " + username + " not found");
        });

        return new User(userInfo.getEmail(), userInfo.getPassword(), List.of());
    }
}
