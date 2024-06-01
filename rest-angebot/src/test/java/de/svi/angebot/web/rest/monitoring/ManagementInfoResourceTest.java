package de.svi.angebot.web.rest.monitoring;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

import de.svi.angebot.TestUtil;
import de.svi.angebot.config.AngebotProperties;
import de.svi.angebot.service.dto.ManagementInfoDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class ManagementInfoResourceTest {

    @Inject
    AngebotProperties mockAngebotProperties;

    private static final TypeRef<ManagementInfoDTO> MANAGEMENT_INFO_DTO = new TypeRef<>() {};

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @Test
    public void swaggerEnabled() {
        // Prepare test data
        Mockito.when(mockAngebotProperties.info().swagger().enable()).thenReturn(true);

        // Get Management info
        final ManagementInfoDTO info = given()
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .when()
            .get("/management/info")
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .as(MANAGEMENT_INFO_DTO);
        assertThat(info.activeProfiles).contains("swagger");
    }

    @Test
    public void swaggerDisabled() {
        // Prepare test data
        Mockito.when(mockAngebotProperties.info().swagger().enable()).thenReturn(false);

        // Get Management info
        final ManagementInfoDTO info = given()
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .when()
            .get("/management/info")
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .as(MANAGEMENT_INFO_DTO);
        assertThat(info.activeProfiles).doesNotContain("swagger");
    }
}
