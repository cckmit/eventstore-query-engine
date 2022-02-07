package nl.rnsd.eventstore.search.engine.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.math.BigInteger;

/**
 * DomainEvent as persisted in the event store.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainEvent {

    @Id
    @Column(name = "event_identifier")
    private String id;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "meta_data")
    private byte[] metaData;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "payload")
    private byte[] payload;

    private String payloadRevision;

    private String payloadType;

    private String timeStamp;

    private String aggregateIdentifier;

    private BigInteger sequenceNumber;

    private BigInteger globalIndex;

    private String type;

}
