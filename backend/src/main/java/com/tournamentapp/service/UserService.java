package com.tournamentapp.service;

import com.tournamentapp.domain.User;
import com.tournamentapp.repo.UserRepo;
import com.tournamentapp.rest.dto.user.UserCreateRequestDTO;
import com.tournamentapp.rest.dto.user.UserDTO;
import com.tournamentapp.rest.dto.user.UserPasswordResetEmailResponseDTO;
import com.tournamentapp.rest.dto.user.UserPasswordResetEmailRequestDTO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserPasswordResetEmailResponseDTO createUser(UserCreateRequestDTO req) {
        User found = userRepo.findByEmail(req.email);
        if (found != null) {
            throw new IllegalArgumentException("email already used");
        }
        User user = new User();

        user.firstName = req.firstName;
        user.lastName = req.lastName;
        user.birthDate = req.birthDate;
        user.email = req.email;
        // TOADD sendEmail for confirmation
        user.privateEmailPasswordResetToken = null;
        Random rand = new Random();
        user.privatePasswordSalt = rand.nextInt() + ":";
        user.privatePasswordSaltedHash = hashPassBase64(user.privatePasswordSalt, req.password);

        user = userRepo.save(user);

        return toUserPrivateDTO(user);
    }

    private String hashPassBase64(String salt, String pass) {
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            String data = salt + pass;
            byte[] digest = hash.digest(data.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("should not occur", e);
        }
    }

    private UserPasswordResetEmailResponseDTO toUserPrivateDTO(User src) {
        val res = new UserPasswordResetEmailResponseDTO();
        res.id = src.id;
        res.firstName = src.firstName;
        res.lastName = src.lastName;
        res.birthDate = src.birthDate;
        res.email = src.email;
        return res;
    }

    private UserDTO toUserDTO(User src) {
        val res = new UserDTO();
        res.id = src.id;
        res.firstName = src.firstName;
        res.lastName = src.lastName;
        return res;
    }

    public UserPasswordResetEmailResponseDTO sendEmailForPasswordReset(UserPasswordResetEmailRequestDTO req) {
        val res = new UserPasswordResetEmailResponseDTO();
        // TOADD..
        return res;
    }
}
