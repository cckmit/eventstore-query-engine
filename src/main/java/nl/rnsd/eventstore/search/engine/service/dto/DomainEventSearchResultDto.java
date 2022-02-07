package nl.rnsd.eventstore.search.engine.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DomainEventSearchResultDto {

    long totalNumberOfSearchHits;

    List<DomainEventDto> domainEvents;
}
