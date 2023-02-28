package en.ladislav.finderapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("scheduler")
@Builder
@NoArgsConstructor
public class Subscribe {
    @Id
    @Column("id")
    private Integer id;
    @Column("item_id")
    private String itemId;
    @Column("user_id")
    private String userId;
}
