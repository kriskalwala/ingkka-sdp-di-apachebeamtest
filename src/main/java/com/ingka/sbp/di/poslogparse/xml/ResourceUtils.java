package com.ingka.sbp.di.poslogparse.xml;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ResourceUtils {
    public InputStream resourceToInputStream(String resourcePath) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(resourcePath);
    }

    public byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return content.getBytes(StandardCharsets.UTF_8);
    }
}
