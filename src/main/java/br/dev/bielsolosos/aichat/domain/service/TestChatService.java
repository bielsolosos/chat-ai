package br.dev.bielsolosos.aichat.domain.service;

import br.dev.bielsolosos.aichat.domain.model.History;
import br.dev.bielsolosos.aichat.domain.repository.HistoryRepository;
import br.dev.bielsolosos.aichat.infrastructre.utils.ChatModelVendor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestChatService {

    private final ChatModelVendor chatModelVendor;
    private final HistoryRepository historyRepository;

    public String getResponse(String prompt) {
        String response = getConfiguredChatModel().call(prompt);
        History history = new History();
        history.setMessage(response);
        historyRepository.save(history);
        return response;
    }

    public ChatModel getConfiguredChatModel() {
        return chatModelVendor.getChatModel();
    }
}
