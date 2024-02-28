package ru.clevertec.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.clevertec.entity.TextNews;
import ru.clevertec.util.ConstFormatDate;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequestDTOOld {

    private String title;

    @Schema(description = "Дата и время создания.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstFormatDate.FORMAT)
    private LocalDateTime time;

    @Builder.Default
    @Schema(description = "Текст новости.")
    private TextNews textNews = new TextNews();

//    @Builder.Default
//    @Schema(description = "Комментарии к новости.")
//    private List<Comment> comments = new ArrayList<>();
}
