package guru.springframework.domain;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"recipe"}, callSuper = true)
@Entity
public class Notes extends BaseEntity {

    @OneToOne
    private Recipe recipe;

    @Lob
    private String recipeNotes;

    @Override
    public String toString() {
        return "Notes{" +
                "recipe=" + (recipe == null ? null : recipe.getId()) +
                ", recipeNotes='" + recipeNotes + '\'' +
                '}';
    }
}
