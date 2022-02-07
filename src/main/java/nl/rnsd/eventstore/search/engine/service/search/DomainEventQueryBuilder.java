package nl.rnsd.eventstore.search.engine.service.search;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.rnsd.eventstore.search.engine.service.search.param.DomainEventQueryParameters;
import nl.rnsd.eventstore.search.engine.service.search.param.PayloadContentSearchParameters;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.requireNonNull;
import static org.elasticsearch.common.Strings.isNullOrEmpty;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainEventQueryBuilder {

    private static final String TIME_STAMP = "timeStamp";
    private static final String PAYLOAD_SIMPLE_TYPE = "payloadSimpleType";
    private static final String AGGREGATE_IDENTIFIER = "aggregateIdentifier";
    private static final String EVENT_IDENTIFIER = "eventIdentifier";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");

    public static Query constructQuery(DomainEventQueryParameters queryParams) {
        BoolQueryBuilder boolQueryBuilder =
            createBoolQueryBuilder(
                queryParams.getEventIdentifier(),
                queryParams.getAggregateIdentifier(),
                queryParams.getPayloadSimpleType(),
                queryParams.getFromDate(),
                queryParams.getToDate(),
                queryParams.getPayloadContent());

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQueryBuilder)
            .withPageable(PageRequest.of(queryParams.getPageNumber(), queryParams.getPageSize()));
        if (!isNullOrEmpty(queryParams.getSortOrder())) {
            searchQuery.withSorts(SortBuilders.fieldSort(TIME_STAMP)
                .order(queryParams.getSortOrder().equals("asc") ? SortOrder.ASC : SortOrder.DESC)); //TODO validate "asc/desc" value?
        }

        return searchQuery.build();
    }

    private static BoolQueryBuilder createBoolQueryBuilder(String eventIdentifier,
                                                           String aggregateIdentifier,
                                                           String payloadSimpleType,
                                                           String fromDate,
                                                           String toDate,
                                                           String payloadContent) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (!isNullOrEmpty(eventIdentifier)) {
            boolQueryBuilder.must(matchPhraseQuery(EVENT_IDENTIFIER, eventIdentifier));
        }
        if (!isNullOrEmpty(aggregateIdentifier)) {
            boolQueryBuilder.must(matchPhraseQuery(AGGREGATE_IDENTIFIER, aggregateIdentifier));
        }
        if (!isNullOrEmpty(payloadSimpleType)) {
            boolQueryBuilder.must(matchPhraseQuery(PAYLOAD_SIMPLE_TYPE, payloadSimpleType.substring(0, Math.min(payloadSimpleType.length(), 19))));
        }
        if (!isNullOrEmpty(fromDate) && !isNullOrEmpty(toDate)) {
            boolQueryBuilder.must(rangeQuery(TIME_STAMP).gte(formatDateString(fromDate)).lte(formatDateString(toDate)));
        }
        if (!isNullOrEmpty(payloadContent)) {
            PayloadContentSearchParameters payloadContentSearchParameters = PayloadContentSearchParameters.fromPayloadTypeSearchString(payloadContent);
            if (payloadContentSearchParameters.getAttributeSearchParams() != null) {
                //search by attribute
                requireNonNull(payloadContentSearchParameters.getAttributeSearchParams())
                    .forEach((key, value) -> boolQueryBuilder.must(matchPhraseQuery("payload." + key, value)));
            } else {
                //global search
                boolQueryBuilder.must(simpleQueryStringQuery("\"" + payloadContentSearchParameters.getGlobalSearchParameter() + "\""));
            }
        }
        return boolQueryBuilder;
    }

    private static String formatDateString(String fromDate) {
        return ZonedDateTime.parse(fromDate, DATE_TIME_FORMATTER.withZone(ZoneOffset.UTC)).toString();
    }

}
