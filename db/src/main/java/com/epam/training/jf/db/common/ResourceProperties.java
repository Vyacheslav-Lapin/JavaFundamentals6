package com.epam.training.jf.db.common;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Log4j2
public class ResourceProperties {

    @Getter
    private Properties properties = new Properties();

    public ResourceProperties(String resource) {
        try (InputStream resourceAsStream = getClass().getResourceAsStream(resource)) {
            properties.load(resourceAsStream);
        } catch (IOException ignored) {
            log.warn("Resource is unaccessible!");
        }
    }

    public Optional<String> getAndRemove(String key) {
        return Optional.ofNullable(
                (String) properties.remove(key)
        );
    }

    public boolean containsKeysOnly(String... keys) {
        if (properties.size() != keys.length)
            return false;

        for (String key : keys)
            if (!properties.containsKey(key))
                return false;

        return true;
    }
}
