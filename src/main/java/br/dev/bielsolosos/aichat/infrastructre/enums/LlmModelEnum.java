package br.dev.bielsolosos.aichat.infrastructre.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LlmModelEnum {

    GEMINI_1_5_PRO("gemini-1.5-pro", ModelVendorEnum.GEMINI),
    GEMINI_2_5_FLASH("gemini-2.5-flash", ModelVendorEnum.GEMINI),
    GEMMA_3_27B("gemma-3-27b-it", ModelVendorEnum.GEMINI),
    LLAMA_3_1_8B("llama3.1:8b", ModelVendorEnum.OLLAMA),
    LLAMA_3_2_3B("llama3.2:3b", ModelVendorEnum.OLLAMA);

    private final String model;
    private final ModelVendorEnum vendor;
}