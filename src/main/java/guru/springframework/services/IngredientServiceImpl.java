package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientConverter;
import guru.springframework.repositories.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientConverter ingredientConverter;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientConverter ingredientConverter) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientConverter = ingredientConverter;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        return ingredientConverter.convertToCommand(
                ingredientRepository.findByRecipeIdAndAndId(recipeId, ingredientId)
        );
    }
}
