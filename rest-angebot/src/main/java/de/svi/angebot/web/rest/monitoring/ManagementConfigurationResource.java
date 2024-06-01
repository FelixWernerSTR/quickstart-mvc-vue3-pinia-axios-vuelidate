package de.svi.angebot.web.rest.monitoring;

import de.svi.angebot.security.AuthoritiesConstants;
import de.svi.angebot.web.rest.vm.ConfigPropsVM;
import de.svi.angebot.web.rest.vm.EnvVM;
import io.quarkus.runtime.configuration.ProfileManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;

@Path("/management")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ManagementConfigurationResource {

    @GET
    @Path("/configprops")
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ConfigPropsVM getConfigs() {
        return new ConfigPropsVM();
    }

    @GET
    @Path("/env")
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public EnvVM getEnvs() {
        Iterable<ConfigSource> configSources = ConfigProvider.getConfig().getConfigSources();
        List<EnvVM.PropertySource> propertySources = StreamSupport
            .stream(configSources.spliterator(), false)
            .map(configSource -> new EnvVM.PropertySource(configSource.getName(), configSource.getProperties()))
            .collect(Collectors.toList());

        return new EnvVM(List.of(ProfileManager.getActiveProfile()), propertySources);
    }
}
