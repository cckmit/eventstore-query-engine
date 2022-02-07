package nl.rnsd.eventstore.search.engine.service.search;

import nl.rnsd.eventstore.search.engine.service.search.param.DomainEventQueryParameters;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DomainEventQueryBuilderTest {

    public static final String TIME_STAMP_FROM = "2021-11-12 19:01:56";
    public static final String TIME_STAMP_TO = "2022-11-12 19:01:56";

    @Test
    @DisplayName("Creates query based on event metadata, no sort order specified")
    void createsQueryBasedOnMetaDataNoSortOrderSpecified() {
        var queryParameters = DomainEventQueryParameters.builder()
            .eventIdentifier("event-identifier")
            .aggregateIdentifier("aggregate-identifier")
            .pageNumber(1)
            .pageSize(10)
            .build();

        Query query = DomainEventQueryBuilder.constructQuery(queryParameters);

        assertThat(((NativeSearchQuery) query).getElasticsearchSorts()).isEmpty();
        assertThat(((NativeSearchQuery) query).getQuery())
            .hasToString("{\n" +
                "  \"bool\" : {\n" +
                "    \"must\" : [\n" +
                "      {\n" +
                "        \"match_phrase\" : {\n" +
                "          \"eventIdentifier\" : {\n" +
                "            \"query\" : \"event-identifier\",\n" +
                "            \"slop\" : 0,\n" +
                "            \"zero_terms_query\" : \"NONE\",\n" +
                "            \"boost\" : 1.0\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"match_phrase\" : {\n" +
                "          \"aggregateIdentifier\" : {\n" +
                "            \"query\" : \"aggregate-identifier\",\n" +
                "            \"slop\" : 0,\n" +
                "            \"zero_terms_query\" : \"NONE\",\n" +
                "            \"boost\" : 1.0\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"adjust_pure_negative\" : true,\n" +
                "    \"boost\" : 1.0\n" +
                "  }\n" +
                "}");
    }

    @Test
    @DisplayName("Creates query based on event metadata, sort order specified")
    void createsQueryBasedOnMetaDataWithSortOrderSpecified() {
        var queryParameters = DomainEventQueryParameters.builder()
            .eventIdentifier("event-identifier")
            .pageNumber(1)
            .pageSize(10)
            .sortOrder("asc")
            .build();

        Query query = DomainEventQueryBuilder.constructQuery(queryParameters);

        assertThat(((NativeSearchQuery) query).getElasticsearchSorts())
            .isEqualTo(List.of(SortBuilders.fieldSort("timeStamp").order(SortOrder.ASC)));
        assertThat(((NativeSearchQuery) query).getQuery())
            .hasToString("{\n" +
                "  \"bool\" : {\n" +
                "    \"must\" : [\n" +
                "      {\n" +
                "        \"match_phrase\" : {\n" +
                "          \"eventIdentifier\" : {\n" +
                "            \"query\" : \"event-identifier\",\n" +
                "            \"slop\" : 0,\n" +
                "            \"zero_terms_query\" : \"NONE\",\n" +
                "            \"boost\" : 1.0\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"adjust_pure_negative\" : true,\n" +
                "    \"boost\" : 1.0\n" +
                "  }\n" +
                "}");
    }

    @Test
    @DisplayName("Creates query based on attribute search parameters")
    void createsQueryAttributeSearch() {
        var queryParameters = DomainEventQueryParameters.builder()
            .payloadContent("attribute1:value1,attribute2:value2")
            .pageNumber(1)
            .pageSize(10)
            .build();

        Query query = DomainEventQueryBuilder.constructQuery(queryParameters);

        //assert : attribute search parameters used, should use bool query with must-match-phrase clauses
        assertThat(((NativeSearchQuery) query).getQuery())
            .hasToString("{\n" +
                "  \"bool\" : {\n" +
                "    \"must\" : [\n" +
                "      {\n" +
                "        \"match_phrase\" : {\n" +
                "          \"payload.attribute1\" : {\n" +
                "            \"query\" : \"value1\",\n" +
                "            \"slop\" : 0,\n" +
                "            \"zero_terms_query\" : \"NONE\",\n" +
                "            \"boost\" : 1.0\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"match_phrase\" : {\n" +
                "          \"payload.attribute2\" : {\n" +
                "            \"query\" : \"value2\",\n" +
                "            \"slop\" : 0,\n" +
                "            \"zero_terms_query\" : \"NONE\",\n" +
                "            \"boost\" : 1.0\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"adjust_pure_negative\" : true,\n" +
                "    \"boost\" : 1.0\n" +
                "  }\n" +
                "}");
    }


    @Test
    @DisplayName("Creates query based on global search parameters")
    void createsQueryBasedOnGlobalSearchParameter() {
        var queryParameters = DomainEventQueryParameters.builder()
            .payloadContent("globalValue")
            .pageNumber(1)
            .pageSize(10)
            .build();

        Query query = DomainEventQueryBuilder.constructQuery(queryParameters);

        //assert : global search parameter used, should use simple query string
        assertThat(((NativeSearchQuery) query).getQuery())
            .hasToString(
                "{\n" +
                    "  \"bool\" : {\n" +
                    "    \"must\" : [\n" +
                    "      {\n" +
                    "        \"simple_query_string\" : {\n" +
                    "          \"query\" : \"\\\"globalValue\\\"\",\n" +
                    "          \"flags\" : -1,\n" +
                    "          \"default_operator\" : \"or\",\n" +
                    "          \"analyze_wildcard\" : false,\n" +
                    "          \"auto_generate_synonyms_phrase_query\" : true,\n" +
                    "          \"fuzzy_prefix_length\" : 0,\n" +
                    "          \"fuzzy_max_expansions\" : 50,\n" +
                    "          \"fuzzy_transpositions\" : true,\n" +
                    "          \"boost\" : 1.0\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"adjust_pure_negative\" : true,\n" +
                    "    \"boost\" : 1.0\n" +
                    "  }\n" +
                    "}");
    }

    @Test
    @DisplayName("Creates query based on event metadata with correct date format if both from date and to date are provided")
    void createsQueryIncludingDates() {
        var queryParameters = DomainEventQueryParameters.builder()
            .fromDate(TIME_STAMP_FROM)
            .toDate(TIME_STAMP_TO)
            .pageNumber(1)
            .pageSize(10)
            .build();

        Query query = DomainEventQueryBuilder.constructQuery(queryParameters);

        assertThat(((NativeSearchQuery) query).getQuery())
            .hasToString(
                "{\n" +
                    "  \"bool\" : {\n" +
                    "    \"must\" : [\n" +
                    "      {\n" +
                    "        \"range\" : {\n" +
                    "          \"timeStamp\" : {\n" +
                    "            \"from\" : \"2021-12-11T19:01:56Z\",\n" +
                    "            \"to\" : \"2022-12-11T19:01:56Z\",\n" +
                    "            \"include_lower\" : true,\n" +
                    "            \"include_upper\" : true,\n" +
                    "            \"boost\" : 1.0\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"adjust_pure_negative\" : true,\n" +
                    "    \"boost\" : 1.0\n" +
                    "  }\n" +
                    "}");
    }

}