package de.svi.angebot.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 *  DTO to emulate /management/info response
 */
@RegisterForReflection
public class ManagementInfoDTO {

    public List<String> activeProfiles = new ArrayList<>();

    @JsonbProperty("display-ribbon-on-profiles")
    public String displayRibbonOnProfiles;
}
