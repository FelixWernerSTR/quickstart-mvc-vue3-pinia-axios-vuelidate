package de.svi.angebot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.svi.angebot.domain.Angebot;

@Singleton
public class AngebotPseudoPersistenceService {

	private static final  Logger log = LoggerFactory.getLogger(AngebotPseudoPersistenceService.class);
	
	private static long idCount = 1000;
	
	private static long incrementId() {
		return idCount++;
	}
	
	private static Map<Long, Angebot> angebotMap = new ConcurrentHashMap<>();

	public Angebot persistOrUpdate(Angebot angebot) {
		log.debug("Request to save Angebot : {}", angebot);
			if (angebot.getId() != null) {
				if (!angebotMap.containsKey(angebot.getId())) {
					throw new RuntimeException("no angebot with id: " + angebot.id);
				}
				angebotMap.replace(angebot.getId(), angebot);
			} else {
				angebot.setId(incrementId());
				angebotMap.put(angebot.getId(), angebot);
			}
		return angebot;
	}

	/**
	 * Delete the Angebot by id.
	 *
	 * @param id the id of the entity.
	 */
	public Angebot delete(Long id) {
		log.debug("Request to delete Angebot : {}", id);
		Angebot deleted = angebotMap.get(id);
		angebotMap.remove(id);
		return deleted;
	}

	/**
	 * Get one Angebot by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	public Optional<Angebot> findOne(Long id) {
		log.debug("Request to get Angebot : {}", id);
        if(angebotMap.containsKey(id)) {
        	return Optional.of(angebotMap.get(id));
        }
        return Optional.empty();
	}


	/**
	 * Get all the angebots.
	 * 
	 * @param page the pagination information.
	 * @return the list of entities.
	 */
	public List<Angebot> findAll() {
		log.debug("Request to get all Angebots");
		return new ArrayList(angebotMap.values());
	}

}
