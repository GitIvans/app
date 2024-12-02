package com.accenture.sms.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of user information")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @ApiModelProperty(notes = "The database generated user ID")
    private Long id;

    @ApiModelProperty(notes = "Username of the user")
    @NotNull(message = "Username must not be null")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @ApiModelProperty(notes = "Password of the user")
    @NotNull(message = "Password must not be null")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @ApiModelProperty(notes = "Email of the user")
    @NotNull(message = "Email must not be null")
    @Email(message = "Email should be valid")
    private String email;

    @ApiModelProperty(notes = "First name of the user")
    private String firstName;

    @ApiModelProperty(notes = "Last name of the user")
    private String lastName;
}
