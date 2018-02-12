package com.knoema.search;

public enum SearchScopeFlag {
    Timeseries(0x1),
    NamedEntity(0x2),
    Atlas(0x4),
    Semantic(0x8);

    private final int value;

    SearchScopeFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static String toString(SearchScopeFlag value) {
        return value.name();
    }
}

