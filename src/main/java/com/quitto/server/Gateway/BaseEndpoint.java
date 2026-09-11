package com.quitto.server.Gateway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quitto.server.Gateway.DTO.DataEntityUser;


@Controller
@RequestMapping("/api")
public class BaseEndpoint {

    @PostMapping("/gateway")
    public String gateway(@RequestBody DataEntityUser data) {

        return "entity";
    }
}
