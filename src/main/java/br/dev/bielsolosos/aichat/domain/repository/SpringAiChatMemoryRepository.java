package br.dev.bielsolosos.aichat.domain.repository;

import br.dev.bielsolosos.aichat.domain.model.SpringAiChatMemory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringAiChatMemoryRepository extends JpaRepository<SpringAiChatMemory, Long> {
}
