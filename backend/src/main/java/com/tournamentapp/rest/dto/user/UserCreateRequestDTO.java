package com.tournamentapp.rest.dto.user;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class UserCreateRequestDTO {

    @Nonnull
    @Pattern(regexp="[A-Z][a-z-. ]*")
    @Max(50)
    public String firstName;

    @Nonnull
    @Pattern(regexp="[A-Z][a-z-. ]*")
    @Max(50)
    public String lastName;

    @Nonnull
    public LocalDate birthDate;

    @Nonnull
    @Email
    @Max(100)
    public String email;

    @Nonnull
    @Min(6)
    public String password;

}
