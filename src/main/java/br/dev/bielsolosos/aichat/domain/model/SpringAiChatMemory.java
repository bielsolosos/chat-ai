package br.dev.bielsolosos.aichat.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "spring_ai_chat_memory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpringAiChatMemory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id")
    private String conversationId;

    private String content;

    private String type;

    private LocalDateTime timestamp;
}