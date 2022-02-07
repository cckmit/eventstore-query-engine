package nl.rnsd.eventstore.search.engine.repository.jpa;

public interface CustomDomainEventEntryRepository {
    void flushAndClear();
}
