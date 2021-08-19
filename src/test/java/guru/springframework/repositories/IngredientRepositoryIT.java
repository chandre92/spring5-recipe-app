package guru.springframework.repositories;

import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class IngredientRepositoryIT {
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Test
    void findByRecipeAndAndId() {
        // Arrange
        Ingredient expectedIngredient = ingredientRepository.save(new Ingredient());

        Recipe recipe = new Recipe();
        recipe.addIngredient(new Ingredient());
        recipe.addIngredient(expectedIngredient);
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Act
        Ingredient searchResult = ingredientRepository
                .findByRecipeIdAndId(savedRecipe.getId(), expectedIngredient.getId());

        // Assert
        assertThat(searchResult.getId()).isEqualTo(expectedIngredient.getId());
    }
}
