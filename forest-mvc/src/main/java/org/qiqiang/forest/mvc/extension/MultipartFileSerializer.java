package org.nature.forest.mvc.extension;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author qiqiang
 */
public class MultipartFileSerializer extends StdSerializer<MultipartFile> {
    protected MultipartFileSerializer(Class<MultipartFile> t) {
        super(t);
    }

    @Override
    public void serialize(MultipartFile value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(String.format("文件名【%s】,大小【%d】", value.getOriginalFilename(), value.getSize()));
        }
    }
}
