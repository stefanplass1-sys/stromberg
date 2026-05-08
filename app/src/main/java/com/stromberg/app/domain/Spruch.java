package com.stromberg.app.domain;

public record Spruch(
        int id,
        String kategorie,
        String thema,
        String zitat
) {
    public String kategorieLabel() {
        return switch (kategorie) {
            case "beste" -> "Beste Sprüche";
            case "fieseste" -> "Fieseste Sprüche";
            default -> kategorie;
        };
    }

    public String themaLabel() {
        return switch (thema) {
            case "selbstbild" -> "Selbstbild";
            case "chef-sein" -> "Chef-Sein";
            case "arbeit-buero" -> "Arbeit & Büro";
            case "frauen" -> "Frauen";
            case "maenner" -> "Männer";
            case "frauen-maenner" -> "Frauen & Männer";
            case "kollegen" -> "Kollegen";
            case "lebensweisheit" -> "Lebensweisheit";
            case "vorurteile" -> "Vorurteile";
            case "allgemein" -> "Allgemein";
            default -> thema;
        };
    }
}
