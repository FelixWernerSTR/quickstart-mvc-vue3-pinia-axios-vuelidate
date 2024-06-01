package de.svi.angebot.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.svi.angebot.TestUtil;
import org.junit.jupiter.api.Test;

public class AngebotTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Angebot.class);
        Angebot angebot1 = new Angebot();
        angebot1.id = 1L;
        Angebot angebot2 = new Angebot();
        angebot2.id = angebot1.id;
        assertThat(angebot1).isEqualTo(angebot2);
        angebot2.id = 2L;
        assertThat(angebot1).isNotEqualTo(angebot2);
        angebot1.id = null;
        assertThat(angebot1).isNotEqualTo(angebot2);
    }
}
