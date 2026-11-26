package ru.championship;

public record Player(
        String name,
        String team,
        String city,
        Position position,
        String nationality,
        String agency,
        long transferCost,
        int participations,
        int goals,
        int assists,
        int yellowCards,
        int redCards
) {}
