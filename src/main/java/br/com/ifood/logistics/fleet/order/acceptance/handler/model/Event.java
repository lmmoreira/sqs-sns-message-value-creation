package br.com.ccrs.logistics.fleet.order.acceptance.handler.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.difference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Event representation.
 *
 */
public final class Event {

    public enum EventType {

        SATURATED_REGION("REGION_ID", "REGION_UUID", "ORDER_COUNT", "WORKER_COUNT"),
        ORDER_STATE_CHANGE("ORDER_ID", "CURRENT_ORDER_STATE", "ORDER_EXTERNAL_ID", "ORDER_UUID"),
        SATURATED_OFFLINE_REGION("REGION_ID", "REGION_UUID", "ORDER_COUNT", "WORKER_COUNT"),
        NORMALIZED_REGION("REGION_ID", "REGION_UUID"),
        NORMALIZED_OFFLINE_REGION("REGION_ID", "REGION_UUID");

        private Set<String> parameterKeys;

        EventType(final String... parameterKeys) {
            this.parameterKeys = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(parameterKeys)));
        }

        public Set<String> getParameterKeys() {
            return parameterKeys;
        }
    }

    private final EventType eventType;
    private final Map<String, String> parameters;

    // lazy properties
    @JsonIgnore
    private String toString;

    @JsonCreator
    public Event(@JsonProperty("eventType") final EventType eventType,
            @JsonProperty("parameters") final Map<String, String> parameters) {
        this.eventType = Objects.requireNonNull(eventType);
        this.parameters = ImmutableMap.copyOf(parameters);

        checkArgument(difference(eventType.getParameterKeys(), parameters.keySet()).isEmpty(),
            "There are missing event type parameters");
        checkArgument(difference(parameters.keySet(), eventType.getParameterKeys()).isEmpty(),
            "There are too many event parameters");
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getParameter(final String key) {
        return parameters.get(key);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Event)) {
            return false;
        }
        final Event castOther = (Event) other;
        return eventType == castOther.eventType && Objects.equals(parameters, castOther.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, parameters);
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = MoreObjects.toStringHelper(getClass())
                    .add("eventType", eventType)
                    .add("parameters", parameters)
                    .toString();
        }
        return toString;
    }

}
