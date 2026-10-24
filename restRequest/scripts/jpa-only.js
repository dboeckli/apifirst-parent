// JPA-only requests (PATCH/DELETE and their verification) are skipped by the
// Bruno runner when the selected environment is not a JPA profile
// (see environments/*.yml).
// IntelliJ has no `bru` global; the guard short-circuits there.
if (typeof bru !== "undefined" && bru.runner && bru.getEnvVar("profile") !== "jpa") {
    bru.runner.skipRequest();
}
