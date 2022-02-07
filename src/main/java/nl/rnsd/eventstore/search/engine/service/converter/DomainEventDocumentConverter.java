package nl.rnsd.eventstore.search.engine.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import nl.rnsd.eventstore.search.engine.domain.document.DomainEventDocument;
import nl.rnsd.eventstore.search.engine.service.dto.DomainEventDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainEventDocumentConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static DomainEventDto toDomainEventDto(DomainEventDocument domainEventDocument) {
        return DomainEventDto.builder()
            .eventIdentifier(domainEventDocument.getEventIdentifier())
            .aggregateIdentifier(domainEventDocument.getAggregateIdentifier())
            .globalIndex(domainEventDocument.getGlobalIndex())
            .payloadRevision(domainEventDocument.getPayloadRevision())
            .payloadSimpleType(domainEventDocument.getPayloadSimpleType())
            .payloadType(domainEventDocument.getPayloadType())
            .sequenceNumber(domainEventDocument.getSequenceNumber())
            .timeStamp(domainEventDocument.getTimeStamp())
            .payload(getPayload(domainEventDocument))
            .type(domainEventDocument.getType())
            .build();
    }

    @SneakyThrows
    private static String getPayload(DomainEventDocument domainEventDocument) {
        return objectMapper.writeValueAsString(domainEventDocument.getPayload());
    }
}
