package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class IngredientConverterTest {
    private static final BigDecimal AMOUNT = new BigDecimal("1");
    private static final String DESCRIPTION = "Cheeseburger";
    private static final Long ID_VALUE = 1L;
    private static final Long UOM_ID = 2L;
    private static final String UOM_DESCRIPTION = "uom description";

    private Ingredient expectedIngredient;
    private IngredientCommand expectedIngredientCommand;

    @Spy
    private UnitOfMeasureConverter unitOfMeasureConverter;

    @InjectMocks
    private IngredientConverter ingredientConverter;

    @BeforeEach
    void beforeAll() {
        expectedIngredient = new Ingredient();
        expectedIngredient.setId(ID_VALUE);
        expectedIngredient.setAmount(AMOUNT);
        expectedIngredient.setDescription(DESCRIPTION);

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(UOM_ID);
        unitOfMeasure.setDescription(UOM_DESCRIPTION);
        expectedIngredient.setUnitOfMeasure(unitOfMeasure);

        expectedIngredientCommand = new IngredientCommand();
        expectedIngredientCommand.setId(ID_VALUE);
        expectedIngredientCommand.setAmount(AMOUNT);
        expectedIngredientCommand.setDescription(DESCRIPTION);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setDescription(UOM_DESCRIPTION);
        unitOfMeasureCommand.setId(UOM_ID);
        expectedIngredientCommand.setUnitOfMeasureCommand(unitOfMeasureCommand);
    }

    @Test
    void convertToCommand_withNull() {
        // Act  && Assert
        assertNull(ingredientConverter.convertToCommand(null));
    }

    @Test
    void convertToDomain_withNull() {
        // Act  && Assert
        assertNull(ingredientConverter.convertToDomain(null));
    }

    @Test
    void convertToCommand_withEmpty() {
        // Act  && Assert
        assertNotNull(ingredientConverter.convertToCommand(new Ingredient()));
    }

    @Test
    void convertToDomain_withEmpty() {
        // Act  && Assert
        assertNotNull(ingredientConverter.convertToDomain(new IngredientCommand()));
    }

    @Test
    void convertToCommand() {
        // Act
        IngredientCommand command = ingredientConverter.convertToCommand(expectedIngredient);

        // Assert
        assertThat(command.getId()).isEqualTo(expectedIngredient.getId());
        assertThat(command.getAmount()).isEqualTo(expectedIngredient.getAmount());
        assertThat(command.getDescription()).isEqualTo(expectedIngredient.getDescription());
        assertThat(command.getUnitOfMeasureCommand().getDescription())
                .isEqualTo(expectedIngredient.getUnitOfMeasure().getDescription());
        assertThat(command.getUnitOfMeasureCommand().getId())
                .isEqualTo(expectedIngredient.getUnitOfMeasure().getId());
    }

    @Test
    void convertToDomain() {
        // Act
        Ingredient ingredient = ingredientConverter.convertToDomain(expectedIngredientCommand);

        // Assert
        assertThat(ingredient.getId()).isEqualTo(expectedIngredientCommand.getId());
        assertThat(ingredient.getAmount()).isEqualTo(expectedIngredientCommand.getAmount());
        assertThat(ingredient.getDescription()).isEqualTo(expectedIngredientCommand.getDescription());
        assertThat(ingredient.getUnitOfMeasure().getDescription())
                .isEqualTo(expectedIngredientCommand.getUnitOfMeasureCommand().getDescription());
        assertThat(ingredient.getUnitOfMeasure().getId())
                .isEqualTo(expectedIngredientCommand.getUnitOfMeasureCommand().getId());
    }
}