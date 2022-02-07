package nl.rnsd.eventstore.search.engine.repository.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//TODO zonder deze repo, em injecteren
public class CustomDomainEventEntryRepositoryImpl implements CustomDomainEventEntryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
