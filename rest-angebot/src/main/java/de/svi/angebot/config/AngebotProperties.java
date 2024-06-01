package de.svi.angebot.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "angebot")
public interface AngebotProperties {
    Info info();

    interface Info {
        Swagger swagger();

        interface Swagger {
            Boolean enable();
        }
    }
}
