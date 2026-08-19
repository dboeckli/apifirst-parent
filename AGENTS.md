# AGENTS.md

Spring Boot 4 (parent 4.1.0) / Spring Framework 6 API-first project on **Java 25** (enforced by the
maven-enforcer plugin). Multi-module Maven build:

- `apifirst-api` — OpenAPI spec, frontend-maven-plugin (npm) build, OpenAPI generator models, attaches
  the `openapi` yaml artifact (`@project.artifactId@-api`).
- `apifirst-server` — API implementation on an in-memory `HashMap` repository. Namespace
  `apifirst-server`, NodePort `30081`.
- `apifirst-server-jpa` — API implementation with JPA (H2). Namespace `apifirst-server-jpa`,
  NodePort `30082`.
- `apifirst-client` — generated RestTemplate client and API tests.

Both servers implement the same API (`ch.guru.springframework.apifirst.*`) with different storage
backends. Main classes are `ch.guru.springframework.apifirst.apifirstserver.server.ApifirstServerApplication`
and `.jpa.ApifirstServerApplication`.

## Build & test commands

- Full build (format, unit + IT tests, Helm lint/template): `./mvnw clean verify` (add
  `-Dskip.start.stop.springboot=true -Dskip.docker.build=true -Dskip.docker.publish=true` to skip the
  app boot and Docker steps), single test: `./mvnw test -Dtest=...`.
- `./mvnw clean install` additionally builds the Docker images and packages both Helm charts into
  `apifirst-server/target/helm/repo/` and `apifirst-server-jpa/target/helm/repo/`.
- Start locally: `./mvnw spring-boot:run` in the module dir, or the `.run/*.run.xml` configurations
  in IntelliJ (the `* JPA` one for `apifirst-server-jpa`).
- Format fixes: `./mvnw spotless:apply` and `./mvnw spring-javaformat:apply`.

After changing code, verify: run the relevant Maven goal above and report its output (evidence, not
just "done").

## Sandbox build quirk (background)

This sandbox mounts the repo via filesystem passthrough, which blocks symlinks — Spotless's
`npm install` (prettier) would fail with `EPERM` unless npm skips bin links. The sandbox kit sets
`npm_config_bin_links=false` globally (`spec.yaml` → `environment.variables`), so no manual export is
needed here. On a normal host (Windows/CI) this does not apply either.

## Formatting is enforced (fails the `validate` phase)

- Java: Spring Java Format → fix with `./mvnw spring-javaformat:apply`.
- Everything else (pom.xml, `**/*.md`, json, `src/main/resources/application*.yaml`, `**/*.sh`):
  Spotless → fix with `./mvnw spotless:apply`. `AGENTS.md`/`CLAUDE.md` are excluded.
- shfmt is pinned to `3.13.1` (parent pom).

## External dependency gotchas

- `apifirst-api` resolves the OpenAPI spec via a scoped npm package `@dboeckli/…` from GitHub Packages
  (`@dboeckli:registry=https://npm.pkg.github.com/`); a `.npmrc` with a GitHub access token is required.
- The build resolves internal artifacts from GitHub Maven Packages
  (`maven.pkg.github.com`); without a PAT in `~/.m2/settings.xml` (server id `github`) the build cannot
  resolve dependencies.

## Test conventions

- Naming matters: `*Test` = unit (surefire), `*IT` = integration (failsafe). A `*Test` class will not
  run during `verify`'s failsafe phase and vice versa.
- `apifirst-server` uses `@WebMvcTest`-based controller tests; `apifirst-server-jpa` uses
  `@SpringBootTest`/JPA ITs against in-memory H2 (no external database needed).
- A custom `TestClassOrderer` sorts test classes and `LocaleExtension` forces `Locale.US`; both are
  auto-registered via `META-INF/services`. Do not add a global locale again.

## Architecture

- Layered flow per server module: `controller` → `service` (interface + impl) → `repository`.
- `apifirst-api` models are generated once; `apifirst-server`/`apifirst-server-jpa` consume the
  `apifirst-api` dependency.
- API-first: the OpenAPI spec (`apifirst-api/api-def/`) is the single source of truth; controllers map
  DTOs to internal domain objects.

## Deploy / CI

- Deployment is Helm-only: charts in `helm-charts/`, packaged to `target/helm/repo/<artifactId>-chart-<version>.tgz`,
  release name = `<artifactId>` (charts use `fullnameOverride` so Service/Deployment names are
  deterministic). See `.run/*.run.xml` (PowerShell) for local deploy/uninstall.
- CI (`.github/workflows/`): `maven-build.yml` builds + deploys snapshots and triggers
  `deploy-and-test-cluster.yml` per deployable module; `release.yml` runs `mvn release:prepare
  release:perform` on main/master only (version must be `-SNAPSHOT`).
- Dependency updates are managed via `.github/dependabot.yml` and `.github/renovate.json`; validate
  changes with `renovate-config-validator .github/renovate.json`.