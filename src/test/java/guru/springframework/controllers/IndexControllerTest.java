package guru.springframework.controllers;

import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

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

    @Test
    void getIndexPage() {
        // Act
        String indexPage = indexController.getIndexPage(model);

        // Assert
        assertThat(indexPage).isEqualTo("index");
        verify(model, times(1)).addAttribute(eq("recipes"), anySet());
        verify(recipeService, times(1)).getRecipes();
        verifyNoMoreInteractions(model, recipeService);
    }
}