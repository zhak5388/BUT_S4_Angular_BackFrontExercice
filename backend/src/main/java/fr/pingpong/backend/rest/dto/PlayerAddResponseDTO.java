package fr.pingpong.backend.rest.dto;

public class PlayerAddResponseDTO {
    public String id;
    public String name;

    public PlayerAddResponseDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
