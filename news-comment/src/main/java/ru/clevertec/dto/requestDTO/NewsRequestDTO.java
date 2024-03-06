package ru.clevertec.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.clevertec.util.ConstFormatDate;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequestDTO {

    @NotBlank(message = "Название не должен быть пустым")
    private String title;

    @Schema(description = "Дата и время создания.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstFormatDate.FORMAT)
    private LocalDateTime time;

    @Schema(description = "Текст новости.")
    @NotBlank(message = "Статья не должена быть пустым")
    private String textNews;

    @Schema(description = "Автор новости.")
    @NotBlank(message = "Имя автора не должен быть пустым")
    @Size(max = 32, message = "Имя автора должно превышать 32 символов")
    private String author;
}
