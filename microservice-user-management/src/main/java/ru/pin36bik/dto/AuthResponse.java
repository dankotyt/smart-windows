package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("user")
    private UserDTO userDTO;
}
