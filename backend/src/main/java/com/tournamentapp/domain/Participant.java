package com.tournamentapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Document
public class Participant {

    @Id
    public String id;

    @DocumentReference // ManyToOne relation
    public User user;

    public LocalDateTime registeredDateTime;
    public String licenseCertificate;


    @DocumentReference // parent relation
    public Tournament tournament;

    @DocumentReference
    public TournamentGroup tournamentGroup; // parent relation
    // public int tournamentGroupIndex;

}
