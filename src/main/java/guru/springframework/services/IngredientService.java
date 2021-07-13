package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Recipe;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}
