package ru.clevertec.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.format.annotation.DateTimeFormat;
import ru.clevertec.util.ConstFormatDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Indexed
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FullTextField
    @Column(name = "title")
    private String title;

    @Column(name = "time")
    @Schema(description = "Дата и время создания.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstFormatDate.FORMAT)
    private LocalDateTime time;

    @FullTextField
    @Column(name = "text_news")
    private String textNews;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Комментарии к новости.")
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();
}
