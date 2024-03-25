package com.tournamentapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;

@Document
public class TournamentReferee {

    @Id
    public String id;

    @DocumentReference
    public User user;

//    public int refereeNumber;

    public String refereeLicense;
    public LocalDate refereeLicenseDate;

}
