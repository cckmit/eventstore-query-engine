package nl.rnsd.eventstore.search.engine.service.search.param;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PayloadContentSearchParametersTest {

    @Test
    @DisplayName("constructs parameters from search string with global search parameter")
    void constructsParamsFromSearchStringWithGlobalSearchParameter() {
        var searchParameters = PayloadContentSearchParameters.fromPayloadTypeSearchString("foo");

        assertThat(searchParameters.getAttributeSearchParams()).isNull();
        assertThat(searchParameters.getGlobalSearchParameter()).isEqualTo("foo");
    }

    @Test
    @DisplayName("constructs parameters from search string with attribute search parameters")
    void constructsParamsFromSearchStringWithAttributeSearchParameters() {
        var searchParameters = PayloadContentSearchParameters.fromPayloadTypeSearchString("foo:bar, bar:foo");

        assertThat(searchParameters.getGlobalSearchParameter()).isNull();
        assertThat(searchParameters.getAttributeSearchParams())
            .containsExactlyInAnyOrderEntriesOf(
                Map.of(
                    "foo", "bar",
                    "bar", "foo"
                )
            );
    }
}