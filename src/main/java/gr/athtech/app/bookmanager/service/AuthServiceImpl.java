package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.security.jwt.JwtService;
import gr.athtech.app.bookmanager.transfer.auth.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String authentication(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));

        return jwtService.generateJwtToken(authRequest.email());
    }
}
