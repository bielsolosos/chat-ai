package br.dev.bielsolosos.aichat.api.controller;

import br.dev.bielsolosos.aichat.domain.service.TestChatService;
import br.dev.bielsolosos.aichat.shared.RequestPrompt;
import br.dev.bielsolosos.aichat.shared.ResponseFromLlm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test-message")
@RequiredArgsConstructor
public class TestChatController {

    private final TestChatService testChatService;

    @PostMapping
    public ResponseEntity<ResponseFromLlm> getResponse(@RequestBody RequestPrompt request, @RequestParam(required = false) boolean withoutHistory) {

        if (withoutHistory) {
            return ResponseEntity.ok(new ResponseFromLlm(testChatService.getResponseWithoutHistory(request.prompt())));
        }

        return ResponseEntity.ok(new ResponseFromLlm(testChatService.getResponseWithHistory(request.prompt())));
    }

}

