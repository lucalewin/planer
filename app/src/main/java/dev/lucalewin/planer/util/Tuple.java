package dev.lucalewin.planer.util;

public class Tuple<T1, T2> {

    private T1 first;
    private T2 second;

    public Tuple(T1 t1, T2 t2) {
        this.first = t1;
        this.second = t2;
    }

    public static <T1, T2> Tuple<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple<>(t1, t2);
    }

    public T1 getFirst() {
        return first;
    }

    public void setFirst(T1 first) {
        this.first = first;
    }

    public T2 getSecond() {
        return second;
    }

    public void setSecond(T2 second) {
        this.second = second;
    }
}
