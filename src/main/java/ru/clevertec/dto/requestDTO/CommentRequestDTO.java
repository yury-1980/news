package ru.clevertec.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.clevertec.entity.News;
import ru.clevertec.entity.TextComment;
import ru.clevertec.util.ConstFormatDate;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
    // TODO: 19-02-2024: удалить
//    private Long id;
    private String userName;

    @Schema(description = "Дата и время создания.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstFormatDate.FORMAT)
    private LocalDateTime time;

    @Builder.Default
    @Schema(description = "Текст комментария.")
    private TextComment textComment = new TextComment();

    @Builder.Default
    @Schema(description = "Новости.")
    private News news = new News();
}
