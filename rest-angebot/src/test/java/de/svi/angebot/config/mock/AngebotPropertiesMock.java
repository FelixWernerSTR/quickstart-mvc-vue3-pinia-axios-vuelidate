package de.svi.angebot.config.mock;

import de.svi.angebot.config.AngebotProperties;
import io.quarkus.test.Mock;
import io.smallrye.config.SmallRyeConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.mockito.Mockito;

@ApplicationScoped
public class AngebotPropertiesMock {

    @Inject
    Config config;

    @Produces
    @ApplicationScoped
    @Mock
    AngebotProperties properties() {
    	AngebotProperties angebotProperties = config.unwrap(SmallRyeConfig.class).getConfigMapping(AngebotProperties.class);
    	AngebotProperties spyAngebotProperties = Mockito.spy(angebotProperties);

    	AngebotProperties.Info spyInfo = Mockito.spy(angebotProperties.info());
    	AngebotProperties.Info.Swagger spySwagger = Mockito.spy(angebotProperties.info().swagger());
        Mockito.when(spyAngebotProperties.info()).thenReturn(spyInfo);
        Mockito.when(spyAngebotProperties.info().swagger()).thenReturn(spySwagger);

        return spyAngebotProperties;
    }
}
