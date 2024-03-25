package com.tournamentapp.rest;

import com.tournamentapp.rest.dto.user.UserCreateRequestDTO;
import com.tournamentapp.rest.dto.user.UserPasswordResetEmailRequestDTO;
import com.tournamentapp.rest.dto.user.UserPasswordResetEmailResponseDTO;
import com.tournamentapp.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user", produces = "application/json")
@OpenAPIDefinition(
        // tags = { Tag("User") }
)
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping()
    @Operation(description = "create a new User")
    public UserPasswordResetEmailResponseDTO create(
            @RequestBody @Valid UserCreateRequestDTO req) {
        return userService.createUser(req);
    }

    @PostMapping("/reset-password-request")
    @Operation(description = "send email for forgotten password reset")
    public UserPasswordResetEmailResponseDTO sendEmailForPasswordReset(
            @RequestBody @Valid UserPasswordResetEmailRequestDTO req) {
        return userService.sendEmailForPasswordReset(req);
    }

    // TOADD
    // updateUser()
    // disableUserAnonymize()

}
