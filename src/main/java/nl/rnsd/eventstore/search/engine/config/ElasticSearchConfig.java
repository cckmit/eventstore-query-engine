package nl.rnsd.eventstore.search.engine.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Bean
    public ElasticsearchOperations elasticSearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }

    @Override
    @Nonnull
    @SuppressWarnings("squid:S2095")
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Override
    @Nonnull
    protected Collection<String> getMappingBasePackages() {
        return List.of("nl.mlbrg.es.search.eventstore");
    }


}
