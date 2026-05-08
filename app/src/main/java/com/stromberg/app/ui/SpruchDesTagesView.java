package com.stromberg.app.ui;

import com.stromberg.app.domain.Spruch;
import com.stromberg.app.service.SpruchService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Route("")
@PageTitle("Capitol Versicherung · Spruch des Tages")
public class SpruchDesTagesView extends VerticalLayout {

    private static final Locale DE = Locale.GERMAN;
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("EEEE, dd. MMMM yyyy", DE);

    private final SpruchService service;
    private final LocalDate heute;
    private final Spruch tagesSpruch;
    private Spruch aktuell;

    private final Div contentMain = new Div();
    private final Div statsRoot = new Div();
    private final Div statusbarRoot = new Div();
    private final Span actionLabel = new Span();

    public SpruchDesTagesView(SpruchService service) {
        this.service = service;
        setPadding(false);
        setSpacing(false);
        addClassName("app");

        this.heute = LocalDate.now();
        this.tagesSpruch = service.spruchFuer(heute);
        this.aktuell = tagesSpruch;

        add(buildTopBar());

        Div shell = new Div();
        shell.addClassName("shell");
        shell.add(buildSideNav());

        Div content = new Div();
        content.addClassName("content");
        content.add(buildPageHead(heute));

        Div grid = new Div();
        grid.addClassName("content-grid");

        contentMain.addClassName("content-main");
        statsRoot.addClassName("stats");
        grid.add(contentMain, statsRoot);

        content.add(grid);
        content.add(buildActionBar());
        shell.add(content);
        add(shell);

        statusbarRoot.addClassName("statusbar");
        add(statusbarRoot);

        zeigeSpruch(aktuell);
    }

    private void zeigeSpruch(Spruch spruch) {
        this.aktuell = spruch;
        boolean istHeutiger = spruch.id() == tagesSpruch.id();

        contentMain.removeAll();
        contentMain.add(buildQuoteCard(spruch, service.fotoFuer(spruch), istHeutiger));
        contentMain.add(buildArchivCard(service, spruch));

        statsRoot.removeAll();
        statsRoot.add(buildStatsKPI(spruch));
        statsRoot.add(buildHinweise());

        statusbarRoot.removeAll();
        statusbarRoot.add(new Html("<div><span class=\"status-dot\"></span> verbunden mit DB-CLUSTER-NORD</div>"));
        statusbarRoot.add(new Html(("<div class=\"statusbar-c\">Vorgang: CAP-SDT-2026/%04d · Zuletzt geändert: heute, 06:42:11 durch SYSTEM</div>")
                .formatted(spruch.id())));
        statusbarRoot.add(new Html("<div class=\"statusbar-r\"><span>Strg+S Speichern</span></div>"));

        actionLabel.setText(istHeutiger
                ? "Sie sehen den Spruch des Tages."
                : "Sie sehen einen Archiv-Spruch (Nr. %02d).".formatted(spruch.id()));
    }

    private Component buildTopBar() {
        Div topbar = new Div();
        topbar.addClassName("topbar");

        Div left = new Div();
        left.addClassName("topbar-left");
        Div brand = new Div();
        brand.addClassName("brandmark");
        Div logo = new Div();
        logo.addClassName("brandmark-logo");
        logo.add(new Html("""
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <rect x="3" y="3" width="18" height="18" rx="3" fill="currentColor" opacity="0.18"/>
                    <path d="M6 17V8.5L12 12l6-3.5V17" stroke="currentColor" stroke-width="1.6" stroke-linejoin="round" fill="none"/>
                    <circle cx="12" cy="12" r="1.6" fill="currentColor"/>
                </svg>
                """));
        Div text = new Div();
        text.addClassName("brandmark-text");
        Div name = new Div();
        name.addClassName("brandmark-name");
        name.setText("Capitol Versicherung AG");
        Div sub = new Div();
        sub.addClassName("brandmark-sub");
        sub.setText("Mitarbeiterportal · Abt. Schadensregulierung");
        text.add(name, sub);
        brand.add(logo, text);
        left.add(brand);

        Div search = new Div();
        search.addClassName("topbar-search");
        search.add(new Html("<span class='search-icon'>⌕</span>"));
        search.add(new Html("<input type='text' placeholder='Vorgangsnummer oder Versicherungsnehmer suchen…' />"));
        search.add(new Html("<kbd class='kbd'>Strg</kbd>"));
        search.add(new Html("<kbd class='kbd'>K</kbd>"));

        Div right = new Div();
        right.addClassName("topbar-right");
        Div user = new Div();
        user.addClassName("user");
        Div avatar = new Div();
        avatar.addClassName("user-avatar");
        avatar.setText("BS");
        Div meta = new Div();
        meta.addClassName("user-meta");
        Div uName = new Div();
        uName.addClassName("user-name");
        uName.setText("Bernd Stromberg");
        Div uRole = new Div();
        uRole.addClassName("user-role");
        uRole.setText("Abteilungsleiter Schadensregulierung");
        meta.add(uName, uRole);
        user.add(avatar, meta);
        right.add(user);

        topbar.add(left, search, right);
        return topbar;
    }

    private Component buildSideNav() {
        Div nav = new Div();
        nav.addClassName("sidenav");
        nav.add(new Html("""
                <nav>
                  <div class="navsect">
                    <div class="navsect-h">Mein Arbeitsplatz</div>
                    <a class="navitem" href="#"><span class="navitem-ic">▦</span><span class="navitem-l">Übersicht</span></a>
                    <a class="navitem" href="#"><span class="navitem-ic">✉</span><span class="navitem-l">Posteingang</span><span class="navitem-badge">42</span></a>
                    <a class="navitem" href="#"><span class="navitem-ic">❑</span><span class="navitem-l">Aufgaben</span><span class="navitem-badge">7</span></a>
                    <a class="navitem is-active" href="#"><span class="navitem-ic">★</span><span class="navitem-l">Spruch des Tages</span></a>
                    <div class="navsect-h" style="margin-top:8px;">Vorgänge</div>
                    <a class="navitem" href="#"><span class="navitem-ic">≡</span><span class="navitem-l">Schadensmeldungen</span></a>
                    <a class="navitem" href="#"><span class="navitem-ic">≡</span><span class="navitem-l">Kfz-Versicherung</span></a>
                    <a class="navitem" href="#"><span class="navitem-ic">≡</span><span class="navitem-l">Hausrat &amp; Gebäude</span></a>
                    <a class="navitem" href="#"><span class="navitem-ic">≡</span><span class="navitem-l">Lebensversicherung</span></a>
                    <div class="navsect-h" style="margin-top:8px;">Verwaltung</div>
                    <a class="navitem" href="#"><span class="navitem-ic">⌘</span><span class="navitem-l">Reisekosten</span></a>
                    <a class="navitem" href="#"><span class="navitem-ic">⌘</span><span class="navitem-l">Urlaubsantrag</span></a>
                    <a class="navitem" href="#"><span class="navitem-ic">⌘</span><span class="navitem-l">Krankmeldung</span></a>
                  </div>
                </nav>
                """));
        nav.add(new Html("""
                <div class="sidenav-foot">
                  <div class="sysstat"><span class="sysstat-dot"></span> Systemstatus: betriebsbereit</div>
                  <div class="sysstat-meta">Letzte Synchronisation 07:14:02<br/>Server: CAPITOL-PROD-04</div>
                </div>
                """));
        return nav;
    }

    private Component buildPageHead(LocalDate heute) {
        Div head = new Div();
        head.addClassName("pagehead");

        head.add(new Html("""
                <nav class="crumb" aria-label="Pfad">
                  <a href="#">Mitarbeiterportal</a>
                  <span class="crumb-sep">›</span>
                  <a href="#">Tägliche Mitteilungen</a>
                  <span class="crumb-sep">›</span>
                  <span class="crumb-cur">Spruch des Tages</span>
                </nav>
                """));

        Div row = new Div();
        row.addClassName("pagehead-row");
        Div titleBlock = new Div();
        Div title = new Div();
        title.addClassName("pagehead-title");
        title.setText("Spruch des Tages");
        Div subtitle = new Div();
        subtitle.addClassName("pagehead-sub");
        int kw = heute.get(WeekFields.ISO.weekOfWeekBasedYear());
        subtitle.setText("Tägliche Mitteilung der Innenkommunikation · Capitol Versicherung · KW " + kw);
        titleBlock.add(title, subtitle);

        Div datebar = new Div();
        datebar.addClassName("datebar");
        datebar.add(new Html("""
                <div class="datebar-main">
                  <span class="datebar-pill">HEUTE</span>
                  <span class="datebar-text">%s</span>
                </div>
                """.formatted(heute.format(DATE_FMT))));

        row.add(titleBlock, datebar);
        head.add(row);
        return head;
    }

    private Component buildQuoteCard(Spruch spruch, String fotoUrl, boolean istHeutiger) {
        Div card = new Div();
        card.addClassName("card");
        card.addClassName("quote");
        card.addClassName("sarcasm-trocken");

        Div headRow = new Div();
        headRow.addClassName("quote-head");
        Div tags = new Div();
        tags.addClassName("quote-tags");
        Span kat = new Span(spruch.kategorieLabel());
        kat.addClassNames("tag", "tag-primary");
        Span thema = new Span(spruch.themaLabel());
        thema.addClassNames("tag", "tag-ghost");
        Span nr = new Span("Ausgabe Nr. " + String.format("%04d", spruch.id()));
        nr.addClassNames("tag", "tag-mono");
        tags.add(kat, thema, nr);
        if (!istHeutiger) {
            Span archiv = new Span("ARCHIV");
            archiv.addClassNames("tag", "tag-ghost");
            archiv.getStyle().set("color", "var(--danger)").set("border-color", "var(--danger)");
            tags.add(archiv);
        }

        Div stamp = new Div();
        stamp.addClassName("quote-stamp");
        stamp.add(new Html("""
                <div class="stamp">
                  <div class="stamp-l1">GEPRÜFT</div>
                  <div class="stamp-l2">Innenrevision</div>
                  <div class="stamp-l3">i.A. Stromberg</div>
                </div>
                """));
        headRow.add(tags, stamp);

        Image foto = new Image(fotoUrl, "Stromberg-Foto");
        foto.addClassName("quote-foto");

        Div body = new Div();
        body.addClassName("quote-body");
        body.add(new Html("<span class='quote-mark' aria-hidden='true'>„</span>"));
        Paragraph text = new Paragraph(spruch.zitat());
        text.addClassName("quote-text");
        body.add(text);
        body.add(new Html("<span class='quote-mark quote-mark-end' aria-hidden='true'>\"</span>"));

        Div foot = new Div();
        foot.addClassName("quote-foot");
        Div author = new Div();
        author.addClassName("quote-author");
        Div aAvatar = new Div();
        aAvatar.addClassName("quote-author-avatar");
        aAvatar.setText("BS");
        Div aMeta = new Div();
        aMeta.addClassName("quote-author-meta");
        Div aName = new Div();
        aName.addClassName("quote-author-name");
        aName.setText("Bernd Stromberg");
        Div aRole = new Div();
        aRole.addClassName("quote-author-role");
        aRole.setText("Abteilungsleiter Schadensregulierung");
        Div aBtr = new Div();
        aBtr.addClassName("quote-author-btr");
        aBtr.setText("Capitol Versicherung AG · seit 2003");
        aMeta.add(aName, aRole, aBtr);
        author.add(aAvatar, aMeta);

        Div actions = new Div();
        actions.addClassName("quote-actions");
        actions.add(new Html("<button class='btn btn-primary'><span aria-hidden='true'>👍</span> Zustimmung erteilen</button>"));
        actions.add(new Html("<button class='btn btn-secondary'><span aria-hidden='true'>◇</span> In Akte ablegen</button>"));
        actions.add(new Html("<button class='btn btn-ghost'><span aria-hidden='true'>🖶</span> Drucken</button>"));
        foot.add(author, actions);

        card.add(headRow, foto, body, foot);
        return card;
    }

    private Component buildArchivCard(SpruchService service, Spruch aktuell) {
        Div card = new Div();
        card.addClassName("card");

        Div head = new Div();
        head.addClassName("card-head");
        head.add(new Html("<h2>Sprüche-Archiv</h2>"));
        head.add(new Html("<span class='card-head-meta'>" + service.gesamtAnzahl() + " Einträge gesamt</span>"));
        card.add(head);

        Div list = new Div();
        list.addClassName("archiv-list");
        for (Spruch s : service.alle()) {
            Div item = new Div();
            item.addClassName("archiv-item");
            if (s.id() == aktuell.id()) {
                item.addClassName("is-current");
            }
            Span num = new Span("#" + String.format("%02d", s.id()));
            num.addClassName("archiv-num");
            Span thema = new Span(s.themaLabel());
            thema.addClassName("archiv-thema");
            Span txt = new Span(kurz(s.zitat()));
            txt.addClassName("archiv-text");
            item.add(num, thema, txt);
            list.add(item);
        }
        card.add(list);
        return card;
    }

    private static String kurz(String s) {
        if (s.length() <= 80) return s;
        return s.substring(0, 77) + "…";
    }

    private Component buildStatsKPI(Spruch spruch) {
        Div k1 = new Div();
        k1.addClassName("card");
        k1.add(new Html("<header class=\"card-head\"><h2>Tageskennzahlen</h2></header>"));
        k1.add(new Html("""
                <dl class="kpi-list">
                  <div class="kpi"><dt>Gesamt-Sprüche im System</dt><dd><span class="kpi-v">%d</span><span class="kpi-sub">verifiziert</span></dd></div>
                  <div class="kpi"><dt>Aktuelle Kategorie</dt><dd><span class="kpi-v">%s</span><span class="kpi-sub">Tagesausgabe</span></dd></div>
                  <div class="kpi"><dt>Thema heute</dt><dd><span class="kpi-v">%s</span><span class="kpi-sub">Schwerpunkt</span></dd></div>
                </dl>
                """.formatted(service.gesamtAnzahl(), spruch.kategorieLabel(), spruch.themaLabel())));
        return k1;
    }

    private Component buildHinweise() {
        Div k2 = new Div();
        k2.addClassName("card");
        k2.addClassName("card-formal");
        k2.add(new Html("<header class=\"card-head\"><h2>Hinweise</h2></header>"));
        k2.add(new Html("""
                <ul class="hinweise">
                  <li><b>BV-Allg. § 12 Abs. 3:</b> Tägliche Lektüre des „Spruchs des Tages" gilt als Arbeitszeit (max. 4 Min.).</li>
                  <li><b>Datenschutz:</b> Lesebestätigungen werden 6 Jahre gespeichert.</li>
                  <li><b>Hinweis:</b> Sprüche entstammen dem fiktionalen Werk „Stromberg" (ProSieben, 2004–2012).</li>
                </ul>
                """));
        return k2;
    }

    private Component buildActionBar() {
        Div bar = new Div();
        bar.addClassName("actionbar");

        actionLabel.addClassName("actionbar-label");

        Button weiter = new Button("Zack noch mal", e -> zeigeSpruch(service.nachfolgerVon(aktuell)));
        weiter.addClassName("actionbar-btn");

        Button heutiger = new Button("Zurück zum Spruch des Tages", e -> zeigeSpruch(tagesSpruch));
        heutiger.addClassName("actionbar-btn-ghost");

        bar.add(actionLabel, heutiger, weiter);
        return bar;
    }
}
