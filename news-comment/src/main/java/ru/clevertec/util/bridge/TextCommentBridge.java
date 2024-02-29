package ru.clevertec.util.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import ru.clevertec.entity.TextComment;

public class TextCommentBridge implements ValueBridge<TextComment, String> {

    @Override
    public String toIndexedValue(TextComment value, ValueBridgeToIndexedValueContext context) {
        // Преобразуйте TextComment в строку для индексации
        return value.toString();
    }

//    @Override
//    public TextComment fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
//        // Преобразуйте строку обратно в TextComment
//        return new TextComment(value);
//    }
//
//    @Override
//    public String parse(String value) {
//        // Преобразуйте строку в TextComment для проекции и поиска
//        return new TextComment(value);
//    }
}
