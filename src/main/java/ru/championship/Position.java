package ru.championship;

public enum Position {
    DEFENDER("Защитник"),
    GOALKEEPER("Вратарь"),
    MIDFIELD("Полузащитник"),
    FORWARD("Нападающий");

    private final String russianName;

    Position(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }

    public static Position fromValue(String value) {
        return Position.valueOf(value.trim().toUpperCase());
    }
}
