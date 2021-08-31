package guru.springframework.commands;

import guru.springframework.domain.Difficulty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecipeCommand extends AbstractDescribableCommand {
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Set<IngredientCommand> ingredientCommands = new HashSet<>();
    private Byte[] image;
    private Difficulty difficulty;
    private NotesCommand notesCommand;
    private Set<CategoryCommand> categoryCommands = new HashSet<>();
}
