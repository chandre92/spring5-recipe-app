package guru.springframework.domain;

import lombok.*;

@Data
@EqualsAndHashCode(exclude = {"recipe"}, callSuper = true)
public class Notes extends BaseEntity {

    private String recipeNotes;

    @Override
    public String toString() {
        return "Notes{" +
                "recipeNotes='" + recipeNotes + '\'' +
                '}';
    }
}
