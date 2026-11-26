package ru.championship;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public final class Charts {
    private Charts() {}

    public static void show(ListWrapper data, int variant) {
        switch (variant) {
            case 1 -> showPositionShare(data);
            case 2 -> showTopTenTeamValues(data);
            case 3 -> showForwardGoalsVsCost(data);
            case 4 -> showNationalityShare(data);
            default -> throw new IllegalArgumentException("Вариант должен быть от 1 до 4.");
        }
    }

    private static void showPositionShare(ListWrapper data) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        data.players().stream()
                .collect(Collectors.groupingBy(p -> p.position().getRussianName(), Collectors.counting()))
                .forEach(dataset::setValue);
        display(ChartFactory.createPieChart("Доля игроков по позициям", dataset, true, true, false));
    }

    private static void showTopTenTeamValues(ListWrapper data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.players().stream()
                .collect(Collectors.groupingBy(Player::team, Collectors.summingLong(Player::transferCost)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> dataset.addValue(e.getValue(), "Трансферная стоимость", e.getKey()));
        display(ChartFactory.createBarChart("Топ-10 команд по трансферной стоимости",
                "Команда", "Суммарная стоимость", dataset));
    }

    private static void showForwardGoalsVsCost(ListWrapper data) {
        XYSeries series = new XYSeries("Нападающие");
        data.players().stream()
                .filter(p -> p.position() == Position.FORWARD)
                .sorted(Comparator.comparingLong(Player::transferCost))
                .forEach(p -> series.add(p.transferCost(), p.goals()));
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        display(ChartFactory.createXYLineChart("Голы в зависимости от трансферной стоимости",
                "Трансферная стоимость", "Количество голов", dataset));
    }

    private static void showNationalityShare(ListWrapper data) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        data.players().stream()
                .collect(Collectors.groupingBy(Player::nationality, Collectors.counting()))
                .forEach(dataset::setValue);
        display(ChartFactory.createPieChart("Доля игроков по странам", dataset, true, true, false));
    }

    private static void display(JFreeChart chart) {
        ChartFrame frame = new ChartFrame(chart.getTitle().getText(), chart);
        frame.pack();
        frame.setVisible(true);
    }

    public record ListWrapper(java.util.List<Player> players) {}
}
