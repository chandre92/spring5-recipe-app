package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exception.NotFoundException;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {
    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    void getRecipe() throws Exception {
        // Arrange
        String stringId = "1";
        Long longId = Long.parseLong(stringId);
        Recipe recipe = Recipe.builder().id(longId).build();

        when(recipeService.findById(longId)).thenReturn(recipe);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + stringId + "/show/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/show"));
    }

    @Test
    void getRecipeNotFound() throws Exception {
        // Arrange
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show/"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.view().name("error/404error"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("exception"));
    }

    @Test
    void getRecipeByNotLongId() throws Exception {
        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/asdf/show/"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.view().name("error/400error"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("exception"));
    }

    @Test
    void getNewRecipeForm() throws Exception {
        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    void postNewRecipeForm() throws Exception {
        // Arrange
        Long id = 2L;
        String description = "description";

        RecipeCommand incomeCommand = new RecipeCommand();
        incomeCommand.setDescription(description);

        RecipeCommand resultCommand = new RecipeCommand();
        resultCommand.setId(id);
        resultCommand.setDescription(description);

        when(recipeService.saveRecipeCommand(incomeCommand)).thenReturn(resultCommand);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", description)
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/" + id + "/show"));

        verify(recipeService).saveRecipeCommand(incomeCommand);
        verifyNoMoreInteractions(recipeService);
    }

    @Test
    void testGetUpdateView() throws Exception {
        // Arrange
        Long id = 2L;
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(id);

        when(recipeService.findCommandById(id)).thenReturn(recipeCommand);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + id + "/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"))
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"));
        verify(recipeService).findCommandById(id);
        verifyNoMoreInteractions(recipeService);
    }

    @Test
    void deleteById() throws Exception {
        // Arrange
        Long id = 2L;

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + id + "/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
        verify(recipeService).deleteById(id);
        verifyNoMoreInteractions(recipeService);
    }
}
