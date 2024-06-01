package de.svi.angebot.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.util.Date;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "angebot")
@RegisterForReflection
public class Angebot extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Lob
    @Column(name = "description")
    public String description;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    public String name;

    @Size(max = 50)
    @Column(name = "vorname", length = 50)
    public String vorname;

    @Column(name = "geburts_datum")
    public Date geburtsDatum;

    @Size(max = 50)
    @Column(name = "strasse", length = 50)
    public String strasse;

    @Min(value = 0)
    @Max(value = 100000)
    @Column(name = "haus_nummer")
    public Integer hausNummer;

    @Min(value = 0)
    @Max(value = 100000)
    @Column(name = "plz")
    public Integer plz;

    @Size(max = 50)
    @Column(name = "land", length = 50)
    public String land;

//    @ManyToOne
//    @JoinColumn(name = "angebot_type_id")
//    @JsonbTransient
//    public AngebotType angebotType;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Angebot)) {
            return false;
        }
        return id != null && id.equals(((Angebot) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Angebot{" +
            "id=" +
            id +
            ", description='" +
            description +
            "'" +
            ", name='" +
            name +
            "'" +
            ", vorname='" +
            vorname +
            "'" +
            ", geburtsDatum='" +
            geburtsDatum +
            "'" +
            ", strasse='" +
            strasse +
            "'" +
            ", hausNummer=" +
            hausNummer +
            ", plz=" +
            plz +
            ", land='" +
            land +
            "'" +
            "}"
        );
    }

    public Angebot update() {
        return update(this);
    }

    public Angebot persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Angebot update(Angebot angebot) {
        if (angebot == null) {
            throw new IllegalArgumentException("angebot can't be null");
        }
        var entity = Angebot.<Angebot>findById(angebot.id);
        if (entity != null) {
            entity.description = angebot.description;
            entity.name = angebot.name;
            entity.vorname = angebot.vorname;
            entity.geburtsDatum = angebot.geburtsDatum;
            entity.strasse = angebot.strasse;
            entity.hausNummer = angebot.hausNummer;
            entity.plz = angebot.plz;
            entity.land = angebot.land;
            //entity.angebotType = angebot.angebotType;
        }
        return entity;
    }

    public static Angebot persistOrUpdate(Angebot angebot) {
        if (angebot == null) {
            throw new IllegalArgumentException("angebot can't be null");
        }
        if (angebot.id == null) {
            persist(angebot);
            return angebot;
        } else {
            return update(angebot);
        }
    }
}
