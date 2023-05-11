package library.hieund.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import library.hieund.model.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {
    @ApiModelProperty(position = 0)
    private String title;
    @ApiModelProperty(position = 1)
    private String author;
    @ApiModelProperty(position = 2)
    private String stock;
    @ApiModelProperty(position = 3)
    private String total;

}
