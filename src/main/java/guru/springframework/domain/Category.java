package guru.springframework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(exclude = {"recipes"}, callSuper = true)
@Document
public class Category extends BaseEntity {
    private String description;

    @DBRef
    private Set<Recipe> recipes;

    @Override
    public String toString() {
        return "Category{" +
                "description='" + description + '\'' +
                ", recipes=" + (recipes == null ? null : recipes.stream().map(Recipe::getId).map(Objects::toString).collect(Collectors.joining(", "))) +
                '}';
    }
}
