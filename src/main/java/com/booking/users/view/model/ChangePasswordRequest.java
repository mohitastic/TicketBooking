package com.booking.users.view.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class ChangePasswordRequest {

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "old password", value = "old password", dataType = "java.lang.String", required = true, position = 1)
    private String oldPassword;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "new password", value = "new password", dataType = "java.lang.String", required = true, position = 2)
    private String newPassword;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "confirm new password", value = "confirm new password", dataType = "java.lang.String", required = true, position = 3)
    private String confirmNewPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public ChangePasswordRequest() {
    }
}
