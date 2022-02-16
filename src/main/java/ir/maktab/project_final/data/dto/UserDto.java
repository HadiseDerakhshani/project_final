package ir.maktab.project_final.data.dto;

import ir.maktab.project_final.data.entity.enums.UserRole;
import ir.maktab.project_final.data.entity.enums.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserDto extends BaseDto {

    @Pattern(regexp = "^[a-zA-Z]+$", message = "firstName is not alphabet")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "lastName is not alphabet")
    private String lastName;

    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "invalid email")
    @NotBlank(message = "email is blank")
    private String email;

    @Pattern(regexp = "^[A-Za-z0-9._%+@|!&*=/-]{8,}$", message = "invalid password")
    private String password;

    @Pattern(regexp = "^(\\+98|0)?9\\d{9}$", message = "invalid phoneNumber")
    private String phoneNumber;

    private UserStatus userStatus;

    private Date dateRegister;

    private Date dateUpdate;

    private UserRole userRole;

    private double credit;

}
