package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientConverter;
import guru.springframework.converters.UnitOfMeasureConverter;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @Mock
    IngredientRepository ingredientRepository;

    IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        ingredientService = new IngredientServiceImpl(ingredientRepository, new IngredientConverter(new UnitOfMeasureConverter()));
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
        Ingredient searchedIngredient = new Ingredient();
        searchedIngredient.setId(searchedIngredientId);

        Ingredient thirdIngredient = new Ingredient();
        thirdIngredient.setId(3L);

        recipe.addIngredient(firstIngredient);
        recipe.addIngredient(searchedIngredient);
        recipe.addIngredient(thirdIngredient);

        when(ingredientRepository.findByRecipeIdAndAndId(recipeId, searchedIngredientId)).thenReturn(searchedIngredient);

        // Act
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(recipeId, searchedIngredientId);

        // Assert
        assertThat(ingredientCommand.getId()).isEqualTo(searchedIngredientId);
        assertThat(ingredientCommand.getRecipeId()).isEqualTo(recipeId);
        verify(ingredientRepository).findByRecipeIdAndAndId(recipeId, searchedIngredientId);
        verifyNoMoreInteractions(ingredientRepository);
    }
}