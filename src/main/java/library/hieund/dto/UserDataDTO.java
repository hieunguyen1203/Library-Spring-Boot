package library.hieund.dto;

import java.util.List;

import javax.validation.constraints.Email;

import io.swagger.annotations.ApiModelProperty;
import library.hieund.model.UserRole;
import library.hieund.validator.EmailConstraint;
import library.hieund.validator.PasswordConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDataDTO {

    @ApiModelProperty(position = 0)
    private int id;

    @ApiModelProperty(position = 1)
    private String username;

    @ApiModelProperty(position = 2)
    @EmailConstraint(message = "Incorrect email format!")
    private String email;

    @ApiModelProperty(position = 3)
    @PasswordConstraint(message = "Password must be longer than 6 characters!")
    private String password;

    @ApiModelProperty(position = 4)
    List<UserRole> appUserRoles;

}
