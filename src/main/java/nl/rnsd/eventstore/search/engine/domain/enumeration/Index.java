package nl.rnsd.eventstore.search.engine.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents ES indices.
 */
@RequiredArgsConstructor
@Getter
public enum Index {
    DOMAIN_EVENT("domain-event");

    private final String name;
}
