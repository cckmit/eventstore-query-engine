package nl.rnsd.eventstore.search.engine.service.sync;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rnsd.eventstore.search.engine.domain.document.DomainEventDocument;
import nl.rnsd.eventstore.search.engine.domain.entity.DomainEvent;
import nl.rnsd.eventstore.search.engine.domain.enumeration.Index;
import nl.rnsd.eventstore.search.engine.repository.es.ElasticSearchEventRepository;
import nl.rnsd.eventstore.search.engine.repository.jpa.CustomDomainEventEntryRepositoryImpl;
import nl.rnsd.eventstore.search.engine.repository.jpa.DomainEventEntryRepository;
import nl.rnsd.eventstore.search.engine.service.converter.DomainEventConverter;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Synchronizes the most recent state of the event store with the ES index.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventStoreSynchronizer {

    public static final int BATCH_SIZE = 5_000;

    private final RestHighLevelClient restHighLevelClient;
    private final DomainEventEntryRepository domainEventEntryRepository;
    private final ElasticSearchEventRepository elasticSearchEventRepository;
    private final CustomDomainEventEntryRepositoryImpl customDomainEventEntryRepository;

    /**
     * Synchronizes event store. Create the ES index (if it does not yet exist) and updates the index in batches.
     *
     * @throws IOException in case of IO errors
     */
    @Transactional
    public void sync() throws IOException {
        if (!indexExists(Index.DOMAIN_EVENT.getName())) {
            createDomainEventIndex();
        }

        synchronizeEventStore();
    }

    private void synchronizeEventStore() {
        Pageable pageRequest = PageRequest.of(0, BATCH_SIZE);
        Page<DomainEvent> page = domainEventEntryRepository.findAll(pageRequest);
        while (!page.isEmpty()) {
            log.info("Indexing batch of domainEventEntries, pageRequest nr = {} of {}", pageRequest.getPageNumber(), page.getTotalPages());

            List<DomainEventDocument> domainEventDocuments = page.stream()
                .map(DomainEventConverter::toDomainEventDocument)
                .collect(toList());
            elasticSearchEventRepository.saveAll(domainEventDocuments);

            customDomainEventEntryRepository.flushAndClear(); //needed because of memory leak :-(
            pageRequest = pageRequest.next();
            page = domainEventEntryRepository.findAll(pageRequest);
        }
    }

    private void createDomainEventIndex() throws IOException {
        restHighLevelClient.indices().create(new CreateIndexRequest(Index.DOMAIN_EVENT.getName()), RequestOptions.DEFAULT);
        PutMappingRequest putMappingRequest = new PutMappingRequest(Index.DOMAIN_EVENT.getName());
        putMappingRequest.source("{\"date_detection\": false}", XContentType.JSON);
        restHighLevelClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
    }

    private boolean indexExists(String indexName) throws IOException {
        return restHighLevelClient.indices()
            .exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
    }
}
