package br.dev.bielsolosos.aichat.infrastructre.properties;

import br.dev.bielsolosos.aichat.infrastructre.enums.ModelVendorEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "spring.chat-ai")
public class ChataiProperties {

    private String modelToUse;

    private int maxAiMessages = 10;

    public ModelVendorEnum getModelBean() {
        return ModelVendorEnum.fromPropertyValue(this.modelToUse);
    }
}
