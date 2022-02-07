package nl.rnsd.eventstore.search.engine.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DomainEventDto {

    private String eventIdentifier;

    private String payloadRevision;

    private String payloadType;

    private String timeStamp;

    private String aggregateIdentifier;

    private String sequenceNumber;

    private String type;

    private String globalIndex;

    private String payloadSimpleType;

    private String payload;
}
