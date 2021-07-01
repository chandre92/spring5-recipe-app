package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeConverter;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeConverter recipeConverter;

    @Test
    void getRecipeById() {
        // Arrange
        Long id = 1L;
        Recipe recipe = Recipe.builder().id(id).build();
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));

        // Act
        Recipe byId = recipeService.findById(id);

        // Assert
        assertThat(byId).isEqualTo(recipe);
        verify(recipeRepository).findById(id);
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test()
    void getRecipeByWrongId() {
        // Arrange
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(NoSuchElementException.class, () -> recipeService.findById(1L));
    }

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

    @Test
    void saveRecipeCommand() {
        // Arrange
        RecipeCommand recipeCommand = new RecipeCommand();
        Long id = 2L;
        recipeCommand.setId(id);

        Recipe recipe = new Recipe();
        recipe.setId(id);

        when(recipeConverter.convertToDomain(recipeCommand)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeConverter.convertToCommand(recipe)).thenReturn(recipeCommand);

        // Act
        RecipeCommand saveRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        assertThat(saveRecipeCommand).isEqualTo(recipeCommand);
        verify(recipeConverter).convertToDomain(recipeCommand);
        verify(recipeRepository).save(recipe);
        verify(recipeConverter).convertToCommand(recipe);
        verifyNoMoreInteractions(recipeConverter, recipeRepository);
    }

    @Test
    void findCommandById() {
        // Arrange
        Long recipeId = 25L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipeId);
        when(recipeConverter.convertToCommand(recipe)).thenReturn(recipeCommand);

        // Act
        RecipeCommand commandById = recipeService.findCommandById(recipeId);
        assertThat(commandById).isEqualTo(recipeCommand);
        verify(recipeRepository).findById(recipeId);
        verify(recipeConverter).convertToCommand(recipe);
        verifyNoMoreInteractions(recipeConverter, recipeRepository);
    }

    @Test
    void deleteById() {
        // Arrange
        Long id = 1L;

        // Act
        recipeService.deleteById(id);

        // Assert
        verify(recipeRepository).deleteById(id);
        verifyNoMoreInteractions(recipeRepository);
    }
}