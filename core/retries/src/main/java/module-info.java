module software.amazon.awssdk.retries {
    exports software.amazon.awssdk.retries;
    exports software.amazon.awssdk.retries.standard;
    requires transitive software.amazon.awssdk.retriesapi;
    requires transitive software.amazon.awssdk.core;
    requires software.amazon.awssdk.annotations;
    requires software.amazon.awssdk.utils;
}
