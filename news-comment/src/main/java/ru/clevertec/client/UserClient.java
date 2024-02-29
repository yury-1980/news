package ru.clevertec.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(url = "http://localhost:8080/news/233", name = "название сервиса юзер")//порт юзера
public interface UserClient {
}
