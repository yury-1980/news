package ru.clevertec.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.format.annotation.DateTimeFormat;
import ru.clevertec.util.bridge.TextCommentBridge;

import java.time.LocalDateTime;

@Data
//@Entity
@Indexed
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class CommentOld {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FullTextField
    @Column(name = "user_name")
    private String userName;

    @GenericField
    @Column(name = "time")
    @Schema(description = "Дата и время создания.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime time;

    //    @Convert(converter = ISBNAttributeConverter.class)
    @FullTextField(valueBridge = @ValueBridgeRef(type = TextCommentBridge.class))
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Текст комментария.")
    @OneToOne(mappedBy = "comment", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private TextComment textComment = new TextComment();

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "Новости.")
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news = new News();
}
