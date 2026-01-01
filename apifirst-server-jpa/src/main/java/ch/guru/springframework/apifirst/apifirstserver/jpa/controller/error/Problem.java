package ch.guru.springframework.apifirst.apifirstserver.jpa.controller.error;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * RFC 7807 "Problem Details" Modell fuer API-Fehlerresponses.
 *
 * <p>
 * Diese Klasse repraesentiert das standardisierte Problem-Detail-Objekt gemäss
 * RFC 7807 (Problem Details for HTTP APIs) und ist das Runtime-Pendant zur
 * OpenAPI-Definition {@code components/schemas/Problem} aus:
 * {@code ..\apifirst-client\target\openapi\openapi.yaml}.
 * </p>
 *
 * <p>
 * Verwendet als Payload fuer Responses mit dem Media Type
 * {@code application/problem+json} (z. B. bei 400/404/409).
 * </p>
 *
 * <p>
 * Abgebildet werden die RFC-Standardfelder:
 * {@code type}, {@code title}, {@code status}, {@code detail}, {@code instance}.
 * Nicht gesetzte Felder werden dank {@code @JsonInclude(NON_NULL)} nicht serialisiert.
 * </p>
 *
 * <p>
 * Zusaetzlich werden RFC-konforme "Extension Members" unterstuetzt:
 * beliebige Zusatzattribute koennen dynamisch via {@link #put(String, Object)}
 * hinzugefuegt werden. Diese Erweiterungen werden gemäss OpenAPI
 * {@code additionalProperties: true} direkt als Top-Level-Properties serialisiert.
 * </p>
 *
 * <p>
 * Hinweis: {@code type} ist in RFC 7807 optional; falls nicht gesetzt, gilt
 * semantisch {@code about:blank}. Diese Klasse laesst {@code type} daher null zu.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {

    @Getter
    private URI type;
    @Getter
    private String title;
    @Getter
    private Integer status;
    @Getter
    private String detail;
    @Getter
    private String instance;

    private final Map<String, Object> extensions = new LinkedHashMap<>();

    public Problem() {
    }

    public static Problem of(URI type, String title, int status, String detail, String instance) {
        Problem p = new Problem();
        p.type = type;
        p.title = title;
        p.status = status;
        p.detail = detail;
        p.instance = instance;
        return p;
    }

    @JsonAnySetter
    public void put(String key, Object value) {
        extensions.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getExtensions() {
        return extensions;
    }
}