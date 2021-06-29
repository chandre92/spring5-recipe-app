package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class RecipeConverter implements TwoWaysConverter<RecipeCommand, Recipe> {
    private final NotesConverter notesConverter;
    private final CategoryConverter categoryConverter;
    private final IngredientConverter ingredientConverter;

    public RecipeConverter(NotesConverter notesConverter, CategoryConverter categoryConverter, IngredientConverter ingredientConverter) {
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Override
    public RecipeCommand convertToCommand(Recipe source) {
        if (source == null) {
            return null;
        }

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(source.getId());
        recipeCommand.setCookTime(source.getCookTime());
        recipeCommand.setPrepTime(source.getPrepTime());
        recipeCommand.setDescription(source.getDescription());
        recipeCommand.setDifficulty(source.getDifficulty());
        recipeCommand.setDirections(source.getDirections());
        recipeCommand.setServings(source.getServings());
        recipeCommand.setSource(source.getSource());
        recipeCommand.setUrl(source.getUrl());
        recipeCommand.setNotesCommand(notesConverter.convertToCommand(source.getNotes()));

        Optional.ofNullable(source.getCategories())
                .orElse(Collections.emptySet())
                .stream()
                .map(categoryConverter::convertToCommand)
                .forEach(categoryCommand -> recipeCommand.getCategoryCommands().add(categoryCommand));

        Optional.ofNullable(source.getIngredients())
                .orElse(Collections.emptySet())
                .stream()
                .map(ingredientConverter::convertToCommand)
                .forEach(ingredientCommand -> recipeCommand.getIngredientCommands().add(ingredientCommand));

        return recipeCommand;
    }

    @Override
    public Recipe convertToDomain(RecipeCommand source) {
        if (source == null) {
            return null;
        }

        Recipe recipe = new Recipe();
        recipe.setId(source.getId());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setDescription(source.getDescription());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setDirections(source.getDirections());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setNotes(notesConverter.convertToDomain(source.getNotesCommand()));

        Optional.ofNullable(source.getCategoryCommands())
                .orElse(Collections.emptySet())
                .stream()
                .map(categoryConverter::convertToDomain)
                .forEach(category -> recipe.getCategories().add(category));

        Optional.ofNullable(source.getIngredientCommands())
                .orElse(Collections.emptySet())
                .stream()
                .map(ingredientConverter::convertToDomain)
                .forEach(ingredient -> recipe.getIngredients().add(ingredient));

        return recipe;
    }
}
