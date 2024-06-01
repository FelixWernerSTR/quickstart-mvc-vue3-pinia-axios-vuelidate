package de.svi.angebot.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.svi.angebot.domain.Angebot;
import de.svi.angebot.service.Paged;
import de.svi.angebot.service.AngebotService;
import de.svi.angebot.web.rest.errors.BadRequestAlertException;
import de.svi.angebot.web.rest.vm.PageRequestVM;
import de.svi.angebot.web.rest.vm.SortRequestVM;
import de.svi.angebot.web.util.HeaderUtil;
import de.svi.angebot.web.util.PaginationUtil;
import de.svi.angebot.web.util.ResponseUtil;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link de.svi.angebot.domain.Angebot}.
 */
@Path("/api/angebot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AngebotResource {

    private final Logger log = LoggerFactory.getLogger(AngebotResource.class);

    private static final String ENTITY_NAME = "angebotManagerAngebot";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    AngebotService angebotService;

    /**
     * {@code POST  /angebots} : Create a new angebot.
     *
     * @param angebot the angebot to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new angebot, or with status {@code 400 (Bad Request)} if the angebot has already an ID.
     */
    @POST
    public Response createAngebot(@Valid Angebot angebot, @Context UriInfo uriInfo) {
        log.debug("REST request to save Angebot : {}", angebot);
//        if (angebot.id != null) {
//            throw new BadRequestAlertException("A new angebot cannot already have an ID", ENTITY_NAME, "idexists");
//        }
        var result = angebotService.persistOrUpdate(angebot);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /angebots} : Updates an existing angebot.
     *
     * @param angebot the angebot to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated angebot,
     * or with status {@code 400 (Bad Request)} if the angebot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the angebot couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateAngebot(@Valid Angebot angebot, @PathParam("id") Long id) {
        log.debug("REST request to update Angebot : {}", angebot);
        if (angebot.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = angebotService.persistOrUpdate(angebot);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, angebot.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /angebots/:id} : delete the "id" angebot.
     *
     * @param id the id of the angebot to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAngebot(@PathParam("id") Long id) {
        log.debug("REST request to delete Angebot : {}", id);
        angebotService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /angebots} : get all the angebots.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of angebots in body.
     */
    @GET
    public Response getAllAngebots(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.info("REST request to get a page of Angebots");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<Angebot> result = angebotService.findAll(page, sort);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /angebots/:id} : get the "id" angebot.
     *
     * @param id the id of the angebot to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the angebot, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getAngebot(@PathParam("id") Long id) {
        log.debug("REST request to get Angebot : {}", id);
        Optional<Angebot> angebot = angebotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(angebot);
    }
}
