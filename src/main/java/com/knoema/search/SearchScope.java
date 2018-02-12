package com.knoema.search;

import java.util.EnumSet;

public class SearchScope {
    private final EnumSet<SearchScopeFlag> value;

    static public final SearchScopeFlag Atlas = SearchScopeFlag.Atlas;
    static public final SearchScopeFlag Timeseries = SearchScopeFlag.Timeseries;
    static public final SearchScopeFlag NamedEntity = SearchScopeFlag.NamedEntity;
    static public final SearchScopeFlag Semantic = SearchScopeFlag.Semantic;

    private SearchScope(EnumSet<SearchScopeFlag> value) {
        this.value = value;
    }

    public static SearchScope none() {
        return new SearchScope(EnumSet.noneOf(SearchScopeFlag.class));
    }

    public static SearchScope of(SearchScopeFlag var0, SearchScopeFlag... var1) {
        return new SearchScope(EnumSet.of(var0, var1));
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public String toString() {
        return toString(this);
    }

    public EnumSet<SearchScopeFlag> value() {
        return value;
    }

    public static String toString(SearchScope value) {
        StringBuilder sb = new StringBuilder();
        boolean was = false;
        for (SearchScopeFlag flag : value.value) {
            if (was) {
                sb.append(',');
            }
            else {
                was = true;
            }
            sb.append(flag.toString());
        }
        return sb.toString();
    }
}
