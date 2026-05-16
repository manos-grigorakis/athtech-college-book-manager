package gr.athtech.app.bookmanager.controller;

import gr.athtech.app.bookmanager.service.AuthorService;
import gr.athtech.app.bookmanager.service.BaseService;
import gr.athtech.app.bookmanager.transfer.author.AuthorRequest;
import gr.athtech.app.bookmanager.transfer.author.AuthorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/authors")
public class AuthorController extends BaseController<AuthorResponse, AuthorRequest> {
    private final AuthorService authorService;

    @Override
    protected BaseService<AuthorResponse, Long, AuthorRequest> getBaseService() {
        return authorService;
    }
}
