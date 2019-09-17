package en.ladislav.finderapi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "QUERY")
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint", nullable = false)
    private long queryId;

//    private String
}
