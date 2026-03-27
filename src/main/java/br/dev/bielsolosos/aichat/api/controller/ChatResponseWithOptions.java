package br.dev.bielsolosos.aichat.api.controller;

import br.dev.bielsolosos.aichat.domain.service.ChatResponseWithOptionsService;
import br.dev.bielsolosos.aichat.shared.RequestPrompt;
import br.dev.bielsolosos.aichat.shared.RequestPromptOptions;
import br.dev.bielsolosos.aichat.shared.ResponseFromLlm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class ChatResponseWithOptions {

    private final ChatResponseWithOptionsService service;

    @PostMapping("/with-options")
    public ResponseEntity<ResponseFromLlm> getResponseWithOptions(@RequestBody RequestPromptOptions request) {
        return ResponseEntity.ok(service.getResponseWithOptions(request));
    }


    @PostMapping("/with-options-default")
    public ResponseEntity<ResponseFromLlm> getResponseWithOptions(@RequestBody RequestPrompt request) {
        return ResponseEntity.ok(service.getResponseWithDefaultOptions(request));
    }

    @PostMapping("/with-options-default-memory")
    public ResponseEntity<ResponseFromLlm> getResponseWithOptionsWithMemory(@RequestBody RequestPrompt request) {
        return ResponseEntity.ok(service.getResponseWithDefaultOptionsWithMemory(request));
    }
}
