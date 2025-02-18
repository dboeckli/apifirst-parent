# API First Development with OpenAPI and Spring Boot

## Überblick

Dieses Projekt demonstriert die API-First-Entwicklung unter Verwendung von OpenAPI und Spring Boot. Es basiert auf dem Udemy-Kurs [API First Engineering with Spring Boot](https://www.udemy.com/course/api-first-engineering-with-spring-boot).

## Projektstruktur

Das Projekt besteht aus drei Hauptkomponenten:

1. `apifirst-api`: Generiert mit der OpenAPI-Spezifikation die Modelklassen
    - Diese Komponente ist verantwortlich für die Erstellung der Modelklassen basierend auf der OpenAPI-Spezifikation.
    - Die generierten Modelklassen werden von den anderen Komponenten verwendet.

2. `apifirst-server`: Implementierung des Servers basierend auf der API-Spezifikation
    - Verwendet eine In-Memory-HashMap zur Datenspeicherung
    - Nutzt die von `apifirst-api` generierten Modelklassen

3. `apifirst-server-jpa`: Erweiterung des Servers mit JPA-Integration
    - Persistiert Daten in einer H2-Datenbank
    - Nutzt ebenfalls die von `apifirst-api` generierten Modelklassen

Beide Server-Implementierungen (`apifirst-server` und `apifirst-server-jpa`) implementieren die gleiche Schnittstelle, verwenden aber unterschiedliche Datenspeicherungsmechanismen.

### API-Dokumentation und OpenAPI-Definitionen

Die OpenAPI-Definitionen werden im Projekt `apifirst-api` erstellt. Diese Definitionen durchlaufen einen GitHub-Build-Prozess mit Redocly, wo sie validiert und verarbeitet werden.

Die finale, validierte OpenAPI-Spezifikation ist auf Redocly verfügbar. Sie bietet eine interaktive und benutzerfreundliche Oberfläche zur Erkundung der API-Spezifikation.

Um auf die Redocly-Dokumentation zuzugreifen, besuchen Sie bitte den folgenden Link:
[API-Dokumentation auf Redocly](https://dboeckli.redocly.app/openapi/openapi/customer)

Dieser Link führt zur finalen YAML-Datei, die durch den Redocly-Prozess erstellt wurde.

## Generierte Quellen

Basierend auf den OpenAPI-Definitionen werden Quellen generiert, einschließlich der Modelklassen und API-Interfaces. Diese werden in das folgende Verzeichnis geschrieben:

Die generierten Quellen, einschließlich der Modelklassen und API-Interfaces, werden in das folgende Verzeichnis geschrieben:
apifirst-api/target/generated-sources/openapi

Diese generierten Dateien werden automatisch in den Klassenpfad des Projekts aufgenommen und können von anderen Modulen verwendet werden.

## Datenspeicherung

- **apifirst-server**: Verwendet eine In-Memory-HashMap zur Datenspeicherung. Die Daten gehen verloren, wenn der Server neu gestartet wird.
- **apifirst-server-jpa**: Nutzt JPA zur Persistierung der Daten in einer H2-Datenbank. Die Daten bleiben auch nach einem Neustart des Servers erhalten, solange die H2-Datenbankdatei nicht gelöscht wird.

