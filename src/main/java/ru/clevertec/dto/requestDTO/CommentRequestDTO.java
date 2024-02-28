package ru.clevertec.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class CommentRequestDTO {

    private String userName;

    @Schema(description = "Дата и время создания.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstFormatDate.FORMAT)
    private LocalDateTime time;

    @Schema(description = "Текст комментария.")
    private String textComment;
}
