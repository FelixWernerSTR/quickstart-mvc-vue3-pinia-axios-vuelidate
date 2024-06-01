package de.svi.angebot.config;

import io.quarkus.jsonb.JsonbConfigCustomizer;
import java.util.Locale;
import jakarta.inject.Singleton;
import jakarta.json.bind.JsonbConfig;

/**
 * Jsonb Configuration
 * Further details https://quarkus.io/guides/rest-json#configuring-json-support
 */
@Singleton
public class JsonbConfiguration implements JsonbConfigCustomizer {

    @Override
    public void customize(JsonbConfig config) {
        config.withDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault());
    }
}
