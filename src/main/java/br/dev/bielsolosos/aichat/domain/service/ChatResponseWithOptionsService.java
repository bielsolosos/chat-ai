package br.dev.bielsolosos.aichat.domain.service;

import br.dev.bielsolosos.aichat.domain.model.History;
import br.dev.bielsolosos.aichat.domain.repository.HistoryRepository;
import br.dev.bielsolosos.aichat.infrastructre.enums.LlmModelEnum;
import br.dev.bielsolosos.aichat.infrastructre.enums.ModelVendorEnum;
import br.dev.bielsolosos.aichat.infrastructre.utils.ChatModelVendor;
import br.dev.bielsolosos.aichat.shared.RequestPrompt;
import br.dev.bielsolosos.aichat.shared.RequestPromptOptions;
import br.dev.bielsolosos.aichat.shared.ResponseFromLlm;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatResponseWithOptionsService {

    private final HistoryRepository historyRepository;
    private final ChatModelVendor chatModelVendor;
    private final PromptChatMemoryAdvisor promptChatMemoryAdvisor;

    public ResponseFromLlm getResponseWithOptions(RequestPromptOptions request) {

        if(!chatModelVendor.getModelVendor().equals(request.model().getVendor())) {
            throw new IllegalArgumentException("Wrong model vendor");
        }

        ChatModel chatModel = getChatModel(request.model().getVendor());

        ChatOptions options = ChatOptions.builder().model(request.model().getModel()).temperature(request.temperature()).build();

        ChatResponse response = chatModel.call(new Prompt(request.prompt(), options));

        registerInHistory(response, request.model().getModel(), request.model().getVendor());

        return new ResponseFromLlm(response.getResult().getOutput().getText());
    }

    /**
     * Faz a request padrão já seta a temperatura com 0.3, define o máximo de tokens para 250 e define o modelo
     * GEMMA_3_27B
     */
    public ResponseFromLlm getResponseWithDefaultOptions(RequestPrompt request) {
        ChatModel chatModel = getChatModel(LlmModelEnum.GEMMA_3_27B.getVendor());

        ChatOptions options = ChatOptions.builder()
                .model(LlmModelEnum.GEMMA_3_27B.getModel())
                .temperature(0.3)
                .maxTokens(250)
                .build();

        ChatResponse response = chatModel.call(new Prompt(request.prompt(), options));

        registerInHistory(response, LlmModelEnum.GEMMA_3_27B.getModel(), ModelVendorEnum.GEMINI);

        return new ResponseFromLlm(response.getResult().getOutput().getText());
    }

    /**
     * Faz a request padrão já seta a temperatura com 0.3, define o máximo de tokens para 250 e define o modelo
     * GEMINI_2_5_FLASH_LITE
     */
    public ResponseFromLlm getResponseWithDefaultOptionsWithMemory(RequestPrompt request) {
        ChatModel chatModel = getChatModel(LlmModelEnum.GEMINI_2_5_FLASH_LITE.getVendor());

        ChatOptions options = ChatOptions.builder().model(LlmModelEnum.GEMINI_2_5_FLASH_LITE.getModel()).temperature(0.3).maxTokens(250).build();

        ChatResponse response = ChatClient.builder(chatModel)
                .defaultAdvisors(promptChatMemoryAdvisor)
                .build()
                .prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "Chat-teste"))
                .options(options)
                .user(request.prompt())
                .call().chatResponse();

        registerInHistory(response, LlmModelEnum.GEMINI_2_5_FLASH_LITE.getModel(), ModelVendorEnum.GEMINI);

        return new ResponseFromLlm(response.getResult().getOutput().getText());
    }

    private void registerInHistory(ChatResponse response, String model, ModelVendorEnum vendor) {
        historyRepository.save(History.builder()
                .message(response.getResult().getOutput().getText())
                .model(model)
                .modelVendor(vendor)
                .build());
    }

    private ChatModel getChatModel(ModelVendorEnum vendor) {
        if (vendor == null) {
            return chatModelVendor.getChatModel();
        }
        return chatModelVendor.getChatModel(vendor);
    }
}
