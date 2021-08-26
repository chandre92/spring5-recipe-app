package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImageService(Long recipeId, MultipartFile image);
}
