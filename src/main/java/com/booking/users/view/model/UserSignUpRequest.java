package com.booking.users.view.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;

public class UserSignUpRequest {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "name", value = "name", dataType = "java.lang.String", required = true, position = 1)
    private String name;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "user name", value = "user name", dataType = "java.lang.String", required = true, position = 2)
    private String username;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "dob", value = "dob", required = true, example = "2022-04-19", position = 3)
    private Date dob;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "email", value = "email", dataType = "java.lang.String", required = true, position = 4)
    private String email;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "phone number", value = "phone number", dataType = "java.lang.String", required = true, position = 5)
    private String phoneNumber;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "password", value = "password", dataType = "java.lang.String", required = true, position = 6)
    private String password;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "confirm password", value = "confirm password", dataType = "java.lang.String", required = true, position = 7)
    private String confirmPassword;

    public UserSignUpRequest(String name, String username, Date dob, String email, String phoneNumber, String password, String confirmPassword) {
        this.name = name;
        this.username = username;
        this.dob = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserSignUpRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
