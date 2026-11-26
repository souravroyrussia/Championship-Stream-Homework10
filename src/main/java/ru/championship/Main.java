package ru.championship;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            List<Player> players = PlayerRepository.loadPlayers();
            IResolver resolver = new PlayerResolver(players);

            System.out.println("ЧЕМПИОНАТ — РЕЗУЛЬТАТЫ");
            System.out.println("Загружено игроков: " + players.size());
            System.out.println("1. Игроков без агентства: " + resolver.getCountWithoutAgency());
            System.out.println("2. Максимум голов у защитника: " + resolver.getMaxDefenderGoalsCount());
            System.out.println("3. Позиция самого дорогого немецкого игрока: "
                    + resolver.getTheExpensiveGermanPlayerPosition());

            System.out.println("4. Игроки по позициям:");
            resolver.getPlayersByPosition().forEach((position, names) ->
                    System.out.println("   " + position + ": " + names));

            System.out.println("5. Команды чемпионата: " + resolver.getTeams());
            System.out.println("6. Топ-5 команд по голам:");
            resolver.getTop5TeamsByGoalsCount().forEach((team, goals) ->
                    System.out.println("   " + team + " — " + goals));

            System.out.println("7. Агентство с наименьшей суммарной стоимостью игроков: "
                    + resolver.getAgencyWithMinPlayersCount());
            System.out.println("8. Команда с наибольшим средним числом красных карточек: "
                    + resolver.getTheRudestTeam());

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("\\nВведите ваш номер в списке группы N для выбора варианта графика: ");
                int n = Integer.parseInt(scanner.nextLine().trim());
                int variant = n % 4 + 1;
                System.out.println("Ваш вариант: " + variant);
                Charts.show(new Charts.ListWrapper(players), variant);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
