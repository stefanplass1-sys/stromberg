# Schlanker Dev-Mode-Container für die Stromberg-„Spruch des Tages"-App.
# Vaadin läuft im Development-Mode: mvn spring-boot:run löst beim ersten Start
# den Frontend-Build (npm install, Vite) automatisch aus.
#
# Build:  docker build -t stromberg-spruch .
# Run:    docker run --rm -p 8080:8080 stromberg-spruch
# Oder:   docker compose up

FROM maven:3.9-eclipse-temurin-21

WORKDIR /workspace

# Maven-Dependencies vorab cachen (separat vom Quellcode-Layer)
COPY app/pom.xml ./pom.xml
RUN mvn -B -q dependency:go-offline || true

# Quellcode + Resources kopieren
COPY app/src ./src

EXPOSE 8080

# Vaadin im Dev-Mode starten. Beim ersten Lauf zieht es Node.js und npm-Pakete
# (~150 MB), das kann einige Minuten dauern.
CMD ["mvn", "-B", "spring-boot:run"]
