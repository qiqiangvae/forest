package online.qiqiang.forest.example.mvc.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import online.qiqiang.forest.framework.log.JacksonLogPrinterCustomizer;
import online.qiqiang.forest.mvc.extension.MultipartFileSerializer;
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
