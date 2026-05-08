# Stromberg — „Spruch des Tages"

Web-App, die jeden Tag einen Stromberg-Spruch im Stil eines Versicherungs-Mitarbeiterportals anzeigt. Java 21 / Spring Boot 3.3 / Vaadin 24 / Maven.

## Inhalt

- `spec.md` — Auftrag (Java/Vaadin/Spring Boot/Maven, Sprüche aus `Sprüche/`, Fotos aus `Fotos/`, Look von `stromberg/`)
- `Sprüche/` — 30 Stromberg-Zitate, kategorisiert (`sprueche.json` ist die maschinenlesbare Wahrheit)
- `Fotos/` — 9 Bilder, die per Modulo-Mapping auf die 30 Sprüche verteilt werden
- `stromberg/` — Claude-Design-Handoff-Bundle (HTML/CSS/JSX-Prototyp), nur visuelle Vorlage
- `app/` — Maven-Projekt (Spring Boot + Vaadin)
- `CLAUDE.md` — Hinweise für AI-Assistenten

## Lokal bauen & starten

Voraussetzungen: JDK 21, Maven 3.9+

```bash
cd app
mvn test                # 6 Unit-Tests
mvn spring-boot:run     # Server auf http://localhost:8080
```

Beim allerersten Start lädt Vaadin Node.js und ~150 MB npm-Pakete; weitere Starts sind schnell.

## Mit Docker starten

```bash
docker compose up        # baut Image, mappt Port 8080
# oder einzeln
docker build -t stromberg-spruch .
docker run --rm -p 8080:8080 stromberg-spruch
```

Die Maven- und npm-Caches sind als Docker-Volumes persistiert (`maven-cache`, `frontend-cache`), damit der zweite Start nicht erneut npm-Install ausführt.

## Funktion

- Auf Wurzel-URL `/` zeigt die App den heutigen Spruch (deterministisch aus `LocalDate.now().toEpochDay() mod 30`).
- Button „Nächster Spruch ›" rotiert durch alle 30 Sprüche; „Zurück zum Spruch des Tages" springt zurück.
- Layout: TopBar, SideNav, Quote-Card mit Foto + Stempel, Sprüche-Archiv-Liste, Stats-Sidebar, StatusBar.

## Struktur des Maven-Moduls

```
app/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/stromberg/app/
    │   │   ├── Application.java          # Spring-Boot-Entry, @Theme("stromberg")
    │   │   ├── domain/Spruch.java        # Record (id, kategorie, thema, zitat) + Labels
    │   │   ├── service/SpruchService.java# Lädt sprueche.json beim Start
    │   │   └── ui/SpruchDesTagesView.java# Vaadin @Route("")
    │   ├── resources/
    │   │   ├── application.properties
    │   │   ├── sprueche.json             # Kopie aus Sprüche/
    │   │   └── static/fotos/             # foto-0.jpg … foto-8.jpg
    │   └── frontend/themes/stromberg/    # CSS-Theme (Tokens aus dem Prototyp)
    └── test/java/com/stromberg/app/service/SpruchServiceTest.java
```
