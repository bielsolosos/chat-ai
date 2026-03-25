package br.dev.bielsolosos.aichat.infrastructre.utils;

import br.dev.bielsolosos.aichat.infrastructre.enums.ModelVendorEnum;
import br.dev.bielsolosos.aichat.infrastructre.properties.ChataiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatModelVendor {

    private final Map<String, ChatModel> chatModelByBeanName;
    private final ChataiProperties chataiProperties;

    public ChatModel getChatModel() {
        String beanName = chataiProperties.getModelBean().getValue();
        ChatModel model = chatModelByBeanName.get(beanName);

        if (model == null) {
            throw new IllegalStateException("ChatModel bean not found for configured vendor: " + beanName);
        }

        return model;
    }

    public String getModelName() {
        return chataiProperties.getModelToUse();
    }

    public ModelVendorEnum getModelVendor() {
        return chataiProperties.getModelBean();
    }
}
