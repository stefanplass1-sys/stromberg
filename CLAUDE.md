# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Projektstatus

Das Repository befindet sich in der **Spec-/Planungsphase**. Es gibt noch keinen Java-Quellcode, kein `pom.xml`, keine Build-Konfiguration und keine Tests. Build-/Test-/Lint-Kommandos existieren also noch nicht — sie müssen im Zuge der Implementierung neu angelegt werden.

## Auftrag (siehe `spec.md`)

Eine **Spring-Boot-Webanwendung mit Vaadin (Java/Maven)** soll einen „Spruch des Tages" anzeigen. Dabei sind drei Inputs zu kombinieren:

1. **`Sprüche/`** — fachlicher Inhalt: 30 Stromberg-Zitate (deutsch), kategorisiert in „beste" / „fieseste" mit Themen-Tags. Maschinenlesbare Wahrheit ist `Sprüche/sprueche.json`; die Markdown-Dateien sind nur für Menschen.
2. **`Fotos/`** — 9 JPGs als Bildmaterial. Da es 30 Sprüche und nur 9 Fotos gibt, ist eine Zuordnung/Rotation zu wählen (z. B. Hash auf Spruch-ID modulo Bildanzahl).
3. **`stromberg/`** — Design-Vorlage für das **Look & Feel**, NICHT für den Inhalt.

## Wichtigste Unterscheidung: Design-Quelle vs. Inhalts-Quelle

Diese Trennung ist nicht offensichtlich und wird leicht verwechselt:

- **`stromberg/project/Spruch des Tages.html`** ist ein Claude-Design-Handoff-Bundle (HTML/CSS/JSX-Prototyp mit React via CDN + Babel inline). Es zeigt eine fiktive Versicherungs-Intranet-Seite („NVV — Norddeutsche Verbund-Versicherung") mit erfundenen Beispielsprüchen in `data.js`. Diese Beispieldaten sind **nicht** der Inhalt der zu bauenden App — sie sind Platzhalter für die Designvorlage.
- **Inhalt der App** sind die echten Stromberg-Zitate aus `Sprüche/sprueche.json`. Der Tonfall ist dadurch deutlich anders als im Mockup.
- Laut `stromberg/README.md` soll der Prototyp **visuell pixelgenau** nachgebaut, aber **nicht strukturell kopiert** werden — die interne Struktur des HTML/JSX-Prototyps ist nicht maßgeblich, nur das visuelle Ergebnis.

## Design-System aus dem Prototyp (in Vaadin/Lumo überführen)

Aus `stromberg/project/Spruch des Tages.html` extrahierte Tokens, die beim Vaadin-Theming nachzubilden sind:

- **Markenfarbe:** `#1676f3` (klassisches Vaadin-Blau) als Primärfarbe; abgeleitet werden `--brand-deep`, `--brand-tint`, `--brand-tint2` via `color-mix(in oklab, …)`.
- **Drei Themes:** `lumo-light` (Default), `beige` (Behörden-Ästhetik, gedecktes Beige), `lumo-dark`.
- **Drei Dichten:** `kompakt` / `regulär` / `großzügig` (steuert `--space`, `--topbar-h`, Padding der Quote-Card und Schriftgröße des Zitats).
- **Drei „Sarkasmus-Level":** `mild` / `trocken` / `beißend` (kosmetisch — beeinflusst Style des Zitats: italic / muted color).
- **Schriften:** IBM Plex Sans (Body), IBM Plex Mono (Metadaten/Tags/Zeitstempel), IBM Plex Serif (Überschriften und Quote-Text). System-Fonts als Fallback — laut Kommentar im Prototyp sind keine externen Font-CDNs erlaubt.
- **Layout-Skelett:** sticky TopBar (Brandmark + Suche + User), 248-px-SideNav (collapsible), Content-Grid mit Hauptbereich (1fr) und Stats-Sidebar (320 px, ab 1180 px Breite einspaltig), StatusBar als Footer.
- **Quote-Card:** linker 4-px-Gradient-Streifen, schräggestellter „GEPRÜFT"-Stempel oben rechts, große Anführungszeichen als dekoratives Serif-Glyph, Aktionszeile mit Like/Speichern/Weiterleiten/Drucken.

## Repository-Layout

- `spec.md` — Auftragsbeschreibung (kurz, 6 Zeilen, maßgeblich)
- `Sprüche/` — `README.md`, `01-die-besten-sprueche.md`, `02-die-fiesesten-sprueche.md`, `sprueche.json` (Schema: `sprueche[].{id, kategorie, thema, zitat}` plus `quelle`/`figur`-Metadaten)
- `Fotos/` — neun JPGs (`Download.jpg`, `Download (1).jpg` … `Download (8).jpg`)
- `stromberg/` — Design-Bundle: `README.md` (Handoff-Anleitung), `project/Spruch des Tages.html`, `project/app.jsx`, `project/data.js` (Mock-Daten), `project/tweaks-panel.jsx`

## Umgebungs-Hinweise

- **Plattform:** Windows 11, PowerShell. Bei Pfadangaben mit Leerzeichen (`C:\Claude-dev\T2 Stromberg\…`) immer quoten.
- **Verzeichnisname `Sprüche/`** enthält ein Umlaut — beim Anlegen von Pfaden/Imports beachten (PowerShell akzeptiert ihn, einige Tools nicht).
- **`rnd.de` ist DataDome-bot-geschützt:** Direktabrufe (WebFetch / `Invoke-WebRequest`) schlagen fehl. Funktioniert hat der Umweg über `https://web.archive.org/web/2024/<originalurl>`. Dies ist die Quelle der aktuellen Sprüche-Sammlung.

## Inhaltliche Sensibilität

Die „fiesesten" Sprüche (`Sprüche/02-die-fiesesten-sprueche.md`, IDs 16–30) sind bewusst grenzwertig — Teil der satirischen Stromberg-Figur. Die Datei enthält dazu einen Hinweis. Bei UI-Texten oder Kategorisierung diesen Kontext nicht weglassen.
