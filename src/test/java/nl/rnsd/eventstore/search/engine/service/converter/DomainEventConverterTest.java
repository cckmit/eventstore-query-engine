package nl.rnsd.eventstore.search.engine.service.converter;

import nl.rnsd.eventstore.search.engine.domain.entity.DomainEvent;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DomainEventConverterTest {

    private static final String TIME_STAMP = "2021-11-13T19:01:56.348870268Z";
    private static final String AGGREGATE_IDENTIFIER = UUID.randomUUID().toString();
    private static final BigInteger SEQUENCE_NUMBER = new BigInteger("1");
    private static final String PAYLOAD_TYPE = "a.payload.type";
    private static final String TYPE = "aggregateType";
    private static final BigInteger GLOBAL_INDEX = new BigInteger("12700");
    private static final String EVENT_IDENTIFIER = UUID.randomUUID().toString();
    private static final Integer AMOUNT = 1;
    private static final String START_DATE = "[2000,11,12]";
    private static final String CONTACT_NAME = "Foo Bar";
    private static final String CONTACT_EMAIL = "foo.bar@somewhere.io";

    @Test
    @DisplayName("Converts domain event entity to document")
    void convertsEntityToDocument() {
        var domainEvent = DomainEvent.builder()
            .id(EVENT_IDENTIFIER)
            .metaData(null)
            .payload(getPayload())
            .timeStamp(TIME_STAMP)
            .aggregateIdentifier(AGGREGATE_IDENTIFIER)
            .sequenceNumber(SEQUENCE_NUMBER)
            .payloadType(PAYLOAD_TYPE)
            .type(TYPE)
            .globalIndex(GLOBAL_INDEX)
            .build();

        var domainEventDocument = DomainEventConverter.toDomainEventDocument(domainEvent);

        assertThat(domainEventDocument.getEventIdentifier()).isEqualTo(domainEvent.getId());
        assertThat(domainEventDocument.getAggregateIdentifier()).isEqualTo(AGGREGATE_IDENTIFIER);
        assertThat(domainEventDocument.getSequenceNumber()).isEqualTo(SEQUENCE_NUMBER.toString());
        assertThat(domainEventDocument.getPayloadType()).isEqualTo(PAYLOAD_TYPE);
        assertThat(domainEventDocument.getPayloadSimpleType()).isEqualTo("type");
        assertThat(domainEventDocument.getType()).isEqualTo(TYPE);
        assertThat(domainEventDocument.getGlobalIndex()).isEqualTo(GLOBAL_INDEX.toString());
        assertThat(domainEventDocument.getTimeStamp()).isEqualTo(TIME_STAMP);
        assertThat(domainEventDocument.getPayload()).containsExactlyInAnyOrderEntriesOf(
            Map.of(
                "id", EVENT_IDENTIFIER,
                "amount", AMOUNT.toString(),
                "startDate", START_DATE,
                "endDate", "null",
                "contactName", CONTACT_NAME,
                "contactEmail", CONTACT_EMAIL
            )
        );
    }


    public static byte[] getPayload() {
        String jsonPayload = String.format(
            "{\"id\":\"%s\"," +
                "\"amount\":%d," +
                "\"startDate\":%s," +
                "\"endDate\":null," +
                "\"contactName\":\"%s\"," +
                "\"contactEmail\":\"%s\"}",
            EVENT_IDENTIFIER,
            AMOUNT,
            START_DATE,
            CONTACT_NAME,
            CONTACT_EMAIL
        );
        String hexString = Hex.encodeHexString(jsonPayload.getBytes(/* charset */));
        return DatatypeConverter.parseHexBinary(hexString);
    }

}