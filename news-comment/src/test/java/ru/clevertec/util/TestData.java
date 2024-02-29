package ru.clevertec.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TestData {

    public static final UUID UUID_PERSON = UUID.fromString("d8b6eda1-2ac7-4190-8523-389b3cccffa9");
    public static final UUID UUID_PERSON_TWO = UUID.fromString("f4036a98-9fe6-4b85-bf89-3342807872c2");
    public static final UUID UUID_HOUSE = UUID.fromString("99efee95-2f1c-459e-b97c-509f7399aa01");
    public static final UUID UUID_HOUSE_TWO = UUID.fromString("99efee96-2f1c-459e-b97c-509f7399aa03");
    private static final String DATE_STRING = "2024-01-12 23:29:04.595808";
    private static final String DATA_CREATE_HOUSE_STRING = "2024-01-12 23:29:04.595808";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    private static final LocalDateTime DATE_TIME = LocalDateTime.parse(DATE_STRING, FORMATTER);
    private static final LocalDateTime DATA_CREATE_HOUSE = LocalDateTime.parse(DATA_CREATE_HOUSE_STRING, FORMATTER);

    public static ResponsePersonDTO getResponsePersonDTO() {
        return ResponsePersonDTO.builder()
                .uuid(UUID_PERSON)
                .name("Masha")
                .surname("Petrova")
                .sex(Sex.FEMALE)
                .passportSeries("AA")
                .passportNumber(7L)
                .createDate(DATE_TIME)
                .updateDate(DATE_TIME)
                .build();
    }

    public static Person getPerson() {
        return Person.builder()
                .uuid(UUID_PERSON)
                .name("Masha")
                .surname("Petrova")
                .sex(Sex.FEMALE)
                .passportSeries("AA")
                .passportNumber(7L)
                .createDate(DATE_TIME)
                .updateDate(DATE_TIME)
                .build();
    }

    public static House getHouse() {
        return House.builder()
                .uuid(UUID_HOUSE)
                .area("Some area")
                .country("Some country")
                .city("Some city")
                .street("Some street")
                .number(123L)
                .createDate(DATA_CREATE_HOUSE)
                .build();
    }

    public static ResponseHouseDTO getResponseHouseDTO() {
        return ResponseHouseDTO.builder()
                .uuid(UUID_HOUSE)
                .area("Some area")
                .country("Some country")
                .city("Some city")
                .street("Some street")
                .number(123L)
                .createDate(DATA_CREATE_HOUSE)
                .build();
    }

    public static RequestPersonDTO getRequestPersonDTO() {

        return RequestPersonDTO.builder()
                .name("Masha")
                .surname("Petrova")
                .sex(Sex.FEMALE)
                .passportSeries("AA")
                .passportNumber(77L)
                .build();
    }

    public static RequestHouseDTO getRequestHouseDTO() {
        return RequestHouseDTO.builder()
                .area("Some area")
                .country("Some country")
                .city("Some city")
                .street("Some street")
                .number(1234L)
                .build();
    }

    public static <MyObject> String convertToJson(MyObject myObject) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(myObject);
    }
}
