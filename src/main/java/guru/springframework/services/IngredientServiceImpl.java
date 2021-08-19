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
    public static final String NO_RECIPE_MESSAGE = "Recipe with id %s doesn't exits";
    public static final String NO_INGREDIENT_IN_RECIPE_MESSAGE = "Recipe doesn't have ingredient with id %s";

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    private final IngredientConverter ingredientConverter;


    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientConverter ingredientConverter, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientConverter = ingredientConverter;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        return ingredientConverter.convertToCommand(
                ingredientRepository.findByRecipeIdAndId(recipeId, ingredientId)
        );
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId())
                .orElseThrow();

        Optional<Ingredient> ingredientMatch = recipe.getIngredients().stream()
                .filter(ingredient -> Objects.equals(ingredient.getId(), ingredientCommand.getId()))
                .findFirst();

        IngredientCommand savedIngredient;
        if (ingredientMatch.isPresent()) {
            savedIngredient = updateIngredient(ingredientMatch.get(), ingredientCommand);
        } else {
            savedIngredient = addIngredient(recipe, ingredientCommand);
        }

        return savedIngredient;
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(NO_RECIPE_MESSAGE, recipeId)));
        Ingredient ingredientMatch = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(NO_INGREDIENT_IN_RECIPE_MESSAGE, ingredientId)));

        recipe.getIngredients().remove(ingredientMatch);
        recipeRepository.save(recipe);
        ingredientRepository.delete(ingredientMatch);
    }

    private IngredientCommand updateIngredient(Ingredient ingredient, IngredientCommand ingredientCommand) {
        ingredient.setDescription(ingredientCommand.getDescription());
        ingredient.setAmount(ingredientCommand.getAmount());
        ingredient.setUnitOfMeasure(
                unitOfMeasureRepository
                        .findById(ingredientCommand.getUnitOfMeasureCommand().getId())
                        .orElseThrow()
        );

        return ingredientConverter.convertToCommand(ingredientRepository.save(ingredient));
    }

    private IngredientCommand addIngredient(Recipe recipe, IngredientCommand ingredientCommand) {
        Ingredient newIngredient = ingredientConverter.convertToDomain(ingredientCommand);

        recipe.addIngredient(newIngredient);
        return ingredientConverter.convertToCommand(ingredientRepository.save(newIngredient));
    }
}
