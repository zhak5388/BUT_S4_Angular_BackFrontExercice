package fr.pingpong.backend;

import fr.pingpong.backend.rest.dto.PlayerAddRequestDTO;
import fr.pingpong.backend.rest.dto.PlayerAddResponseDTO;
import org.springframework.web.bind.annotation.*;

//All routes will start with /api (Define the base path)
@RequestMapping("/api")

//Annotation. It marks this class as a rest Controller, so spring framework can identify it as such
@RestController
public class PlayerController {

    // Define a POST endpoint for adding a player
    //curl -v http://localhost:8080/api/player/add --header "Content-Type: application/json" -d '{"name":"jean"}'
    @PostMapping("/player/add")
    public PlayerAddResponseDTO createPlayer(@RequestBody PlayerAddRequestDTO req) {
        return new PlayerAddResponseDTO(java.time.LocalDateTime.now().toString(), req.name);
    }
}
