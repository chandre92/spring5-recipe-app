package guru.springframework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

@Data
@ToString(exclude = {"recipe"})
@EqualsAndHashCode(exclude = {"recipe"}, callSuper = true)
public class Ingredient extends BaseEntity {
    private String description;
    private BigDecimal amount;

    @DBRef
    private UnitOfMeasure unitOfMeasure;

    public Ingredient() {
    }

    public Ingredient(
            String description,
            BigDecimal amount,
            UnitOfMeasure unitOfMeasure) {
        this.description = description;
        this.amount = amount;
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "description='" + description + '\'' +
                ", amount=" + amount +
                ", unitOfMeasure=" + unitOfMeasure +
                '}';
    }
}
