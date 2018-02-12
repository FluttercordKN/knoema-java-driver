package com.knoema.core;

import java.util.Objects;

public class Triplet<T1, T2, T3> {
    public final T1 item1;
    public final T2 item2;
    public final T3 item3;

    public Triplet(T1 item1, T2 item2, T3 item3) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(item1, triplet.item1) &&
                Objects.equals(item2, triplet.item2) &&
                Objects.equals(item3, triplet.item3);
    }

    @Override
    public int hashCode() {

        return Objects.hash(item1, item2, item3);
    }
}
