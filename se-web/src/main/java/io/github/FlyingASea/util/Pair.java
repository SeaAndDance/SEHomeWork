package io.github.FlyingASea.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Pair<C1, C2>(@JsonProperty("first") C1 first, @JsonProperty("second") C2 second) {
}
