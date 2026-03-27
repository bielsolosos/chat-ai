package br.dev.bielsolosos.aichat.infrastructre.config;

import br.dev.bielsolosos.aichat.infrastructre.properties.ChataiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Classe responsável por configurar a memória da IA.
 */
@Component
@RequiredArgsConstructor
public class PromptChatMemoryAdvisorConfig {

    private final ChataiProperties properties;

    @Bean
    PromptChatMemoryAdvisor promptChatMemoryAdvisor(DataSource dataSource) {
        //Configura a camada de persistência
        var jdbc = JdbcChatMemoryRepository
                .builder()
                .dataSource(dataSource)
                .build();

        //Gerenciador de janelas, ele decide quantas mensagens serão passadas e enviadas para o modelo a cada pergunta
        var chatMessageWindow = MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(jdbc)
                .maxMessages(properties.getMaxAiMessages())
                .build();

        //Retorna o interceptor para a request para a IA.
        return PromptChatMemoryAdvisor
                .builder(chatMessageWindow)
                .build();
    }
}
