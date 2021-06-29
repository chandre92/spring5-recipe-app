package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.NotesCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeConverterTest {
    private static final Long RECIPE_ID = 1L;
    private static final Integer COOK_TIME = Integer.valueOf("5");
    private static final Integer PREP_TIME = Integer.valueOf("7");
    private static final String DESCRIPTION = "My Recipe";
    private static final String DIRECTIONS = "Directions";
    private static final Difficulty DIFFICULTY = Difficulty.EASY;
    private static final Integer SERVINGS = Integer.valueOf("3");
    private static final String SOURCE = "Source";
    private static final String URL = "Some URL";
    private static final Long CAT_ID_1 = 1L;
    private static final Long CAT_ID2 = 2L;
    private static final Long INGREDIENT_ID_1 = 3L;
    private static final Long INGREDIENT_ID_2 = 4L;
    private static final Long NOTES_ID = 9L;

    RecipeConverter converter;


    @BeforeEach
    public void setUp() {
        converter = new RecipeConverter(
                new NotesConverter(),
                new CategoryConverter(),
                new IngredientConverter(new UnitOfMeasureConverter())
        );
    }

    @Test
    void convertToCommand_withNull() {
        assertNull(converter.convertToCommand(null));
    }

    @Test
    void convertToDomain_withNull() {
        assertNull(converter.convertToDomain(null));
    }

    @Test
    void convertToCommand_withEmpty() {
        assertNotNull(converter.convertToCommand(new Recipe()));
    }

    @Test
    void convertToDomain_withEmpty() {
        assertNotNull(converter.convertToDomain(new RecipeCommand()));
    }

    @Test
    void convertToCommand() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setCookTime(COOK_TIME);
        recipe.setPrepTime(PREP_TIME);
        recipe.setDescription(DESCRIPTION);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setDirections(DIRECTIONS);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);

        Notes notes = new Notes();
        notes.setId(NOTES_ID);

        recipe.setNotes(notes);

        Category category = new Category();
        category.setId(CAT_ID_1);

        Category category2 = new Category();
        category2.setId(CAT_ID2);

        recipe.getCategories().add(category);
        recipe.getCategories().add(category2);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID_1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(INGREDIENT_ID_2);

        recipe.getIngredients().add(ingredient);
        recipe.getIngredients().add(ingredient2);

        // Act
        RecipeCommand command = converter.convertToCommand(recipe);

        // Assert
        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getId());
        assertEquals(COOK_TIME, command.getCookTime());
        assertEquals(PREP_TIME, command.getPrepTime());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(DIFFICULTY, command.getDifficulty());
        assertEquals(DIRECTIONS, command.getDirections());
        assertEquals(SERVINGS, command.getServings());
        assertEquals(SOURCE, command.getSource());
        assertEquals(URL, command.getUrl());
        assertEquals(NOTES_ID, command.getNotesCommand().getId());
        assertEquals(2, command.getCategoryCommands().size());
        assertEquals(2, command.getIngredientCommands().size());
    }

    @Test
    void convertToDomain() {
        // Arrange
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setCookTime(COOK_TIME);
        recipeCommand.setPrepTime(PREP_TIME);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setDifficulty(DIFFICULTY);
        recipeCommand.setDirections(DIRECTIONS);
        recipeCommand.setServings(SERVINGS);
        recipeCommand.setSource(SOURCE);
        recipeCommand.setUrl(URL);

        NotesCommand notes = new NotesCommand();
        notes.setId(NOTES_ID);

        recipeCommand.setNotesCommand(notes);

        CategoryCommand category = new CategoryCommand();
        category.setId(CAT_ID_1);

        CategoryCommand category2 = new CategoryCommand();
        category2.setId(CAT_ID2);

        recipeCommand.getCategoryCommands().add(category);
        recipeCommand.getCategoryCommands().add(category2);

        IngredientCommand ingredient = new IngredientCommand();
        ingredient.setId(INGREDIENT_ID_1);

        IngredientCommand ingredient2 = new IngredientCommand();
        ingredient2.setId(INGREDIENT_ID_2);

        recipeCommand.getIngredientCommands().add(ingredient);
        recipeCommand.getIngredientCommands().add(ingredient2);

        // Act
        Recipe recipe  = converter.convertToDomain(recipeCommand);

        // Assert
        assertNotNull(recipe);
        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(2, recipe.getCategories().size());
        assertEquals(2, recipe.getIngredients().size());
    }
}