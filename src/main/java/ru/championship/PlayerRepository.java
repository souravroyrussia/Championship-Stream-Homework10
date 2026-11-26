package ru.championship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class PlayerRepository {
    private PlayerRepository() {}

    public static List<Player> loadPlayers() throws IOException {
        InputStream stream = PlayerRepository.class.getResourceAsStream("/fakePlayers.csv");
        if (stream == null) {
            throw new IOException("Не найден файл fakePlayers.csv в resources.");
        }

        List<Player> players = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            boolean header = true;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (header) {
                    header = false;
                    continue;
                }
                if (line.isBlank()) continue;

                String[] fields = line.split(";", -1);
                if (fields.length != 12) {
                    throw new IOException("Ошибка формата CSV в строке " + lineNumber
                            + ": ожидалось 12 столбцов, получено " + fields.length);
                }

                players.add(new Player(
                        fields[0].trim(),
                        fields[1].trim(),
                        fields[2].trim(),
                        Position.fromValue(fields[3]),
                        fields[4].trim(),
                        fields[5].trim().isEmpty() ? null : fields[5].trim(),
                        Long.parseLong(fields[6].trim()),
                        Integer.parseInt(fields[7].trim()),
                        Integer.parseInt(fields[8].trim()),
                        Integer.parseInt(fields[9].trim()),
                        Integer.parseInt(fields[10].trim()),
                        Integer.parseInt(fields[11].trim())
                ));
            }
        }
        return players;
    }
}
