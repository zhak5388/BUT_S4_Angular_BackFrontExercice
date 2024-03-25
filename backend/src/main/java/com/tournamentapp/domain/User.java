package com.tournamentapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class User {

    @Id
    public String id;

    public String firstName;

    public String lastName;

    public LocalDate birthDate;


    public String email;
    public String privateEmailPasswordResetToken;
    public String privatePasswordSalt;
    public String privatePasswordSaltedHash;

}
