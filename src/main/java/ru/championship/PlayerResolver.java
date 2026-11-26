package ru.championship;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerResolver implements IResolver {
    private final List<Player> players;

    public PlayerResolver(List<Player> players) {
        this.players = List.copyOf(players);
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public int getCountWithoutAgency() {
        return (int) players.stream()
                .filter(p -> p.agency() == null || p.agency().isBlank())
                .count();
    }

    @Override
    public int getMaxDefenderGoalsCount() {
        return players.stream()
                .filter(p -> p.position() == Position.DEFENDER)
                .mapToInt(Player::goals)
                .max()
                .orElse(0);
    }

    @Override
    public String getTheExpensiveGermanPlayerPosition() {
        return players.stream()
                .filter(p -> p.nationality().equalsIgnoreCase("Germany"))
                .max(Comparator.comparingLong(Player::transferCost))
                .map(p -> p.position().getRussianName())
                .orElse("Немецкие игроки не найдены");
    }

    @Override
    public Map<String, String> getPlayersByPosition() {
        Map<String, String> result = new LinkedHashMap<>();
        players.stream().collect(Collectors.groupingBy(
                p -> p.position().getRussianName(),
                LinkedHashMap::new,
                Collectors.mapping(Player::name, Collectors.joining(", "))
        )).forEach(result::put);
        return result;
    }

    @Override
    public Set<String> getTeams() {
        return players.stream()
                .map(Player::team)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public Map<String, Integer> getTop5TeamsByGoalsCount() {
        Map<String, Integer> result = new LinkedHashMap<>();
        players.stream()
                .collect(Collectors.groupingBy(Player::team, Collectors.summingInt(Player::goals)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(5)
                .forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    @Override
    public String getAgencyWithMinPlayersCount() {
        return players.stream()
                .filter(p -> p.agency() != null && !p.agency().isBlank())
                .collect(Collectors.groupingBy(Player::agency,
                        Collectors.summingLong(Player::transferCost)))
                .entrySet().stream()
                .min(Comparator.comparingLong((Map.Entry<String, Long> e) -> e.getValue())
                        .thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .orElse("Агентства не найдены");
    }

    @Override
    public String getTheRudestTeam() {
        return players.stream()
                .collect(Collectors.groupingBy(Player::team,
                        Collectors.averagingInt(Player::redCards)))
                .entrySet().stream()
                .max(Comparator.<Map.Entry<String, Double>>comparingDouble(Map.Entry::getValue)
                        .thenComparing(Map.Entry::getKey, Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .orElse("Команды не найдены");
    }
}
