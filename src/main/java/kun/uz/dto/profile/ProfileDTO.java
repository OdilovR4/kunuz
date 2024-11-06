package kun.uz.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kun.uz.dto.attach.AttachDTO;
import kun.uz.enums.ProfileRole;
import kun.uz.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {

   @NotBlank(message = "name is required")
   @Pattern(regexp = "^[a-zA-ZА-Яа-яЁёЎўҚқҒғҲҳ]+$", message = "name must consist of only letters")
   @Size(min = 3, max = 15, message = "the length of name must be between 3 and 15")
   String name;
   @NotBlank(message = "Surname is required")
   @Pattern(regexp = "^[a-zA-ZА-Яа-яЁёЎўҚқҒғҲҳ]+$", message = "surname must consist of only letters")
   @Size(min = 3, max = 15, message = "the length of surname must be between 3 and 15")
   String surname;
   @NotBlank(message = "username may be phone number or email is required")
   @Size(min = 6, max = 15, message = "the length of login must be between 6 and 20")
   String username;
   @NotBlank(message = "password is required ")
   @Size(min = 8, max = 15, message = "the length of password must be between 8 and 20")
   @Pattern(
           regexp = "^(?=.*[0-9])(?=.*[@#$%^&+=])(?=.*[a-zA-Z])[a-zA-Z0-9@#$%^&+=]{8,}$",
           message = "Password must contain letters, at least one digit, and one special character"
   )
   String password;

   @NotNull(message = "status is required")
   ProfileStatus status;

   @NotNull(message = "role is required")
   ProfileRole role;

   String photoId;

   LocalDateTime createdDate;

   @NotNull(message = "It is not be empty or null ")
   String jwtToken;

   private AttachDTO photo;

}
