package br.dev.bielsolosos.aichat.api.controller;

import br.dev.bielsolosos.aichat.domain.service.TestChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestChatController {

    private final TestChatService testChatService;

    public record ResponseFromLlm(String response){}
    public record RequestPrompt(String prompt){}

    @PostMapping
    public ResponseEntity<ResponseFromLlm> getResponse(@RequestBody RequestPrompt request) {
        return ResponseEntity.ok(new ResponseFromLlm(testChatService.getResponse(request.prompt())));
    }

}

