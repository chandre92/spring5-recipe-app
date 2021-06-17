package guru.springframework.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand extends AbstractDescribableCommand {
    private BigDecimal amount;
    private UnitOfMeasureCommand unitOfMeasureCommand;
}
