package ru.clevertec.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.util.ConstFormatDate;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;

    @NotBlank(message = "Имя автора не должен быть пустым")
    @Size(max = 32, message = "Имя автора должно превышать 32 символов")
    private String userName;

    @Schema(description = "Дата и время создания.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstFormatDate.FORMAT)
    private LocalDateTime time;

    @Schema(description = "Текст комментария.")
    @NotBlank(message = "Текст комментария не должен быть пустым")
    private String textComment;
}
