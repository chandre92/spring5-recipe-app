package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class IngredientControllerTest {
    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @InjectMocks
    IngredientController ingredientController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    void listIngredients() throws Exception {
        // Arrange
        Long id = 123L;
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(id);

        when(recipeService.findCommandById(id)).thenReturn(recipeCommand);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + id + "/ingredients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"))
                .andExpect(MockMvcResultMatchers.view().name("ingredient/list"));
        verify(recipeService).findCommandById(id);
        verifyNoMoreInteractions(recipeService);
        verifyNoMoreInteractions(ingredientService);
    }

    @Test
    void showIngredient() throws Exception {
        // Arrange
        Long recipeId = 123L;
        Long ingredientId = 321L;
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setId(ingredientId);

        when(ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId)).thenReturn(ingredientCommand);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/ingredient/" + ingredientId + "/show"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ingredient/show"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"));
        verify(ingredientService).findByRecipeIdAndIngredientId(recipeId, ingredientId);
        verifyNoMoreInteractions(ingredientService);
        verifyNoMoreInteractions(recipeService);
    }
}