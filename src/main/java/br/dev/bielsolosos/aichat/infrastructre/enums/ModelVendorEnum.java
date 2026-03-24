package br.dev.bielsolosos.aichat.infrastructre.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum ModelVendorEnum {
    OLLAMA("ollamaChatModel"),
    GEMINI("googleGenAiChatModel");

    private final String value;

    public static ModelVendorEnum fromPropertyValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Property spring.chat-ai.model-to-use cannot be empty.");
        }

        return switch (value.trim().toUpperCase(Locale.ROOT)) {
            case "OLLAMA" -> OLLAMA;
            case "GEMINI", "GOOGLE", "GOOGLE_GENAI", "GOOGLEGENAI" -> GEMINI;
            default -> throw new IllegalArgumentException("Unsupported model vendor: " + value);
        };
    }
}
