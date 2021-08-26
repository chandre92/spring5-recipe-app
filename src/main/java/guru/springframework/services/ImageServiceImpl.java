package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageService(Long recipeId, MultipartFile image) throws IOException {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Recipe with id %s not found", recipeId)));

        byte[] primitiveBytes = image.getBytes();
        Byte[] bytes = new Byte[primitiveBytes.length];
        Arrays.parallelSetAll(bytes, n -> primitiveBytes[n]);

        recipe.setImage(bytes);
        recipeRepository.save(recipe);
    }
}
