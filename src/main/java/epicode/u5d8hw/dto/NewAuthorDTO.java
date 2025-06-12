package epicode.u5d8hw.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewAuthorDTO {
    @NotBlank(message = "Il nome è obbligatorio")
    private String name;

    @NotBlank(message = "Il cognome è obbligatorio")
    private String surname;

    @Email(message = "Email non valida")
    @NotBlank(message = "L'email è obbligatoria")
    private String email;

    private String dateOfBirth;
}
