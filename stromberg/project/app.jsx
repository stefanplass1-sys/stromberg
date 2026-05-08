// NVV — Spruch des Tages — Hauptkomponente
// React + Babel inline. Vaadin-Lumo-inspirierter Look, original Tokens.

const { useState, useEffect, useMemo, useRef } = React;

// ── Datums-Helfer ──
function fmtDate(d, lang = 'de') {
  return d.toLocaleDateString(lang === 'de' ? 'de-DE' : 'en-US',
    { weekday: 'long', day: '2-digit', month: 'long', year: 'numeric' });
}
function fmtKW(d) {
  // ISO-Kalenderwoche
  const t = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
  const dayNum = t.getUTCDay() || 7;
  t.setUTCDate(t.getUTCDate() + 4 - dayNum);
  const yearStart = new Date(Date.UTC(t.getUTCFullYear(), 0, 1));
  return Math.ceil((((t - yearStart) / 86400000) + 1) / 7);
}

// ── TopBar ──
function TopBar({ onMenuToggle, density }) {
  return (
    <header className="topbar" data-density={density}>
      <div className="topbar-left">
        <button className="iconbtn" onClick={onMenuToggle} aria-label="Menü" title="Navigation umschalten">
          <span className="hamburger">≡</span>
        </button>
        <div className="brandmark">
          <div className="brandmark-logo">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" aria-hidden="true">
              <rect x="3" y="3" width="18" height="18" rx="3" fill="currentColor" opacity="0.18"/>
              <path d="M6 17V8.5L12 12l6-3.5V17" stroke="currentColor" strokeWidth="1.6" strokeLinejoin="round" fill="none"/>
              <circle cx="12" cy="12" r="1.6" fill="currentColor"/>
            </svg>
          </div>
          <div className="brandmark-text">
            <div className="brandmark-name">Norddeutsche Verbund-Versicherung</div>
            <div className="brandmark-sub">Mitarbeiterportal · v8.4.21-RELEASE</div>
          </div>
        </div>
      </div>
      <div className="topbar-search">
        <span className="search-icon">⌕</span>
        <input type="text" placeholder="Vorgangs-Nr., Aktenzeichen oder Mitarbeiter suchen…" />
        <kbd className="kbd">Strg</kbd><kbd className="kbd">K</kbd>
      </div>
      <div className="topbar-right">
        <button className="iconbtn" title="Hilfe (F1)">?</button>
        <button className="iconbtn dot" title="Benachrichtigungen">
          <span>◔</span><i className="dot-marker">3</i>
        </button>
        <div className="user">
          <div className="user-avatar" aria-hidden="true">CH</div>
          <div className="user-meta">
            <div className="user-name">Cornelia Hagedorn</div>
            <div className="user-role">Sachb. Schadenregulierung · KSt. 4711</div>
          </div>
        </div>
      </div>
    </header>
  );
}

// ── SideNav ──
function SideNav({ collapsed }) {
  return (
    <aside className={`sidenav${collapsed ? ' is-collapsed' : ''}`}>
      <nav>
        {window.NVV_NAV.map((sec, i) => (
          <div className="navsect" key={i}>
            <div className="navsect-h">{sec.sect}</div>
            {sec.items.map((it, j) => (
              <a className={`navitem${it.active ? ' is-active' : ''}`} key={j} href="#">
                <span className="navitem-ic" aria-hidden="true">{it.ic}</span>
                <span className="navitem-l">{it.l}</span>
                {it.badge != null && <span className="navitem-badge">{it.badge}</span>}
              </a>
            ))}
          </div>
        ))}
      </nav>
      <div className="sidenav-foot">
        <div className="sysstat">
          <span className="sysstat-dot" />
          Systemstatus: betriebsbereit
        </div>
        <div className="sysstat-meta">
          Letzte Synchronisation 07:14:02<br />
          Server: VAADIN-PROD-04
        </div>
      </div>
    </aside>
  );
}

// ── Breadcrumbs / Page header ──
function PageHeader({ date, onPrevDay, onNextDay, isToday }) {
  return (
    <div className="pagehead">
      <nav className="crumb" aria-label="Pfad">
        <a href="#">Mitarbeiterportal</a>
        <span className="crumb-sep">›</span>
        <a href="#">Tägliche Mitteilungen</a>
        <span className="crumb-sep">›</span>
        <span className="crumb-cur">Spruch des Tages</span>
      </nav>
      <div className="pagehead-row">
        <div>
          <h1 className="pagehead-title">Spruch des Tages</h1>
          <div className="pagehead-sub">
            Tägliche redaktionelle Mitteilung der Innenkommunikation · Ressort 7-A · KW {fmtKW(date)}
          </div>
        </div>
        <div className="datebar">
          <button className="btn btn-ghost" onClick={onPrevDay} title="Vortag">‹</button>
          <div className="datebar-main">
            <span className="datebar-pill">{isToday ? 'HEUTE' : 'ARCHIV'}</span>
            <span className="datebar-text">{fmtDate(date)}</span>
          </div>
          <button className="btn btn-ghost" onClick={onNextDay} title="Folgetag" disabled={isToday}>›</button>
        </div>
      </div>
    </div>
  );
}

// ── QuoteCard ──
function QuoteCard({ q, liked, saved, onLike, onSave, sarcasm }) {
  return (
    <section className="card quote" aria-labelledby="sdt-title">
      <header className="quote-head">
        <div className="quote-tags">
          <span className="tag tag-primary">{q.kategorie}</span>
          <span className="tag tag-mono">Az.: {q.az}</span>
          <span className="tag tag-ghost">Ausgabe Nr. {String(q.nr).padStart(4, '0')}</span>
        </div>
        <div className="quote-stamp" aria-hidden="true">
          <div className="stamp">
            <div className="stamp-l1">GEPRÜFT</div>
            <div className="stamp-l2">Innenrevision</div>
            <div className="stamp-l3">i.A. {q.autor.split(' ').slice(-1)[0]}</div>
          </div>
        </div>
      </header>

      <div className="quote-body" data-sarcasm={sarcasm}>
        <span className="quote-mark" aria-hidden="true">„</span>
        <p id="sdt-title" className="quote-text">{q.text}</p>
        <span className="quote-mark quote-mark-end" aria-hidden="true">"</span>
      </div>

      <footer className="quote-foot">
        <div className="quote-author">
          <div className="quote-author-avatar" aria-hidden="true">
            {q.autor.split(' ').map(s => s[0]).slice(0, 2).join('')}
          </div>
          <div className="quote-author-meta">
            <div className="quote-author-name">{q.autor}</div>
            <div className="quote-author-role">{q.rolle}</div>
            <div className="quote-author-btr">{q.btr}</div>
          </div>
        </div>
        <div className="quote-actions">
          <button className={`btn ${liked ? 'btn-primary' : 'btn-secondary'}`} onClick={onLike}>
            <span aria-hidden="true">{liked ? '✓' : '👍'}</span>
            {liked ? 'Zustimmung vermerkt' : 'Zustimmung erteilen'}
            <span className="btn-count">{q.likes + (liked ? 1 : 0)}</span>
          </button>
          <button className={`btn ${saved ? 'btn-primary' : 'btn-secondary'}`} onClick={onSave}>
            <span aria-hidden="true">{saved ? '◆' : '◇'}</span>
            {saved ? 'Aktenkundig gemacht' : 'In Akte ablegen'}
          </button>
          <button className="btn btn-secondary">
            <span aria-hidden="true">✉</span>
            Per E-Mail weiterleiten
            <span className="btn-count">{q.weiterleitungen}</span>
          </button>
          <button className="btn btn-ghost" title="Drucken (3-fach)">
            <span aria-hidden="true">🖶</span>
            Drucken
          </button>
        </div>
      </footer>
    </section>
  );
}

// ── Kommentare ──
function CommentList({ kommentare }) {
  const [draft, setDraft] = useState("");
  const [pending, setPending] = useState([]);
  const submit = () => {
    if (!draft.trim()) return;
    setPending(p => [...p, { autor: "Cornelia Hagedorn", rolle: "Sachb. Schadenregulierung",
                              zeit: new Date().toLocaleTimeString('de-DE', { hour: '2-digit', minute: '2-digit' }),
                              text: draft.trim(), self: true }]);
    setDraft("");
  };
  const all = [...kommentare, ...pending];
  return (
    <section className="card comments">
      <header className="card-head">
        <h2>Kommentare</h2>
        <span className="card-head-meta">{all.length} Beiträge · Moderiert nach DV-Richtlinie KOM-2024/03</span>
      </header>
      <ol className="comment-list">
        {all.map((c, i) => (
          <li key={i} className={`comment${c.self ? ' is-self' : ''}`}>
            <div className="comment-avatar" aria-hidden="true">
              {c.autor.split(' ').map(s => s[0]).slice(0, 2).join('')}
            </div>
            <div className="comment-main">
              <div className="comment-meta">
                <span className="comment-name">{c.autor}</span>
                <span className="comment-role">{c.rolle}</span>
                <span className="comment-time">{c.zeit}</span>
              </div>
              <div className="comment-text">{c.text}</div>
              <div className="comment-actions">
                <button className="link-btn">Antworten</button>
                <button className="link-btn">Zustimmung</button>
                <button className="link-btn">Melden</button>
              </div>
            </div>
          </li>
        ))}
      </ol>
      <div className="comment-form">
        <textarea
          rows="3"
          value={draft}
          onChange={e => setDraft(e.target.value)}
          placeholder="Sachlicher Kommentar (Bitte beachten: § 4 Kommunikationsleitlinie — keine privaten Anliegen)"
        />
        <div className="comment-form-foot">
          <span className="comment-form-hint">Kommentar wird mit Ihrer Personalnummer 04711-CH veröffentlicht.</span>
          <button className="btn btn-primary" onClick={submit} disabled={!draft.trim()}>Kommentar einreichen</button>
        </div>
      </div>
    </section>
  );
}

// ── Stats-Sidebar ──
function StatsPanel({ q, day }) {
  const stats = [
    { l: "Tage in Folge gelesen", v: 247, sub: "ohne Unterbrechung" },
    { l: "Persönliche Trefferquote", v: "92,4 %", sub: "Spruch verstanden" },
    { l: "Gesamtzustimmung heute", v: q.likes, sub: "abteilungsübergreifend" },
    { l: "Weiterleitungen", v: q.weiterleitungen, sub: "an Außenstellen" },
  ];
  return (
    <aside className="stats">
      <section className="card">
        <header className="card-head"><h2>Tageskennzahlen</h2></header>
        <dl className="kpi-list">
          {stats.map((s, i) => (
            <div className="kpi" key={i}>
              <dt>{s.l}</dt>
              <dd><span className="kpi-v">{s.v}</span><span className="kpi-sub">{s.sub}</span></dd>
            </div>
          ))}
        </dl>
      </section>

      <section className="card">
        <header className="card-head"><h2>Redaktionsplan</h2></header>
        <ul className="plan">
          <li>
            <span className="plan-time">Mo</span>
            <span className="plan-topic">Verfahrensordnung</span>
            <span className="plan-author">R. Möller</span>
          </li>
          <li className="is-current">
            <span className="plan-time">Di</span>
            <span className="plan-topic">{q.kategorie}</span>
            <span className="plan-author">{q.autor.split(' ').slice(-1)[0]}</span>
          </li>
          <li>
            <span className="plan-time">Mi</span>
            <span className="plan-topic">Bürotechnik</span>
            <span className="plan-author">HW. Pott</span>
          </li>
          <li>
            <span className="plan-time">Do</span>
            <span className="plan-topic">Lebensphilosophie</span>
            <span className="plan-author">U. Sasse</span>
          </li>
          <li>
            <span className="plan-time">Fr</span>
            <span className="plan-topic">Verpflegung</span>
            <span className="plan-author">Fr. Heidenreich</span>
          </li>
        </ul>
      </section>

      <section className="card card-formal">
        <header className="card-head"><h2>Hinweise</h2></header>
        <ul className="hinweise">
          <li><b>BV-Allg. § 12 Abs. 3:</b> Tägliche Lektüre des „Spruchs des Tages" gilt als Arbeitszeit (max. 4 Min.).</li>
          <li><b>Datenschutz:</b> Lesebestätigungen werden 6 Jahre gemäß Aufbewahrungs­fristen­verordnung gespeichert.</li>
          <li><b>Druckkosten:</b> Mehrfachausdruck ist auf Kostenstelle 4711-INTERN zu buchen.</li>
        </ul>
      </section>
    </aside>
  );
}

// ── Footer / Statusleiste ──
function StatusBar({ q }) {
  return (
    <footer className="statusbar">
      <div className="statusbar-l">
        <span className="status-dot" /> verbunden mit DB-CLUSTER-NORD
      </div>
      <div className="statusbar-c">
        Vorgang: {q.az} · Zuletzt geändert: heute, 06:42:11 durch SYSTEM
      </div>
      <div className="statusbar-r">
        <span>Strg+S Speichern</span>
        <span>F2 Bearbeiten</span>
        <span>F11 Vollbild</span>
      </div>
    </footer>
  );
}

// ── Hauptkomponente ──
const TWEAK_DEFAULTS = /*EDITMODE-BEGIN*/{
  "theme": "lumo-light",
  "primaryColor": "#1676f3",
  "density": "regular",
  "sarcasm": "trocken",
  "showSidebar": true,
  "fontFamily": "plex"
}/*EDITMODE-END*/;

const PRIMARY_OPTIONS = [
  "#1676f3", // klassisches Vaadin-Blau
  "#0d4a8a", // konservativ-tief
  "#2d7a5f", // sparkassen-grün
  "#a83232", // versicherungs-rot
];

const FONT_STACKS = {
  plex: '"IBM Plex Sans", system-ui, -apple-system, "Segoe UI", sans-serif',
  source: '"Source Sans 3", system-ui, -apple-system, "Segoe UI", sans-serif',
  helvetica: '"Helvetica Neue", Helvetica, Arial, sans-serif',
};

function App() {
  const [t, setTweak] = useTweaks(TWEAK_DEFAULTS);
  const [navCollapsed, setNavCollapsed] = useState(false);
  const [dayOffset, setDayOffset] = useState(0); // 0 = heute, -1 = gestern
  const [liked, setLiked] = useState({});
  const [saved, setSaved] = useState({});

  const today = useMemo(() => {
    const d = new Date(2026, 4, 8); // 8. Mai 2026
    d.setDate(d.getDate() + dayOffset);
    return d;
  }, [dayOffset]);

  const quoteIdx = ((dayOffset % window.NVV_QUOTES.length) + window.NVV_QUOTES.length) % window.NVV_QUOTES.length;
  const q = window.NVV_QUOTES[quoteIdx];

  // Theme-Klasse auf Wurzel
  const rootClass = `app theme-${t.theme} density-${t.density} sarcasm-${t.sarcasm}`;
  const rootStyle = {
    '--brand': t.primaryColor,
    '--font-base': FONT_STACKS[t.fontFamily] || FONT_STACKS.plex,
  };

  return (
    <div className={rootClass} style={rootStyle} data-screen-label="Spruch des Tages">
      <TopBar onMenuToggle={() => setNavCollapsed(c => !c)} density={t.density} />
      <div className="shell">
        {t.showSidebar && <SideNav collapsed={navCollapsed} />}
        <main className="content">
          <PageHeader
            date={today}
            isToday={dayOffset === 0}
            onPrevDay={() => setDayOffset(o => o - 1)}
            onNextDay={() => setDayOffset(o => Math.min(0, o + 1))}
          />
          <div className="content-grid">
            <div className="content-main">
              <QuoteCard
                q={q}
                liked={!!liked[q.nr]}
                saved={!!saved[q.nr]}
                sarcasm={t.sarcasm}
                onLike={() => setLiked(s => ({ ...s, [q.nr]: !s[q.nr] }))}
                onSave={() => setSaved(s => ({ ...s, [q.nr]: !s[q.nr] }))}
              />
              <CommentList kommentare={window.NVV_KOMMENTARE} />
            </div>
            <StatsPanel q={q} day={today} />
          </div>
        </main>
      </div>
      <StatusBar q={q} />

      <TweaksPanel title="Tweaks">
        <TweakSection label="Erscheinungsbild">
          <TweakRadio label="Theme" value={t.theme}
            options={[
              { value: 'lumo-light', label: 'Hell' },
              { value: 'beige', label: 'Behörde' },
              { value: 'lumo-dark', label: 'Dunkel' },
            ]}
            onChange={v => setTweak('theme', v)} />
          <TweakColor label="Markenfarbe" value={t.primaryColor}
            options={PRIMARY_OPTIONS}
            onChange={v => setTweak('primaryColor', v)} />
          <TweakRadio label="Schrift" value={t.fontFamily}
            options={[
              { value: 'plex', label: 'IBM Plex' },
              { value: 'source', label: 'Source Sans' },
              { value: 'helvetica', label: 'Helvetica' },
            ]}
            onChange={v => setTweak('fontFamily', v)} />
        </TweakSection>
        <TweakSection label="Layout">
          <TweakRadio label="Dichte" value={t.density}
            options={['kompakt', 'regulär', 'großzügig']}
            onChange={v => setTweak('density', v)} />
          <TweakToggle label="Seitennavigation" value={t.showSidebar}
            onChange={v => setTweak('showSidebar', v)} />
        </TweakSection>
        <TweakSection label="Inhalt">
          <TweakRadio label="Sarkasmus-Level" value={t.sarcasm}
            options={['mild', 'trocken', 'beißend']}
            onChange={v => setTweak('sarcasm', v)} />
        </TweakSection>
      </TweaksPanel>
    </div>
  );
}

ReactDOM.createRoot(document.getElementById('root')).render(<App />);
