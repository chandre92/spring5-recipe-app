package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientConverter;
import guru.springframework.converters.UnitOfMeasureConverter;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        // TODO: 13.07.21 try to impl with pure annotation config
        // TODO: 13.07.21 google for Mockito vs transitive dependencies
        ingredientService = new IngredientServiceImpl(recipeRepository, new IngredientConverter(new UnitOfMeasureConverter()));
    }

    @Test
    void findByRecipeIdAndIngredientId() {
        // Arrange
        Long recipeId = 123L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        Ingredient firstIngredient = new Ingredient();
        firstIngredient.setId(1L);

        Long searchedIngredientId = 2L;
        Ingredient secondIngredient = new Ingredient();
        secondIngredient.setId(searchedIngredientId);

        Ingredient thirdIngredient = new Ingredient();
        thirdIngredient.setId(3L);

        recipe.addIngredient(firstIngredient);
        recipe.addIngredient(secondIngredient);
        recipe.addIngredient(thirdIngredient);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Act
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(recipeId, searchedIngredientId);

        // Assert
        assertThat(ingredientCommand.getId()).isEqualTo(searchedIngredientId);
        assertThat(ingredientCommand.getRecipeId()).isEqualTo(recipeId);
        verify(recipeRepository).findById(recipeId);
    }
}