package gr.athtech.app.bookmanager.service;

import gr.athtech.app.bookmanager.transfer.auth.AuthRequest;

public interface AuthService {
    String authentication(AuthRequest authRequest);
}
