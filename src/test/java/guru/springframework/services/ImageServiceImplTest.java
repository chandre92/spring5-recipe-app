package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    ImageServiceImpl imageService;

    @Test
    void saveImageService_exceptionOnRecipeNotFound() {
        // Arrange
        Long recipeId = 1L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> imageService.saveImageService(recipeId, null),
                String.format("Recipe with id %s not found", recipeId));
    }

    @Test
    void saveImageService() throws IOException {
        // Arrange
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        byte[] bytes = "content".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name", bytes);

        // Act
        imageService.saveImageService(recipeId, mockMultipartFile);

        // Assert
        verify(recipeRepository).findById(recipeId);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(captor.capture());
        assertEquals(bytes.length, captor.getValue().getImage().length);
        verifyNoMoreInteractions(recipeRepository);
    }
}