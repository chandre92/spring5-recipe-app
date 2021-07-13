package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IngredientConverter implements TwoWaysConverter<IngredientCommand, Ingredient> {
    private final UnitOfMeasureConverter unitOfMeasureConverter;

    public IngredientConverter(UnitOfMeasureConverter unitOfMeasureConverter) {
        this.unitOfMeasureConverter = unitOfMeasureConverter;
    }

    @Override
    public IngredientCommand convertToCommand(Ingredient source) {
        if (source == null) {
            return null;
        }

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(source.getId());
        ingredientCommand.setAmount(source.getAmount());
        ingredientCommand.setDescription(source.getDescription());
        ingredientCommand.setUnitOfMeasureCommand(unitOfMeasureConverter.convertToCommand(source.getUnitOfMeasure()));
        // TODO: 13.07.21 cover by test
        if (source.getRecipe() != null) {
            ingredientCommand.setRecipeId(source.getRecipe().getId());
        }

        return ingredientCommand;
    }

    @Override
    public Ingredient convertToDomain(IngredientCommand source) {
        if (source == null) {
            return null;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUnitOfMeasure(unitOfMeasureConverter.convertToDomain(source.getUnitOfMeasureCommand()));

        return ingredient;
    }
}
