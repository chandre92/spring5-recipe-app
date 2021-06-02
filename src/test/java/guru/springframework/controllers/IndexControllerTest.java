package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {
    @InjectMocks
    IndexController indexController;

    @Mock
    Model model;

    @Mock
    RecipeService recipeService;

    @Captor
    ArgumentCaptor<Set<Recipe>> recipesCaptor;

    @Test
    void testMockMVC() throws Exception {
        // Arrange
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    void getIndexPage() {
        // Arrange
        Set<Recipe> recipes = Collections.singleton(new Recipe());
        when(recipeService.getRecipes()).thenReturn(recipes);

        // Act
        String indexPage = indexController.getIndexPage(model);

        // Assert
        assertThat(indexPage).isEqualTo("index");
        verify(model, times(1)).addAttribute(eq("recipes"), recipesCaptor.capture());
        verify(recipeService, times(1)).getRecipes();
        verifyNoMoreInteractions(model, recipeService);
        assertThat(recipesCaptor.getValue()).isEqualTo(recipes);
    }
}