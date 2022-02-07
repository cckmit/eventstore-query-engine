package nl.rnsd.eventstore.search.engine.service.converter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.rnsd.eventstore.search.engine.domain.document.DomainEventDocument;
import nl.rnsd.eventstore.search.engine.domain.entity.DomainEvent;

import javax.xml.bind.DatatypeConverter;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainEventConverter {

    /**
     * Converts the database representation of an event to an elastic search document representation.
     */
    public static DomainEventDocument toDomainEventDocument(DomainEvent event) {
        return DomainEventDocument.builder()
                .eventIdentifier(event.getId())
                .payload(getPayloadByAttribute(event.getPayload()))
                .payloadRevision(event.getPayloadRevision())
                .payloadType(event.getPayloadType())
                .timeStamp(event.getTimeStamp())
                .aggregateIdentifier(event.getAggregateIdentifier())
                .sequenceNumber(event.getSequenceNumber().toString())
                .type(event.getType())
                .globalIndex(event.getGlobalIndex().toString())
                .payloadSimpleType(removeQualifier(event.getPayloadType()))
                .build();
    }

    /**
     * Strips the package qualifier of the payload type.
     */
    private static String removeQualifier(String payloadType) {
        return payloadType.substring(payloadType.lastIndexOf(".") + 1);
    }

    private static Map<String, String> getPayloadByAttribute(byte[] payload) {
        String payloadAsHexBinary = DatatypeConverter.printHexBinary(payload);
        String payloadJsonString = hexToAscii(payloadAsHexBinary);
        JsonObject payloadJson = new JsonParser().parse(payloadJsonString).getAsJsonObject();
        return payloadJson.entrySet()
                .stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().toString().replace("\"", "")));
    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

}
