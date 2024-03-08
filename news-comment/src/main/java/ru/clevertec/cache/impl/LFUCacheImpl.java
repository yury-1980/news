package ru.clevertec.cache.impl;

import jakarta.annotation.PostConstruct;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс LFUCacheImpl представляет реализацию Least Frequently Used (LFU) кэша.
 *
 * @param <K> Тип ключа
 * @param <V> Тип значения
 */
@Slf4j
@ToString
@Component
@Profile("test")
public class LFUCacheImpl<K, V> implements Cache<K, V> {

    private final int capacity;
    private Node<K, V> head;
    private Node<K, V> tail;
    private final Map<K, Node<K, V>> map;

    public LFUCacheImpl(@Value("${Cache.capacity}") int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    /**
     * Инициализация кэша после создания экземпляра объекта.
     */
    @PostConstruct
    void init() {
        log.info("Profile = test");
    }

    /**
     * Получает значение по ключу из кэша.
     *
     * @param key Ключ
     * @return Значение, соответствующее ключу, либо null, если ключ не найден
     */
    public V get(K key) {

        if (map.get(key) == null) {
            return null;
        }

        Node<K, V> item = map.get(key);
        removeNode(item);
        item.frequency = item.frequency + 1;
        addNodeWithUpdatedFrequency(item);

        return item.getValue();
    }

    /**
     * Помещает значение в кэш по ключу.
     *
     * @param key   Ключ
     * @param value Значение
     */
    public void put(K key, V value) {

        if (map.containsKey(key)) {
            Node<K, V> item = map.get(key);
            item.setValue(value);
            item.frequency = item.frequency + 1;

            removeNode(item);
            addNodeWithUpdatedFrequency(item);

        } else {

            if (map.size() >= capacity) {

                map.remove(head.getKey());
                removeNode(head);
            }

            Node<K, V> node = new Node<K, V>(key, value, 1);
            addNodeWithUpdatedFrequency(node);
            map.put(key, node);
        }
    }

    /**
     * Удаляет значение из кэша по ключу.
     *
     * @param key Ключ
     */
    public void remove(K key) {

        if (map.containsKey(key)) {
            Node<K, V> item = map.get(key);
            removeNode(item);
            map.remove(key);
        }
    }

    /**
     * Удаляет узел из двусвязного списка.
     *
     * @param node Узел для удаления
     */
    private void removeNode(Node<K, V> node) {

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());

        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());

        } else {
            tail = node.getPrev();
        }
    }

    /**
     * Добавляет узел в двусвязный список с обновленной частотой.
     *
     * @param node Узел для добавления
     */
    private void addNodeWithUpdatedFrequency(Node<K, V> node) {

        if (tail != null && head != null) {
            Node<K, V> temp = head;

            while (true) {

                if (temp.frequency > node.frequency) {

                    if (temp == head) {
                        node.setNext(temp);
                        temp.setPrev(node);
                        head = node;
                        break;

                    } else {
                        node.setNext(temp);
                        node.setPrev(temp.getPrev());
                        temp.getPrev().setNext(node);
                        node.setPrev(temp.getPrev());
                        break;
                    }
                } else {
                    temp = temp.getNext();

                    if (temp == null) {
                        tail.setNext(node);
                        node.setPrev(tail);
                        node.setNext(null);
                        tail = node;
                        break;
                    }
                }
            }
        } else {
            tail = node;
            head = tail;
        }
    }
}
