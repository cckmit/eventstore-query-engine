package nl.rnsd.eventstore.search.engine.service.search.param;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.annotation.Nullable;
import java.util.Arrays;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PayloadContentSearchParameters {

    private final ImmutableMap<String, String> attributeSearchParams;

    private final String globalSearchParameter;

    /**
     * Constructs {@link PayloadContentSearchParameters} from a given payload type search string.
     * A payload type search string consists of EITHER a comma-separated string of attribute name-value pairs, e.g. "attr1:value, attr2:value ..." OR
     * a single value. TODO validate input in controller
     * @param searchString the payload type search string
     * @return the payload content search parameters
     */
    public static PayloadContentSearchParameters fromPayloadTypeSearchString(String searchString) {
        String[] entries = searchString.replace("\"", "").trim().split(",");
        if (entries[0].contains(":")) {
            ImmutableMap<String, String> attributeSearchParams = Arrays.stream(entries)
                    .collect(toImmutableMap(
                        entry -> entry.substring(0, entry.indexOf(":")).trim(), //get attribute name
                        entry -> entry.substring(entry.indexOf(":") + 1).trim()) //get attribute value
                    );
            return new PayloadContentSearchParameters(attributeSearchParams, null);
        }

        return new PayloadContentSearchParameters(null, entries[0]);
    }

    @Nullable
    public ImmutableMap<String, String> getAttributeSearchParams() {
        return attributeSearchParams;
    }

    @Nullable
    public String getGlobalSearchParameter() {
        return globalSearchParameter;
    }

}
