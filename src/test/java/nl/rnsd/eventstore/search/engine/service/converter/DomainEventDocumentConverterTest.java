package nl.rnsd.eventstore.search.engine.service.converter;

import nl.rnsd.eventstore.search.engine.domain.document.DomainEventDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DomainEventDocumentConverterTest {

    public static final String EVENT_IDENTIFIER = UUID.randomUUID().toString();
    public static final String AGGREGATE_IDENTIFIER = UUID.randomUUID().toString();
    public static final String GLOBAL_INDEX = "110";
    public static final String PAYLOAD_TYPE = "a.random.payloadtype";
    public static final String PAYLOAD_SIMPLE_TYPE = "type";
    public static final String SEQUENCE_NUMBER = "2.0";
    public static final String TYPE = "type";
    public static final String TIME_STAMP = "2021-11-13T19:01:56.348870268Z";
    public static final String PAYLOAD_REVISION = "5.0";

    @Test
    @DisplayName("Converts domain event document to dto")
    void convertsDocumentToDto() {
        var domainEventDocument = DomainEventDocument.builder()
            .eventIdentifier(EVENT_IDENTIFIER)
            .aggregateIdentifier(AGGREGATE_IDENTIFIER)
            .globalIndex(GLOBAL_INDEX)
            .payloadType(PAYLOAD_TYPE)
            .payloadSimpleType(PAYLOAD_SIMPLE_TYPE)
            .sequenceNumber(SEQUENCE_NUMBER)
            .type(TYPE)
            .timeStamp(TIME_STAMP)
            .payloadRevision(PAYLOAD_REVISION)
            .payload(Map.of("attr1", "value1"))
            .build();

        var domainEventDto = DomainEventDocumentConverter.toDomainEventDto(domainEventDocument);

        assertThat(domainEventDto.getEventIdentifier()).isEqualTo(EVENT_IDENTIFIER);
        assertThat(domainEventDto.getAggregateIdentifier()).isEqualTo(AGGREGATE_IDENTIFIER);
        assertThat(domainEventDto.getGlobalIndex()).isEqualTo(GLOBAL_INDEX);
        assertThat(domainEventDto.getPayloadType()).isEqualTo(PAYLOAD_TYPE);
        assertThat(domainEventDto.getPayloadSimpleType()).isEqualTo(PAYLOAD_SIMPLE_TYPE);
        assertThat(domainEventDto.getSequenceNumber()).isEqualTo(SEQUENCE_NUMBER);
        assertThat(domainEventDto.getType()).isEqualTo(TYPE);
        assertThat(domainEventDto.getTimeStamp()).isEqualTo(TIME_STAMP);
        assertThat(domainEventDto.getPayloadRevision()).isEqualTo(PAYLOAD_REVISION);
        assertThat(domainEventDto.getPayload()).isEqualTo("{\"attr1\":\"value1\"}");
    }
}