package nl.rnsd.eventstore.search.engine.service.search;

import lombok.RequiredArgsConstructor;
import nl.rnsd.eventstore.search.engine.domain.document.DomainEventDocument;
import nl.rnsd.eventstore.search.engine.domain.enumeration.Index;
import nl.rnsd.eventstore.search.engine.service.dto.DomainEventDto;
import nl.rnsd.eventstore.search.engine.service.dto.DomainEventSearchResultDto;
import nl.rnsd.eventstore.search.engine.service.search.param.DomainEventQueryParameters;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static nl.rnsd.eventstore.search.engine.service.converter.DomainEventDocumentConverter.toDomainEventDto;
import static nl.rnsd.eventstore.search.engine.service.search.DomainEventQueryBuilder.constructQuery;

@Service
@RequiredArgsConstructor
public class DomainEventService {

    private final ElasticsearchOperations elasticsearchTemplate;

    public DomainEventSearchResultDto search(DomainEventQueryParameters queryParameters) {
        Query query = constructQuery(queryParameters);
        SearchHits<DomainEventDocument> domainEventSearchHits = elasticsearchTemplate.search(
            query,
            DomainEventDocument.class,
            IndexCoordinates.of(Index.DOMAIN_EVENT.getName()));
        return createSearchResult(domainEventSearchHits);
    }


    private static DomainEventSearchResultDto createSearchResult(SearchHits<DomainEventDocument> domainEventSearchHits) {
        List<DomainEventDto> domainEvents = domainEventSearchHits.get()
            .map(hit -> toDomainEventDto(hit.getContent()))
            .collect(toList());

        return DomainEventSearchResultDto.builder()
            .totalNumberOfSearchHits(domainEventSearchHits.getTotalHits())
            .domainEvents(domainEvents)
            .build();
    }


}
