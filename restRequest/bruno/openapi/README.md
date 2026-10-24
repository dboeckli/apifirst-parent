# OpenAPI-derived Bruno requests

Die Requests unter `openapi/apifirst/` sind aus der OpenAPI-Spec generiert und werden **nicht**
handgepflegt. Die Spec stammt aus `apifirst-api`: dort wird das npm-Paket
`@dboeckli/apifirst-sb` geholt und mit Redocly nach `apifirst-api/api-def/dist/bundle.yaml`
gebündelt (Maven-Goal `generate-sources`).

|       Ordner       |                  Spec                   |           Quelle            |
|--------------------|-----------------------------------------|-----------------------------|
| `openapi/apifirst` | `apifirst-api/api-def/dist/bundle.yaml` | npm `@dboeckli/apifirst-sb` |

## Regenerieren

```bash
# 1. Spec erzeugen (npm/Redocly im apifirst-api-Modul)
./mvnw -pl apifirst-api generate-sources

# 2. Bruno-Requests neu importieren (überschreibt openapi/apifirst/)
bash restRequest/bruno/tools/sync-openapi.sh
```

Das Skript importiert mit `bru import openapi --group-by path` und ersetzt `{{baseUrl}}` durch
`http://localhost:{{application-port}}`. Die Environments der Collection (`local`,
`local-with-jpa`, `k8s`, `k8s-with-jpa`) liefern den Port.

Alternativ eine bereits veröffentlichte Spec verwenden:

```bash
./mvnw dependency:copy \
  -Dartifact=ch.guru.springframework.apifirst:apifirst-api:<version>:yaml:openapi \
  -DoutputDirectory=target/openapi
bash restRequest/bruno/tools/sync-openapi.sh target/openapi/apifirst-api-<version>-openapi.yaml
```

## Publikation

Der `maven-build`-Workflow lädt die Spec als Artefakt `openapi-spec` hoch. Der Job
`publish-reports` im Deploy-Workflow lädt sie für den jeweiligen Commit herunter und veröffentlicht
sie unter `openapi/apifirst.yaml` und `openapi/apifirst.json` auf GitHub Pages. Zusätzlich wird eine
Swagger-UI-Seite unter `openapi/` generiert (Assets aus `swagger-ui-dist` werden mit ausgeliefert,
kein CDN nötig).

## Ausführen

```bash
cd restRequest/bruno
bru run openapi/apifirst/v1/customers -r --env local
```

Hinweise:

- Pfadparameter sind Bruno-Path-Params (`:customerId`, `:productId`, `:orderId`) und müssen im
  Request gesetzt werden; die Requests sind Endpunkt-Referenzen, keine zusammenhängenden Flows.
- `PATCH`/`DELETE` funktionieren nur mit dem JPA-Profil (`local-with-jpa` = 8082,
  `k8s-with-jpa` = 30082). Anders als die Flows unter `../customer-flow-*` werden diese
  Import-Requests nicht automatisch geskippt.
- Die Requests sind nicht Teil der CI-Flow-Liste; CI prüft nur die generierten Flows.

