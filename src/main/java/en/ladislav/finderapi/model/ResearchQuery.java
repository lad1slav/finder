package en.ladislav.finderapi.model;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResearchQuery {
    Long chatId;
    String query;
    String localDateTime;
    Integer messageToDel;
}
