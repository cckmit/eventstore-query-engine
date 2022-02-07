package nl.rnsd.eventstore.search.engine.repository.es;

import nl.rnsd.eventstore.search.engine.domain.document.DomainEventDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticSearchEventRepository extends ElasticsearchRepository<DomainEventDocument, String> {

    @Query("{\"filter\":{\"match_all\":{ }},\"sort\":[{\"timeStamp\":{\"order\":\"desc\"}}],\"size\": 1}")
    DomainEventDocument findByLatestTimestamp();

    List<DomainEventDocument> findByPayloadSimpleType(String payloadSimpleType);

    DomainEventDocument findByEventIdentifier(String eventIdentifier);


}