package guru.springframework.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Recipe extends BaseEntity {

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Set<Ingredient> ingredients = new HashSet<>();
    private Byte[] image;
    private Notes notes;
    private Difficulty difficulty;

    @DBRef
    private Set<Category> categories = new HashSet<>();

    @Builder
    public Recipe(String id, String description, Integer prepTime, Integer cookTime, Integer servings, String source, String url, String directions, Set<Ingredient> ingredients, Byte[] image, Notes notes, Difficulty difficulty, Set<Category> categories) {
        super(id);
        this.description = description;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
        this.source = source;
        this.url = url;
        this.directions = directions;
        this.ingredients = ingredients;
        this.image = image;
        this.notes = notes;
        this.difficulty = difficulty;
        this.categories = categories;
    }

    public Recipe addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }
}
