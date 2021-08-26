package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {
    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    @InjectMocks
    ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void showUploadForm() throws Exception {
        // Arrange
        Long recipeId = 1L;
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipeId);

        when(recipeService.findCommandById(recipeId)).thenReturn(recipeCommand);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/image"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"))
                .andExpect(MockMvcResultMatchers.view().name("recipe/imageuploadform"));
        verify(recipeService).findCommandById(recipeId);
        verifyNoMoreInteractions(recipeService);
        verifyNoInteractions(imageService);
    }

    @Test
    void uploadImage() throws Exception {
        // Arrange
        Long recipeId = 1L;
        MockMultipartFile file = new MockMultipartFile("imagefile", "content".getBytes());

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/recipe/" + recipeId + "/image").file(file))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/" + recipeId + "/show"));
        verify(imageService).saveImageService(recipeId, file);
        verifyNoMoreInteractions(imageService);
        verifyNoInteractions(recipeService);
    }
}