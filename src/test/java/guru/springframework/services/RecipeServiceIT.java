package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeConverter;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

// Do not need the @ExtendWith(SpringExtension.class) because @SpringBootTest already has it.
@SpringBootTest
public class RecipeServiceIT {
    private static final String NEW_DESCRIPTION = "New description";

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeConverter recipeConverter;

    @Test
    @Transactional
    public void testSave() {
        // Arrange
        Recipe recipe = recipeRepository.findAll()
                .iterator()
                .next();
        RecipeCommand recipeCommand = recipeConverter.convertToCommand(recipe);

        // Act
        recipeCommand.setDescription(NEW_DESCRIPTION);
        recipeService.saveRecipeCommand(recipeCommand);

        // Assert
        Recipe savedRecipe = recipeRepository.findById(recipe.getId()).orElseThrow();
        assertThat(savedRecipe.getDescription()).isEqualTo(NEW_DESCRIPTION);
        assertThat(recipe.getCategories()).hasSize(savedRecipe.getCategories().size());
        assertThat(recipe.getIngredients()).hasSize(savedRecipe.getIngredients().size());
    }
}

