package br.dev.bielsolosos.aichat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
				+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
				+ "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
				+ "org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatAutoConfiguration,"
				+ "org.springframework.ai.model.ollama.autoconfigure.chat.OllamaChatAutoConfiguration,"
				+ "org.springframework.ai.model.ollama.autoconfigure.embedding.OllamaEmbeddingAutoConfiguration"
})
class AichatApplicationTests {

	@Test
	void contextLoads() {
	}

}
