package nl.rnsd.eventstore.search.engine.repository.jpa;

import nl.rnsd.eventstore.search.engine.domain.entity.DomainEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface DomainEventEntryRepository extends CrudRepository<DomainEvent, BigInteger>, CustomDomainEventEntryRepository {

    Page<DomainEvent> findAll(Pageable pageRequest);
}
