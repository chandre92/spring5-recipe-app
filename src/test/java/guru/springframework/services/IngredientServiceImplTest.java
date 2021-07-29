package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientConverter;
import guru.springframework.converters.UnitOfMeasureConverter;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @Mock
    IngredientRepository ingredientRepository;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientService ingredientService;

    IngredientConverter ingredientConverter;

    @BeforeEach
    void setUp() {
        ingredientConverter = spy(new IngredientConverter(new UnitOfMeasureConverter()));
        ingredientService = new IngredientServiceImpl(ingredientRepository, ingredientConverter, recipeRepository, unitOfMeasureRepository);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
        // Arrange
        Long recipeId = 123L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        Ingredient firstIngredient = new Ingredient();
        firstIngredient.setId(1L);

        Long searchedIngredientId = 2L;
        Ingredient searchedIngredient = new Ingredient();
        searchedIngredient.setId(searchedIngredientId);

        Ingredient thirdIngredient = new Ingredient();
        thirdIngredient.setId(3L);

        recipe.addIngredient(firstIngredient);
        recipe.addIngredient(searchedIngredient);
        recipe.addIngredient(thirdIngredient);

        when(ingredientRepository.findByRecipeIdAndAndId(recipeId, searchedIngredientId)).thenReturn(searchedIngredient);

        // Act
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(recipeId, searchedIngredientId);

        // Assert
        assertThat(ingredientCommand.getId()).isEqualTo(searchedIngredientId);
        assertThat(ingredientCommand.getRecipeId()).isEqualTo(recipeId);
        verify(ingredientRepository).findByRecipeIdAndAndId(recipeId, searchedIngredientId);
        verifyNoMoreInteractions(ingredientRepository);
        verify(ingredientConverter).convertToCommand(searchedIngredient);
    }

    @Test
    void saveIngredientCommand_noRecipeFound() {
        // Arrange
        Long recipeId = 2L;
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act
        Assertions.assertThrows(NoSuchElementException.class,
                () -> ingredientService.saveIngredientCommand(ingredientCommand));
        verify(recipeRepository).findById(recipeId);
        verifyNoMoreInteractions(recipeRepository);
        verifyNoMoreInteractions(unitOfMeasureRepository);
        verifyNoMoreInteractions(ingredientConverter);
    }

    @Test
    void saveIngredientCommand_ingredientMatch() {
        // Arrange
        Long recipeId = 2L;
        Long ingredientId = 1L;
        Long newUnitOfMeasureId = 4L;
        String ingredientDescription = "foobar";
        BigDecimal ingredientAmount = BigDecimal.valueOf(2);

        Recipe recipe = initStartRecipeState(recipeId, ingredientId);

        UnitOfMeasure newUnitOfMeasure = new UnitOfMeasure();
        newUnitOfMeasure.setId(newUnitOfMeasureId);
        when(unitOfMeasureRepository.findById(newUnitOfMeasureId)).thenReturn(Optional.of(newUnitOfMeasure));

        // Act
        IngredientCommand ingredientCommandToSave = createFullyInitIngredientCommand(
                recipeId, ingredientId, ingredientDescription, ingredientAmount, newUnitOfMeasureId
        );
        IngredientCommand ingredientCommandSaved = ingredientService.saveIngredientCommand(ingredientCommandToSave);

        // Assert
        assertThat(ingredientCommandSaved.getRecipeId()).isEqualTo(recipeId);
        assertThat(ingredientCommandSaved.getDescription()).isEqualTo(ingredientDescription);
        assertThat(ingredientCommandSaved.getAmount()).isEqualTo(ingredientAmount);
        assertThat(ingredientCommandSaved.getUnitOfMeasureCommand().getId()).isEqualTo(newUnitOfMeasureId);
        verify(recipeRepository).findById(recipeId);
        verify(recipeRepository).save(recipe);
        verify(unitOfMeasureRepository).findById(newUnitOfMeasureId);
        verify(ingredientConverter).convertToCommand(recipe.getIngredients().iterator().next());

        verifyNoMoreInteractions(recipeRepository, unitOfMeasureRepository, ingredientConverter);
    }

    @Test
    void saveIngredientCommand_noIngredientMatch() {
        // Arrange
        Long recipeId = 1L;
        Long ingredientId = 2L;
        Long newIngredientId = 3L;
        Long unitOfMeasureId = 4L;

        Recipe recipe = initStartRecipeState(recipeId, ingredientId);

        IngredientCommand ingredientCommandToSave = createFullyInitIngredientCommand(
                recipeId, newIngredientId, "newDescription", BigDecimal.TEN, unitOfMeasureId
        );

        // Act
        ingredientService.saveIngredientCommand(ingredientCommandToSave);

        // Assert
        assertThat(recipe.getIngredients()).hasSize(2);

        Collection<Long> ingredientsIds = recipe.getIngredients().stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());
        assertThat(ingredientsIds).contains(ingredientId, newIngredientId);
        verify(recipeRepository).findById(recipeId);
        verify(recipeRepository).save(recipe);
        verify(ingredientConverter).convertToDomain(ingredientCommandToSave);
        verify(ingredientConverter).convertToCommand(recipe.getIngredients().iterator().next());

        verifyNoMoreInteractions(recipeRepository, ingredientConverter);
        verifyNoInteractions(unitOfMeasureRepository);
    }


    private Recipe initStartRecipeState(Long recipeId, Long ingredientId) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        ingredient.setAmount(BigDecimal.ONE);
        ingredient.setDescription("init description");

        recipe.addIngredient(ingredient);
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        return recipe;
    }

    private IngredientCommand createFullyInitIngredientCommand(Long recipeId, Long ingredientId, String ingredientDescription, BigDecimal ingredientAmount, Long unitOfMeasureId) {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setId(ingredientId);
        ingredientCommand.setDescription(ingredientDescription);
        ingredientCommand.setAmount(ingredientAmount);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(unitOfMeasureId);
        ingredientCommand.setUnitOfMeasureCommand(unitOfMeasureCommand);

        return ingredientCommand;
    }
}