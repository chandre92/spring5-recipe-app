package guru.springframework.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(exclude = {"recipes"}, callSuper = true)
@Entity
public class Category extends BaseEntity {
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes;

    @Override
    public String toString() {
        return "Category{" +
                "description='" + description + '\'' +
                ", recipes=" + (recipes == null ? null : recipes.stream().map(Recipe::getId).map(Objects::toString).collect(Collectors.joining(", "))) +
                '}';
    }
}
