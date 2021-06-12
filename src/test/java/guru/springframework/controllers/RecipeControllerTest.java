package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {
    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    @Test
    void getRecipe() throws Exception {
        // Arrange
        String stringId = "1";
        Long longId = Long.parseLong(stringId);
        Recipe recipe = Recipe.builder().id(longId).build();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

        when(recipeService.findById(longId)).thenReturn(recipe);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/show/" + stringId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/show"));
    }
}
