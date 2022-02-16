package ir.maktab.project_final.data.dto;

import ir.maktab.project_final.data.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class ManagerDto {
    @Pattern(regexp = "^[a-zA-Z]+$", message = "userName is not alphabet")
    private String username;


    @Pattern(regexp = "^[A-Za-z0-9._%+@|!&*=/-]{8,}$", message = "invalid password")
    private String password;
    private UserRole userRole;
}
