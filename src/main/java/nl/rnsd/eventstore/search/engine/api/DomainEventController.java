package nl.rnsd.eventstore.search.engine.api;

import lombok.RequiredArgsConstructor;
import nl.rnsd.eventstore.search.engine.service.dto.DomainEventSearchResultDto;
import nl.rnsd.eventstore.search.engine.service.search.DomainEventService;
import nl.rnsd.eventstore.search.engine.service.search.param.DomainEventQueryParameters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DomainEventController {

    private final DomainEventService domainEventService;

    @GetMapping("/events")
    public ResponseEntity<DomainEventSearchResultDto> searchEvents(@RequestParam(required = false) String eventIdentifier,
                                                                   @RequestParam(required = false) String aggregateIdentifier,
                                                                   @RequestParam(required = false) String payloadSimpleType,
                                                                   @RequestParam(required = false) String fromDate,
                                                                   @RequestParam(required = false) String toDate,
                                                                   @RequestParam(required = false) String payloadContent,
                                                                   @RequestParam String sortOrder,
                                                                   @RequestParam Integer pageNumber,
                                                                   @RequestParam Integer pageSize) {

        DomainEventSearchResultDto searchResult = domainEventService.search(
            DomainEventQueryParameters.builder()
                .eventIdentifier(eventIdentifier)
                .aggregateIdentifier(aggregateIdentifier)
                .payloadSimpleType(payloadSimpleType)
                .fromDate(fromDate)
                .toDate(toDate)
                .payloadContent(payloadContent)
                .sortOrder(sortOrder)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build());

        return ResponseEntity.ok(searchResult);
    }


}
