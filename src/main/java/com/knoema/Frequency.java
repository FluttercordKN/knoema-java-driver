package com.knoema;

public enum Frequency {
    Annual(0),
    SemiAnnual(1),
    Quarterly(2),
    Monthly(3),
    Weekly(4),
    Daily(5);

    private final int value;

    Frequency(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static Frequency toFrequencyInt(char value) {

        switch (value) {
            case 'A':
                return Frequency.Annual;
            case 'H':
            case 'S':
                return Frequency.SemiAnnual;
            case 'Q':
                return Frequency.Quarterly;
            case 'M':
                return Frequency.Monthly;
            case 'W':
                return Frequency.Weekly;
            case 'D':
                return Frequency.Daily;
        }
        return null;
    }

    private static char toChar(Frequency frequency) {

        switch (frequency) {
            case Annual:
                return 'A';
            case SemiAnnual:
                return 'H';
            case Quarterly:
                return 'Q';
            case Monthly:
                return 'M';
            case Weekly:
                return 'W';
            case Daily:
                return 'D';
        }
        return '\0';
    }

    public static Frequency parse(String value) {
        return toFrequencyInt(value.charAt(0));
    }

    public static String toString(Frequency value) {
        return String.valueOf(toChar(value));
    }
}
