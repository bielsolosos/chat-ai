package br.dev.bielsolosos.aichat.shared;

import br.dev.bielsolosos.aichat.infrastructre.enums.LlmModelEnum;

public record RequestPromptOptions(String prompt, LlmModelEnum model, Double temperature) {
}
