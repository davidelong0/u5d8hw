package epicode.u5d8hw.dto;

import lombok.Data;

@Data
public class AuthorDTO {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String dateOfBirth;
    private String avatar;
}
