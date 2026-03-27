package br.dev.bielsolosos.aichat.api.controller;

import br.dev.bielsolosos.aichat.domain.model.SpringAiChatMemory;
import br.dev.bielsolosos.aichat.domain.repository.SpringAiChatMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/memory")
@RequiredArgsConstructor
public class SpringAiChatMemoryController {
    private final SpringAiChatMemoryRepository repository;

    @GetMapping
    public List<SpringAiChatMemory> findAll() {
        return repository.findAll();
    }

}
