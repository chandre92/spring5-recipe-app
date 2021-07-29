package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import org.springframework.stereotype.Component;

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

        // TODO: 29.07.21 do we really need it? Looks like no but need to test
        // TODO: 29.07.21 if need - cover by the unit test!
/*        if (source.getRecipeId() != null) {
            Recipe recipe = new Recipe();
            recipe.setId(source.getRecipeId());
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }*/

        ingredient.setId(source.getId());
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUnitOfMeasure(unitOfMeasureConverter.convertToDomain(source.getUnitOfMeasureCommand()));

        return ingredient;
    }
}
