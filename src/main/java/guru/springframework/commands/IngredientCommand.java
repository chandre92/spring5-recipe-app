package guru.springframework.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IngredientCommand extends AbstractDescribableCommand {
    private BigDecimal amount;
    private Long recipeId;
    private UnitOfMeasureCommand unitOfMeasureCommand;
}
