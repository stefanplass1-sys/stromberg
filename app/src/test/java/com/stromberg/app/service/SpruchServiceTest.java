package com.stromberg.app.service;

import com.stromberg.app.domain.Spruch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpruchServiceTest {

    private SpruchService service;

    @BeforeEach
    void setUp() throws Exception {
        service = new SpruchService();
        service.load();
    }

    @Test
    void laedtAlleDreissigSprueche() {
        assertEquals(30, service.gesamtAnzahl());
    }

    @Test
    void spruechePflichtfelderSindGesetzt() {
        List<Spruch> alle = service.alle();
        for (Spruch s : alle) {
            assertAll("Spruch " + s.id(),
                    () -> assertTrue(s.id() >= 1 && s.id() <= 30, "id im Bereich"),
                    () -> assertNotNull(s.zitat(), "zitat nicht null"),
                    () -> assertTrue(s.zitat().length() > 10, "zitat nicht-leer"),
                    () -> assertNotNull(s.kategorie()),
                    () -> assertNotNull(s.thema())
            );
        }
    }

    @Test
    void spruchDesTagesIstDeterministisch() {
        LocalDate tag = LocalDate.of(2026, 5, 8);
        Spruch s1 = service.spruchFuer(tag);
        Spruch s2 = service.spruchFuer(tag);
        assertEquals(s1.id(), s2.id(), "selbes Datum liefert selben Spruch");
    }

    @Test
    void spruchDesTagesAendertSichAmFolgetag() {
        LocalDate heute = LocalDate.of(2026, 5, 8);
        Spruch s1 = service.spruchFuer(heute);
        Spruch s2 = service.spruchFuer(heute.plusDays(1));
        assertTrue(s1.id() != s2.id() || service.gesamtAnzahl() == 1,
                "Folgetag sollte anderen Spruch liefern");
    }

    @Test
    void fotoFuerJedenSpruchExistiertImErlaubtenBereich() {
        for (Spruch s : service.alle()) {
            String pfad = service.fotoFuer(s);
            assertTrue(pfad.matches("/fotos/foto-[0-8]\\.jpg"),
                    "Foto-Pfad gültig für Spruch " + s.id() + ": " + pfad);
        }
    }

    @Test
    void kategorienVerteilung() {
        long beste = service.alle().stream().filter(s -> "beste".equals(s.kategorie())).count();
        long fieseste = service.alle().stream().filter(s -> "fieseste".equals(s.kategorie())).count();
        assertEquals(15, beste);
        assertEquals(15, fieseste);
    }
}
