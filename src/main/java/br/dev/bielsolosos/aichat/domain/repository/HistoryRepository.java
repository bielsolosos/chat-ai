package br.dev.bielsolosos.aichat.domain.repository;

import br.dev.bielsolosos.aichat.domain.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}

