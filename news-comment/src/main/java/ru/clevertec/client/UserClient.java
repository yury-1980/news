package ru.clevertec.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${urls.user-role}", name = "user-role")//порт юзера
public interface UserClient {

    @PostMapping("/authenticate")
    void authenticate(@RequestBody AuthenticationRequest request)
}
