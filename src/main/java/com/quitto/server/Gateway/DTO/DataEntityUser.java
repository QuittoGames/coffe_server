package com.quitto.server.Gateway.DTO;

import java.util.Optional;

public record DataEntityUser(
    String token,
    Optional<String> id
) {

}
