package de.svi.angebot.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.svi.angebot.TestUtil;
import de.svi.angebot.domain.Angebot;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import jakarta.inject.Inject;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class AngebotResourceIT {

    private static final TypeRef<Angebot> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<Angebot>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VORNAME = "AAAAAAAAAA";
    private static final String UPDATED_VORNAME = "BBBBBBBBBB";

    private static final Date DEFAULT_GEBURTS_DATUM = new Date();
    private static final Date UPDATED_GEBURTS_DATUM = new Date();

    private static final String DEFAULT_STRASSE = "AAAAAAAAAA";
    private static final String UPDATED_STRASSE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HAUS_NUMMER = 0;
    private static final Integer UPDATED_HAUS_NUMMER = 1;

    private static final Integer DEFAULT_PLZ = 0;
    private static final Integer UPDATED_PLZ = 1;

    private static final String DEFAULT_LAND = "AAAAAAAAAA";
    private static final String UPDATED_LAND = "BBBBBBBBBB";

    String adminToken;

    Angebot angebot;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        //this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Angebot createEntity() {
        var angebot = new Angebot();
        angebot.description = DEFAULT_DESCRIPTION;
        angebot.name = DEFAULT_NAME;
        angebot.vorname = DEFAULT_VORNAME;
        angebot.geburtsDatum = DEFAULT_GEBURTS_DATUM;
        angebot.strasse = DEFAULT_STRASSE;
        angebot.hausNummer = DEFAULT_HAUS_NUMMER;
        angebot.plz = DEFAULT_PLZ;
        angebot.land = DEFAULT_LAND;
        return angebot;
    }

    @BeforeEach
    public void initTest() {
        angebot = createEntity();
    }

    @Test
    public void createAngebot() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Angebot
        angebot =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(angebot)
                .when()
                .post("/api/angebots")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Validate the Angebot in the database
        var angebotList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(angebotList).hasSize(databaseSizeBeforeCreate + 1);
        var testAngebot = angebotList.stream().filter(it -> angebot.id.equals(it.id)).findFirst().get();
        assertThat(testAngebot.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAngebot.name).isEqualTo(DEFAULT_NAME);
        assertThat(testAngebot.vorname).isEqualTo(DEFAULT_VORNAME);
        assertThat(testAngebot.geburtsDatum).isEqualTo(DEFAULT_GEBURTS_DATUM);
        assertThat(testAngebot.strasse).isEqualTo(DEFAULT_STRASSE);
        assertThat(testAngebot.hausNummer).isEqualTo(DEFAULT_HAUS_NUMMER);
        assertThat(testAngebot.plz).isEqualTo(DEFAULT_PLZ);
        assertThat(testAngebot.land).isEqualTo(DEFAULT_LAND);
    }

    @Test
    public void createAngebotWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Angebot with an existing ID
        angebot.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(angebot)
            .when()
            .post("/api/angebots")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Angebot in the database
        var angebotList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(angebotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void updateAngebot() {
        // Initialize the database
        angebot =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(angebot)
                .when()
                .post("/api/angebots")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the angebot
        var updatedAngebot = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots/{id}", angebot.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the angebot
        updatedAngebot.description = UPDATED_DESCRIPTION;
        updatedAngebot.name = UPDATED_NAME;
        updatedAngebot.vorname = UPDATED_VORNAME;
        updatedAngebot.geburtsDatum = UPDATED_GEBURTS_DATUM;
        updatedAngebot.strasse = UPDATED_STRASSE;
        updatedAngebot.hausNummer = UPDATED_HAUS_NUMMER;
        updatedAngebot.plz = UPDATED_PLZ;
        updatedAngebot.land = UPDATED_LAND;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedAngebot)
            .when()
            .put("/api/angebots/" + angebot.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Angebot in the database
        var angebotList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(angebotList).hasSize(databaseSizeBeforeUpdate);
        var testAngebot = angebotList.stream().filter(it -> updatedAngebot.id.equals(it.id)).findFirst().get();
        assertThat(testAngebot.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAngebot.name).isEqualTo(UPDATED_NAME);
        assertThat(testAngebot.vorname).isEqualTo(UPDATED_VORNAME);
        assertThat(testAngebot.geburtsDatum).isEqualTo(UPDATED_GEBURTS_DATUM);
        assertThat(testAngebot.strasse).isEqualTo(UPDATED_STRASSE);
        assertThat(testAngebot.hausNummer).isEqualTo(UPDATED_HAUS_NUMMER);
        assertThat(testAngebot.plz).isEqualTo(UPDATED_PLZ);
        assertThat(testAngebot.land).isEqualTo(UPDATED_LAND);
    }

    @Test
    public void updateNonExistingAngebot() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(angebot)
            .when()
            .put("/api/angebots/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Angebot in the database
        var angebotList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(angebotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAngebot() {
        // Initialize the database
        angebot =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(angebot)
                .when()
                .post("/api/angebots")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the angebot
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/angebots/{id}", angebot.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var angebotList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(angebotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllAngebots() {
        // Initialize the database
        angebot =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(angebot)
                .when()
                .post("/api/angebots")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Get all the angebotList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(angebot.id.intValue()))
            .body("description", hasItem(DEFAULT_DESCRIPTION.toString()))
            .body("name", hasItem(DEFAULT_NAME))
            .body("vorname", hasItem(DEFAULT_VORNAME))
            //.body("geburtsDatum", hasItem(TestUtil.formatDateTime(DEFAULT_GEBURTS_DATUM)))
            .body("strasse", hasItem(DEFAULT_STRASSE))
            .body("hausNummer", hasItem(DEFAULT_HAUS_NUMMER.intValue()))
            .body("plz", hasItem(DEFAULT_PLZ.intValue()))
            .body("land", hasItem(DEFAULT_LAND));
    }

    @Test
    public void getAngebot() {
        // Initialize the database
        angebot =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(angebot)
                .when()
                .post("/api/angebots")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var response = given() // Get the angebot
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots/{id}", angebot.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(ENTITY_TYPE);

        // Get the angebot
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots/{id}", angebot.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(angebot.id.intValue()))
            .body("description", is(DEFAULT_DESCRIPTION.toString()))
            .body("name", is(DEFAULT_NAME))
            .body("vorname", is(DEFAULT_VORNAME))
            //.body("geburtsDatum", is(TestUtil.formatDateTime(DEFAULT_GEBURTS_DATUM)))
            .body("strasse", is(DEFAULT_STRASSE))
            .body("hausNummer", is(DEFAULT_HAUS_NUMMER.intValue()))
            .body("plz", is(DEFAULT_PLZ.intValue()))
            .body("land", is(DEFAULT_LAND));
    }

    @Test
    public void getNonExistingAngebot() {
        // Get the angebot
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/angebots/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
