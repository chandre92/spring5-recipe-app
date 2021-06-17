package guru.springframework.commands;

import guru.springframework.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class RecipeCommand extends AbstractDescribableCommand {
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Set<IngredientCommand> ingredientCommands = new HashSet<>();
    private Difficulty difficulty;
    private NotesCommand notesCommand;
    private Set<CategoryCommand> categoryCommands = new HashSet<>();
}
