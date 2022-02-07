package nl.rnsd.eventstore.search.engine.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Map;

/**
 * DomainEvent as represented in ElasticSearch.
 */
@Document(indexName = "domain-event", shards = 4)
@Setting(settingPath = "elasticsearch/event_name_analyzer.json")
@Builder
@Getter
@AllArgsConstructor
public class DomainEventDocument {

    @Id
    private final String eventIdentifier;

    private final String payloadRevision;

    private final String payloadType;

    private final String timeStamp;

    private final String aggregateIdentifier;

    private final String sequenceNumber;

    private final String type;

    private final String globalIndex;

    @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
    private final String payloadSimpleType;

    private final Map<String, String> payload;
}
