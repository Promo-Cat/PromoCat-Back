package org.promocat.promocat.dto.pojo;

/**
 * Created by Danil Lyskin at 20:42 12.12.2020
 */
public class Pair<T> {
    public T first;
    public T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }
}
