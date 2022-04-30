package com.booking.users.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Entity
@Table(name = "user_details")
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty
    @NotBlank(message = "User name must be provided")
    @Column(name = "name", nullable = false, unique = true)
    @ApiModelProperty(name = "name", value = "Name of the user", required = true, example = "name", position = 1)
    private String name;

    @Column(name = "dob", nullable = false)
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of birth must be provided")
    @ApiModelProperty(name = "dob", value = "Date of birth of the user", required = true, example = "2022-04-19", position = 2)
    private Date dob;

    @JsonProperty
    @NotBlank(message = "Email address must be provided")
    @Column(name = "email_address", nullable = false)
    @ApiModelProperty(name = "email_address", value = "Email of the user", required = true, example = "abc@email.com", position = 3)
    private String email;

    @Column(name = "mobile_number", nullable = false)
    @JsonProperty
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number must have exactly 10 digits")
    @NotBlank(message = "Phone number must be provided")
    @ApiModelProperty(name = "mobile_number", value = "Phone number of the customer", required = true, example = "9933221100", position = 4)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public UserDetail() {
    }

    public UserDetail(String name, Date dob, String email, String phoneNumber, User user) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

}