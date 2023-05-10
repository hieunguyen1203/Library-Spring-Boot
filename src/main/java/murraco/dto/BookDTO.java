package murraco.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import murraco.model.AppUserRole;

@Data
@NoArgsConstructor
public class BookDTO {
	 @ApiModelProperty(position = 0)
	 private String title;
	 @ApiModelProperty(position = 1)
	 private String author;
	 @ApiModelProperty(position = 2)
	 private int stock;
	 @ApiModelProperty(position = 3)
	 private int total;
	

}
