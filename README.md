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
   - erreichbar über port 8081/30081
3. `apifirst-server-jpa`: Erweiterung des Servers mit JPA-Integration
   - Persistiert Daten in einer H2-Datenbank
   - Nutzt ebenfalls die von `apifirst-api` generierten Modelklassen
   - erreichbar über port 8082/30082
4. `apifirst-client`: Client-Implementierung für die API
   - Enthält die generierte Clientschnittstelle
   - Nutzt die von `apifirst-api` generierten Modelklassen und API-Interfaces
   - Ermöglicht das Testen und die Demonstration der API-Funktionalität

Beide Server-Implementierungen (`apifirst-server` und `apifirst-server-jpa`) implementieren die gleiche Schnittstelle, verwenden aber unterschiedliche Datenspeicherungsmechanismen.

### API-Dokumentation und OpenAPI-Definitionen

Die OpenAPI-Definitionen werden im Projekt `apifirst-api` erstellt: https://github.com/dboeckli/apifirst-sb.git
Diese Definitionen durchlaufen einen GitHub-Build-Prozess mit Redocly, wo sie validiert und verarbeitet werden.

Die finale, validierte OpenAPI-Spezifikation war auf Redocly verfügbar. Sie bot eine interaktive und benutzerfreundliche Oberfläche zur Erkundung der API-Spezifikation.
Dies ist nicht der Fall wegen kostenpflichtiger Lizenz. Das openapi yaml wird jetzt per npm dependency geholt (statt wie vorher via url.)

## Generierte Quellen

Um die npm-Abhängigkeiten aufzulösen, muss eine .npmrc-Datei im Home-Verzeichnis platziert werden. Diese Datei enthält den GitHub-Zugriffstoken, der im KeePass-Tresor unter "github dboeckli access token" gespeichert ist.

Basierend auf den OpenAPI-Definitionen werden Quellen generiert, einschließlich der Modelklassen und API-Interfaces. Diese werden in das folgende Verzeichnis geschrieben:

Die generierten Quellen, einschließlich der Modelklassen und API-Interfaces, werden in das folgende Verzeichnis geschrieben:
apifirst-api/target/generated-sources/openapi

Diese generierten Dateien werden automatisch in den Klassenpfad des Projekts aufgenommen und können von anderen Modulen verwendet werden.

## Datenspeicherung

- **apifirst-server**: Verwendet eine In-Memory-HashMap zur Datenspeicherung. Die Daten gehen verloren, wenn der Server neu gestartet wird.
- **apifirst-server-jpa**: Nutzt JPA zur Persistierung der Daten in einer H2-Datenbank. Die Daten bleiben auch nach einem Neustart des Servers erhalten, solange die H2-Datenbankdatei nicht gelöscht wird.

## Deployment with Kubernetes

first which projects you want to deploy.

```bash
cd apifirst-server
```

or

```bash
cd apifirst-server-jpa
```

To run maven filtering for destination target/k8s

```bash
mvn clean install -DskipTests 
```

Deployment goes into the default namespace.

To deploy all resources:

```bash
kubectl apply -f target/k8s/
```

To remove all resources:

```bash
kubectl delete -f target/k8s/
```

Check

```bash
kubectl get deployments -o wide
kubectl get pods -o wide
```

You can use the actuator rest call to verify via port 30081/30082

## Deployment with Helm

Be aware that we are using a different namespace here (not default).

To run maven filtering for destination target/helm

```bash
mvn clean install -DskipTests 
```

first which projects you want to deploy

```bash
cd apifirst-server
$namespace = "apifirst-server"
```

or

```bash
cd apifirst-server-jpa
$namespace = "apifirst-server-jpa"
```

Go to the directory where the tgz file has been created after 'mvn install'

```powershell
cd target/helm/repo
```

unpack

```powershell
$file = Get-ChildItem -Filter *.tgz | Select-Object -First 1
tar -xvf $file.Name
```

install

```powershell
$APPLICATION_NAME = Get-ChildItem -Directory | Where-Object { $_.LastWriteTime -ge $file.LastWriteTime } | Select-Object -ExpandProperty Name
helm upgrade --install $APPLICATION_NAME ./$APPLICATION_NAME --namespace $namespace --create-namespace --wait --timeout 5m --debug
```

show logs

```powershell
kubectl get pods -l app.kubernetes.io/name=$APPLICATION_NAME -n $namespace
```

replace $POD with pods from the command above

```powershell
kubectl logs $POD -n $namespace --all-containers
```

test

```powershell
helm test $APPLICATION_NAME --namespace workflow-hello-world --logs
```

uninstall

```powershell
helm uninstall $APPLICATION_NAME --namespace $namespace
```

delete all

```powershell
kubectl delete all --all -n $namespace
```

create busybox sidecar

```powershell
kubectl run busybox-test --rm -it --image=busybox:1.36 --namespace=$namespace --command -- sh
```

You can use the actuator rest call to verify via port 30081/30082

## Sandbox (local dev environment)

### Start the sandbox (opencode-sandbox-kit)

The sandbox is provisioned by the opencode-sandbox-kit and runs as a Docker container. It mounts this
repo, starts opencode, and connects the IntelliJ MCP server.

Allow the kit source (GitHub without cloning):

```powershell
sbx settings set kit.allowedSources --% "[\"docker.io/\",\"github.com/dboeckli/\"]"
```

Start a new sandbox:

```powershell
sbx run opencode --name apifirst-parent `
    --static-mcp idea `
    --kit "git+https://github.com/dboeckli/opencode-sandbox-kit.git#dir=opencode-agent" `
    -t docker/sandbox-templates:opencode-docker-0.5.0 `
    "C:\development\projects\apifirst-parent" `
    "C:\development\maven-repo:ro"
```

Start the sandbox with Kubernetes support:

```powershell
sbx run opencode --name apifirst-parent `
    --static-mcp idea `
    --kit "git+https://github.com/dboeckli/opencode-sandbox-kit.git#dir=opencode-agent" `
    -t docker/sandbox-templates:opencode-docker-0.5.0 `
    "C:\development\projects\apifirst-parent" `
    "$env:USERPROFILE\.kube:ro" `
    "C:\development\maven-repo:ro"
```

Apply the kit to an existing sandbox (restarts the sandbox, VM state is kept):

```powershell
sbx kit add apifirst-parent "git+https://github.com/dboeckli/opencode-sandbox-kit.git#dir=opencode-agent"
```

### Start the app

Start one of the servers via the IntelliJ run configurations (`ApifirstServerApplication` for
`apifirst-server`, `ApifirstServerApplication JPA` for `apifirst-server-jpa`) or via
`./mvnw spring-boot:run` in the module directory.

`apifirst-client` optionally uses `apifirst-client/compose.yaml` (busybox) when the
`spring-boot-docker-compose` starter is active; the servers need no external services (in-memory /
H2).
