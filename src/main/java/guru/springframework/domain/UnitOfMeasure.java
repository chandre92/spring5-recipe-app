package guru.springframework.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document
public class UnitOfMeasure extends BaseEntity {
    private String description;
}
