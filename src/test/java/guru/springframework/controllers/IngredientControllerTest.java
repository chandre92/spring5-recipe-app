package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class IngredientControllerTest {
    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

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
        verifyNoMoreInteractions(recipeService, ingredientService);
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
        verifyNoMoreInteractions(ingredientService, recipeService);
    }

    @Test
    void updateRecipeIngredient() throws Exception {
        // Arrange
        Long recipeId = 1L;
        Long ingredientId = 2L;
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(ingredientId);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId))
                .thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAll()).thenReturn(Set.of(unitOfMeasureCommand));

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/ingredient/" + ingredientId + "/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ingredient/ingredientform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("uomList"));
        verify(ingredientService).findByRecipeIdAndIngredientId(recipeId, ingredientId);
        verify(unitOfMeasureService).listAll();
        verifyNoMoreInteractions(ingredientService,unitOfMeasureService);
    }

    @Test
    void saveOrUpdate() throws Exception {
        // Arrange
        Long recipeId = 1L;
        Long ingredientId = 2L;
        IngredientCommand savedIngredientCommand = new IngredientCommand();
        savedIngredientCommand.setId(ingredientId);
        savedIngredientCommand.setRecipeId(recipeId);

        when(ingredientService.saveIngredientCommand(any(IngredientCommand.class))).thenReturn(savedIngredientCommand);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/recipe/ingredient"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/" + recipeId + "/ingredient/" + ingredientId + "/show"));
        verify(ingredientService).saveIngredientCommand(any());
        verifyNoMoreInteractions(ingredientService);
        verifyNoInteractions(unitOfMeasureService, recipeService);
    }

    @Test
    void newIngredient_exceptionOnWrongRecipeId() {
        // Arrange
        Long notExistingId = 123L;

        // Act && Assert
        assertThrows(IllegalArgumentException.class, () -> ingredientController.newIngredient(notExistingId, null));
        verify(recipeService).isRecipeExists(notExistingId);
        verifyNoMoreInteractions(recipeService);
        verifyNoInteractions(unitOfMeasureService);
    }

    @Test
    public void newIngredient() throws Exception {
        // Arrange
        Long recipeId = 1L;
        when(recipeService.isRecipeExists(recipeId)).thenReturn(true);
        when(unitOfMeasureService.listAll()).thenReturn(Set.of(new UnitOfMeasureCommand()));

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/ingredient/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ingredient/ingredientform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient", "uomList"));
        verify(recipeService).isRecipeExists(recipeId);
        verify(unitOfMeasureService).listAll();
        verifyNoMoreInteractions(recipeService, unitOfMeasureService);
    }
}