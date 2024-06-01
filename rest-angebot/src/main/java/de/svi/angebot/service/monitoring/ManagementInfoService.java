package de.svi.angebot.service.monitoring;

import de.svi.angebot.config.AngebotProperties;
import de.svi.angebot.service.dto.ManagementInfoDTO;
import io.quarkus.runtime.configuration.ProfileManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Provides information for management/info resource
 */
@ApplicationScoped
public class ManagementInfoService {

    private final AngebotProperties angebotProperties;

    @Inject
    public ManagementInfoService(AngebotProperties angebotProperties) {
        this.angebotProperties = angebotProperties;
    }

    public ManagementInfoDTO getManagementInfo() {
        var info = new ManagementInfoDTO();
        if (angebotProperties.info().swagger().enable()) {
            info.activeProfiles.add("swagger");
        }
        info.activeProfiles.add(ProfileManager.getActiveProfile());
        info.displayRibbonOnProfiles = ProfileManager.getActiveProfile();
        return info;
    }
}
