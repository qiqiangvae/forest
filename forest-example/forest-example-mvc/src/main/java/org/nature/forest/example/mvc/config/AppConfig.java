package org.nature.forest.example.mvc.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.nature.forest.framework.log.JacksonLogPrinterCustomizer;
import org.nature.forest.mvc.extension.MultipartFileSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author qiqiang
 */
@Configuration
public class AppConfig {
    @Bean
    public JacksonLogPrinterCustomizer jacksonLogPrinterCustomizer() {
        JacksonLogPrinterCustomizer customizer = new JacksonLogPrinterCustomizer();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new MultipartFileSerializer(MultipartFile.class));
        customizer.addModel(module);
        return customizer;
    }
}
