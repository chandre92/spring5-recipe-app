package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Test
    void getRecipes() {
        // Arrange
        Recipe recipe = new Recipe();
        when(recipeRepository.findAll()).thenReturn(Collections.singletonList(recipe));

        // Act
        Set<Recipe> recipes = recipeService.getRecipes();

        // Assert
        assertThat(recipes).hasSize(1);
        assertThat(recipes).contains(recipe);
        verify(recipeRepository, times(1)).findAll();
        verifyNoMoreInteractions(recipeRepository);
    }
}