package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Recipe;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteById(String recipeId, String ingredientId);
}
