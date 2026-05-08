package com.stromberg.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stromberg.app.domain.Spruch;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SpruchService {

    private static final int FOTO_COUNT = 9;

    private List<Spruch> sprueche = List.of();

    @PostConstruct
    void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream in = new ClassPathResource("sprueche.json").getInputStream()) {
            JsonNode root = mapper.readTree(in);
            JsonNode arr = root.path("sprueche");
            List<Spruch> list = new ArrayList<>(arr.size());
            for (JsonNode n : arr) {
                list.add(new Spruch(
                        n.path("id").asInt(),
                        n.path("kategorie").asText(),
                        n.path("thema").asText(),
                        n.path("zitat").asText()
                ));
            }
            this.sprueche = Collections.unmodifiableList(list);
        }
    }

    public List<Spruch> alle() {
        return sprueche;
    }

    public Spruch spruchFuer(LocalDate datum) {
        if (sprueche.isEmpty()) {
            throw new IllegalStateException("Keine Sprüche geladen");
        }
        long tage = datum.toEpochDay();
        int idx = Math.floorMod(tage, sprueche.size());
        return sprueche.get(idx);
    }

    public Spruch nachfolgerVon(Spruch aktuell) {
        int aktuellerIdx = sprueche.indexOf(aktuell);
        int next = Math.floorMod(aktuellerIdx + 1, sprueche.size());
        return sprueche.get(next);
    }

    public String fotoFuer(Spruch spruch) {
        int idx = Math.floorMod(spruch.id() - 1, FOTO_COUNT);
        return "/fotos/foto-" + idx + ".jpg";
    }

    public int gesamtAnzahl() {
        return sprueche.size();
    }
}
