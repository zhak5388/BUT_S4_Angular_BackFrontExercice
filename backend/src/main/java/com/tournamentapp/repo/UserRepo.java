package com.tournamentapp.repo;

import com.tournamentapp.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User,String> {
    User findByEmail(String email);

}
