package guru.springframework.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"recipes"}, callSuper = true)
@Entity
public class Category extends BaseEntity {
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes;

}
