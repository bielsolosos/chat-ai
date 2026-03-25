package br.dev.bielsolosos.aichat.domain.service;

import br.dev.bielsolosos.aichat.domain.model.History;
import br.dev.bielsolosos.aichat.domain.repository.HistoryRepository;
import br.dev.bielsolosos.aichat.infrastructre.utils.ChatModelVendor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * {@code @Author} Gabriel Coutinho (bielsolosos)
 * @Date 24/03/2026
 * Serviço que irá fazer uma requisição seca para o modelo da aplicação. Sem guardar contexto da conversa. Ou operações mais complexas.
 */
@Service
@RequiredArgsConstructor
public class TestChatService {

    private final ChatModelVendor chatModelVendor;
    private final HistoryRepository historyRepository;

    public String getResponseWithHistory(String prompt) {
        String response = getConfiguredChatModel().call(prompt);
        History history = new History();
        history.setMessage(response);
        history.setModel(chatModelVendor.getModelName());
        history.setModelVendor(chatModelVendor.getModelVendor());
        historyRepository.save(history);
        return response;
    }

    public String getResponseWithoutHistory(String prompt) {
        return getConfiguredChatModel().call(prompt);
    }

    /**
     * Pega o Modelo Default da aplicação de acordo com o application Properties.
     * @return
     */
    private ChatModel getConfiguredChatModel() {
        return chatModelVendor.getChatModel();
    }
}
