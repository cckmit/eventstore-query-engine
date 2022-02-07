package nl.rnsd.eventstore.search.engine.service.search.param;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.annotation.Nonnull;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DomainEventQueryParameters {
    private final String eventIdentifier;
    private final String aggregateIdentifier;
    private final String payloadSimpleType;
    private final String fromDate;
    private final String toDate;
    private final String payloadContent;
    private final String sortOrder;
    @Nonnull
    private final Integer pageNumber;
    @Nonnull
    private final Integer pageSize;
}
