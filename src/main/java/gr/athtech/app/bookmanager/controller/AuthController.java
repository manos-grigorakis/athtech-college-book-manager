package gr.athtech.app.bookmanager.controller;

import gr.athtech.app.bookmanager.service.AuthService;
import gr.athtech.app.bookmanager.transfer.auth.AuthRequest;
import gr.athtech.app.bookmanager.transfer.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> authentication(@RequestBody @Valid AuthRequest authRequest) {
        String token = authService.authentication(authRequest);
        return ApiResponse.<Map<String, String>>builder().data(Map.of("token", token)).build();
    }
}
