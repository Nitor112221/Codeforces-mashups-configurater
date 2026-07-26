package org.nitor112221.UI;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class FilterBlock {
    @Getter
    private final int id;
    @Getter
    private JPanel panel;
    private boolean ratingAdded = false;
    private boolean indexDivAdded = false;
    private boolean containsTagsAdded = false;
    private boolean notContainsTagsAdded = false;

    public FilterBlock(int id, MainWindow main, DefaultListModel<String> listModel) {
        this.id = id;
        buildPanel(main, listModel);
    }

    private void buildPanel(MainWindow main, DefaultListModel<String> listModel) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Блок фильтров #" + id),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(240, 248, 255));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Панель для кнопок добавления фильтров
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonsPanel.setOpaque(false);

        JButton addRating = new JButton("Рейтинг от X до Y");
        addRating.addActionListener(e -> addRatingFilter());
        addRating.setEnabled(!ratingAdded);

        JButton addIndexDiv = new JButton("Индекс X-Y из Div Z");
        addIndexDiv.addActionListener(e -> addIndexDivFilter());
        addIndexDiv.setEnabled(!indexDivAdded);

        JButton addContains = new JButton("Содержит теги");
        addContains.addActionListener(e -> addContainsTagsFilter());
        addContains.setEnabled(!containsTagsAdded);

        JButton addNotContains = new JButton("Не содержит теги");
        addNotContains.addActionListener(e -> addNotContainsTagsFilter());
        addNotContains.setEnabled(!notContainsTagsAdded);

        buttonsPanel.add(addRating);
        buttonsPanel.add(addIndexDiv);
        buttonsPanel.add(addContains);
        buttonsPanel.add(addNotContains);
        panel.add(buttonsPanel);

        // Контейнер для добавленных фильтров (сюда будут добавляться панели фильтров)
        JPanel filtersAddedPanel = new JPanel();
        filtersAddedPanel.setLayout(new BoxLayout(filtersAddedPanel, BoxLayout.Y_AXIS));
        filtersAddedPanel.setOpaque(false);
        panel.add(filtersAddedPanel);

        JButton generateButton = new JButton("Сгенерировать мэшап");
        generateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateButton.addActionListener(e -> generateSingleMashup(main, listModel));
        panel.add(generateButton);

        panel.putClientProperty("filtersAddedPanel", filtersAddedPanel);
    }

    private void addRatingFilter() {
        if (ratingAdded) return;
        ratingAdded = true;
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createEtchedBorder());
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Рейтинг от:"));
        JTextField xField = new JTextField(5);
        filterPanel.add(xField);
        filterPanel.add(new JLabel("до:"));
        JTextField yField = new JTextField(5);
        filterPanel.add(yField);

        JButton removeBtn = new JButton("X");
        removeBtn.addActionListener(e -> {
            ratingAdded = false;
            enableAddButtons();
            Container parent = filterPanel.getParent();
            if (parent != null) {
                parent.remove(filterPanel);
                parent.revalidate();
                parent.repaint();
            }
        });
        filterPanel.add(removeBtn);

        JPanel container = (JPanel) panel.getClientProperty("filtersAddedPanel");
        if (container != null) {
            container.add(filterPanel);
            container.revalidate();
            container.repaint();
        }
        enableAddButtons();
    }

    private void addIndexDivFilter() {
        if (indexDivAdded) return;
        indexDivAdded = true;
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createEtchedBorder());
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Индекс от:"));
        JTextField xField = new JTextField(3);
        filterPanel.add(xField);
        filterPanel.add(new JLabel("до:"));
        JTextField yField = new JTextField(3);
        filterPanel.add(yField);
        filterPanel.add(new JLabel("Div:"));
        JComboBox<String> divCombo = new JComboBox<>(new String[]{"Div. 1", "Div. 2", "Div. 3", "Div. 4", "Div. 1 + Div. 2"});
        filterPanel.add(divCombo);
        JButton removeBtn = new JButton("X");
        removeBtn.addActionListener(e -> {
            indexDivAdded = false;
            enableAddButtons();
            Container parent = filterPanel.getParent();
            if (parent != null) {
                parent.remove(filterPanel);
                parent.revalidate();
                parent.repaint();
            }
        });
        filterPanel.add(removeBtn);

        JPanel container = (JPanel) panel.getClientProperty("filtersAddedPanel");
        if (container != null) {
            container.add(filterPanel);
            container.revalidate();
            container.repaint();
        }
        enableAddButtons();
    }

    private void addContainsTagsFilter() {
        if (containsTagsAdded) return;
        containsTagsAdded = true;
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createEtchedBorder());
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Теги (через запятую):"));
        JTextField tagsField = new JTextField(20);
        filterPanel.add(tagsField);
        JButton removeBtn = new JButton("X");
        removeBtn.addActionListener(e -> {
            containsTagsAdded = false;
            enableAddButtons();
            Container parent = filterPanel.getParent();
            if (parent != null) {
                parent.remove(filterPanel);
                parent.revalidate();
                parent.repaint();
            }
        });
        filterPanel.add(removeBtn);

        JPanel container = (JPanel) panel.getClientProperty("filtersAddedPanel");
        if (container != null) {
            container.add(filterPanel);
            container.revalidate();
            container.repaint();
        }
        enableAddButtons();
    }

    private void addNotContainsTagsFilter() {
        if (notContainsTagsAdded) return;
        notContainsTagsAdded = true;
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createEtchedBorder());
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Исключить теги:"));
        JTextField tagsField = new JTextField(20);
        filterPanel.add(tagsField);
        JButton removeBtn = new JButton("X");
        removeBtn.addActionListener(e -> {
            notContainsTagsAdded = false;
            enableAddButtons();
            Container parent = filterPanel.getParent();
            if (parent != null) {
                parent.remove(filterPanel);
                parent.revalidate();
                parent.repaint();
            }
        });
        filterPanel.add(removeBtn);

        JPanel container = (JPanel) panel.getClientProperty("filtersAddedPanel");
        if (container != null) {
            container.add(filterPanel);
            container.revalidate();
            container.repaint();
        }
        enableAddButtons();
    }

    private void enableAddButtons() {
        Component[] comps = panel.getComponents();
        for (Component c : comps) {
            if (c instanceof JPanel && ((JPanel) c).getComponentCount() > 0) {
                for (Component btn : ((JPanel) c).getComponents()) {
                    if (btn instanceof JButton b) {
                        String text = b.getText();
                        switch (text) {
                            case "Рейтинг от X до Y" -> b.setEnabled(!ratingAdded);
                            case "Индекс X-Y из Div Z" -> b.setEnabled(!indexDivAdded);
                            case "Содержит теги" -> b.setEnabled(!containsTagsAdded);
                            case "Не содержит теги" -> b.setEnabled(!notContainsTagsAdded);
                        }
                    }
                }
            }
        }
    }

    private void generateSingleMashup(MainWindow main, DefaultListModel<String> listModel) {
        // Демонстрация: добавляем один мэшап в общий список
        String name = "Мэшап (блок #" + id + ")";
        listModel.addElement(name);
        JOptionPane.showMessageDialog(main,
                "Мэшап для блока #" + id + " сгенерирован.",
                "Генерация",
                JOptionPane.INFORMATION_MESSAGE);
    }
}