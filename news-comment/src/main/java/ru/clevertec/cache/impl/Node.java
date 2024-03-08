package ru.clevertec.cache.impl;

import lombok.Getter;
import lombok.Setter;

/**
 *  Класс, представляющий узел двусвязного списка для хранения ключей и значений.
 *
 * @param <K> Тип ключа
 * @param <V> Тип значения
 */
@Getter
@Setter
public class Node<K, V> {

    int frequency;
    private K key;
    private V value;
    private Node<K, V> prev;
    private Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Node(K key, V value, int frequency) {
        this.key = key;
        this.value = value;
        this.frequency = frequency;
    }
}
