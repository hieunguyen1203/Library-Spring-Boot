package library.hieund.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.Null;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.lang.Nullable;

import io.swagger.annotations.ApiModelProperty;
import library.hieund.model.UserRole;
import library.hieund.validator.EmailConstraint;
import library.hieund.validator.PasswordConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    @ApiModelProperty(position = 0)
    @NumberFormat(style = Style.NUMBER)
    private String id;

    @ApiModelProperty(position = 1)
    private String username;

    @ApiModelProperty(position = 2)
//    @EmailConstraint(message = "Incorrect email format!")
    private String email;

    @ApiModelProperty(position = 3)

    @PasswordConstraint(message = "Password must be longer than 6 characters!")
    private String password;

    @ApiModelProperty(position = 4)
    List<UserRole> appUserRoles;

}
