package en.ladislav.finderapi.model;

import en.ladislav.finderapi.utility.Item;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("query")
@Builder
public class Query {
    @Id
    @Column("id")
    private Integer queryId;
    @Column("query")
    private String queryName;
    @Column("avg_price")
    private Double averagePrice;
}
