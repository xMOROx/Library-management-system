package pl.edu.agh.managementlibrarysystem.utils;

public class Pair<K, V> {
    K value0;
    V value1;

    private Pair(K value0, V value1) {
        this.value0 = value0;
        this.value1 = value1;
    }

    public static <K, V> Pair<K, V> build(K value0, V value1) {
        return new Pair<>(value0, value1);
    }

    public K getFirst() {
        return value0;
    }

    public V getSecond() {
        return value1;
    }
}
