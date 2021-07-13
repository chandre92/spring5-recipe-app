package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientConverter;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final IngredientConverter ingredientConverter;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientConverter ingredientConverter) {
        this.recipeRepository = recipeRepository;
        this.ingredientConverter = ingredientConverter;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        // TODO: 13.07.21 reimpl - it is very shitty impl. Try to imp repo for ingredients
        // TODO: 13.07.21 with search by two fields
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isEmpty()) {
            log.error("recipe id not found: " + recipeId);
        }

        Recipe recipe = optionalRecipe.get();

        Optional<IngredientCommand> optionalIngredientCommand = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientConverter::convertToCommand)
                .findFirst();

        if (optionalIngredientCommand.isEmpty()) {
            log.error("ingredient id not found: " + ingredientId);
        }

        return optionalIngredientCommand.get();
    }
}
