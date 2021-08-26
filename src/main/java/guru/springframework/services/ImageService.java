package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void saveImageService(Long recipeId, MultipartFile image) throws IOException;
}
