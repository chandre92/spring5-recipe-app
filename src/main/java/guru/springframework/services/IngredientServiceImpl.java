package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientConverter;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientConverter ingredientConverter;

    private final RecipeRepository recipeRepository;

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientConverter ingredientConverter, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientConverter = ingredientConverter;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        return ingredientConverter.convertToCommand(
                ingredientRepository.findByRecipeIdAndAndId(recipeId, ingredientId)
        );
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        // TODO: 20.07.21 add some default error page
        Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId())
                .orElseThrow();

        Optional<Ingredient> ingredientMatch = recipe.getIngredients().stream()
                .filter(ingredient -> Objects.equals(ingredient.getId(), ingredientCommand.getId()))
                .findFirst();

        if (ingredientMatch.isPresent()) {
            Ingredient ingredient = ingredientMatch.get();
            ingredient.setDescription(ingredientCommand.getDescription());
            ingredient.setAmount(ingredientCommand.getAmount());
            ingredient.setUnitOfMeasure(
                    unitOfMeasureRepository
                            .findById(ingredientCommand.getUnitOfMeasureCommand().getId())
                            .orElseThrow()
            );
        } else {
            recipe.addIngredient(ingredientConverter.convertToDomain(ingredientCommand));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        return ingredientConverter.convertToCommand(savedRecipe.getIngredients().stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(ingredientCommand.getId()))
                .findFirst()
                .orElseThrow());
    }
}
