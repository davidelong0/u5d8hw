package epicode.u5d8hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class NewBlogPostDTO {
    private int authorId;

    @NotBlank(message = "La categoria è obbligatoria")
    private String category;

    @NotBlank(message = "Il contenuto è obbligatorio")
    private String content;

    @Positive(message = "Il tempo di lettura deve essere positivo")
    private double readingTime;

    @NotBlank(message = "Il titolo è obbligatorio")
    private String title;
}
