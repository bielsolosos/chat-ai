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

    public ResponseFromLlm getResponseWithOptions(RequestPromptOptions request) {

        if(!chatModelVendor.getModelVendor().equals(request.model().getVendor())) {
            throw new IllegalArgumentException("Wrong model vendor");
        }

        ChatModel chatModel = getChatModel();

        ChatOptions options = ChatOptions.builder().model(request.model().getModel()).temperature(request.temperature()).build();

        ChatResponse response = chatModel.call(new Prompt(request.prompt(), options));

        registerInHistory(response, request.model().getModel(), request.model().getVendor());

        return new ResponseFromLlm(response.getResult().getOutput().getText());
    }

    public ResponseFromLlm getResponseWithDefaultOptions(RequestPrompt request) {
        ChatModel chatModel = getChatModel();

        ChatOptions options = ChatOptions.builder()
                .model(LlmModelEnum.GEMMA_3_27B.getModel())
                .temperature(0.1)
                .maxTokens(250)
                .build();

        ChatResponse response = chatModel.call(new Prompt(request.prompt(), options));

        registerInHistory(response, LlmModelEnum.GEMMA_3_27B.getModel(), ModelVendorEnum.GEMINI);

        return new ResponseFromLlm(response.getResult().getOutput().getText());
    }

    private void registerInHistory(ChatResponse response, String model, ModelVendorEnum vendor) {
        historyRepository.save(History.builder()
                .message(response.getResult().getOutput().getText())
                .model(model)
                .modelVendor(vendor)
                .build());
    }

    private ChatModel getChatModel() {
        return chatModelVendor.getChatModel();
    }
}
