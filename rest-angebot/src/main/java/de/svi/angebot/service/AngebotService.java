package de.svi.angebot.service;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.svi.angebot.domain.Angebot;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
@Transactional
public class AngebotService {

    private final Logger log = LoggerFactory.getLogger(AngebotService.class);

    @Transactional
    public Angebot persistOrUpdate(Angebot angebot) {
        log.debug("Request to save Angebot : {}", angebot);
        return Angebot.persistOrUpdate(angebot);
    }

    /**
     * Delete the Angebot by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Angebot : {}", id);
        Angebot
            .findByIdOptional(id)
            .ifPresent(angebot -> {
                angebot.delete();
            });
    }

    /**
     * Get one angebot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Angebot> findOne(Long id) {
        log.debug("Request to get Angebot : {}", id);
        return Angebot.findByIdOptional(id);
    }

    /**
     * Get all the angebots.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<Angebot> findAll(Page page, Sort sort) {
        log.debug("Request to get all Angebots");
        return new Paged<>(Angebot.findAll(sort).page(page));
    }
}
