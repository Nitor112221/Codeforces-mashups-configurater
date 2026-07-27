package org.nitor112221.UI;

import lombok.Getter;
import org.nitor112221.dto.TagEnum;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class FilterBlockDesign {
    @Getter
    protected final int id;
    @Getter
    private JPanel panel;

    // Состояние фильтров
    protected boolean ratingAdded = false;
    protected boolean indexDivAdded = false;
    protected boolean containsTagsAdded = false;
    protected boolean notContainsTagsAdded = false;

    // Хранилище для выбранных тегов
    private final Set<String> containsTags = new HashSet<>();
    private final Set<String> notContainsTags = new HashSet<>();

    // Компоненты для тегов
    private JPanel containsTagsContainer;
    private JPanel notContainsTagsContainer;
    private JComboBox<String> containsTagCombo;
    private JComboBox<String> notContainsTagCombo;

    // Поле для количества задач
    protected JSpinner problemCountSpinner;

    // Ссылка на модель списка для обновления
    protected final DefaultListModel<MainWindowDesign.ProblemDisplayItem> listModel;
    protected final MainWindow mainWindow;

    public FilterBlockDesign(int id, MainWindow main, DefaultListModel<MainWindowDesign.ProblemDisplayItem> listModel) {
        this.id = id;
        this.mainWindow = main;
        this.listModel = listModel;
        buildPanel();
    }

    private void buildPanel() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Блок фильтров #" + id),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(240, 248, 255));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- Поле для количества задач ---
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        countPanel.setOpaque(false);
        countPanel.add(new JLabel("Количество задач:"));
        problemCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        problemCountSpinner.setPreferredSize(new Dimension(60, 25));
        countPanel.add(problemCountSpinner);
        panel.add(countPanel);

        // --- Панель для кнопок добавления фильтров ---
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

        // --- Контейнер для добавленных фильтров ---
        JPanel filtersAddedPanel = new JPanel();
        filtersAddedPanel.setLayout(new BoxLayout(filtersAddedPanel, BoxLayout.Y_AXIS));
        filtersAddedPanel.setOpaque(false);
        panel.add(filtersAddedPanel);
        panel.putClientProperty("filtersAddedPanel", filtersAddedPanel);

        // --- Кнопка генерации ---
        JButton generateButton = new JButton("Сгенерировать мэшап");
        generateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateButton.addActionListener(e -> generateSingleMashup(true));
        panel.add(generateButton);


        // --- Кнопка удаления ---
        JButton deleteBlockButton = new JButton("Удалить блок");
        deleteBlockButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteBlockButton.setBackground(new Color(200, 100, 100));
        deleteBlockButton.setForeground(Color.WHITE);
        deleteBlockButton.addActionListener(e -> deleteBlock());
        panel.add(deleteBlockButton);
    }

    // ==================== Рейтинг ====================

    private void addRatingFilter() {
        if (ratingAdded) return;
        ratingAdded = true;

        JPanel filterPanel = createFilterPanel();
        filterPanel.add(new JLabel("Рейтинг от:"));
        JTextField xField = new JTextField(5);
        filterPanel.add(xField);
        filterPanel.add(new JLabel("до:"));
        JTextField yField = new JTextField(5);
        filterPanel.add(yField);

        addRemoveButton(filterPanel, () -> {
            ratingAdded = false;
            enableAddButtons();
        });

        addToContainer(filterPanel);
        enableAddButtons();
    }

    // ==================== Индекс + Div ====================

    private void addIndexDivFilter() {
        if (indexDivAdded) return;
        indexDivAdded = true;

        JPanel filterPanel = createFilterPanel();
        filterPanel.add(new JLabel("Индекс от:"));
        JTextField xField = new JTextField(3);
        filterPanel.add(xField);
        filterPanel.add(new JLabel("до:"));
        JTextField yField = new JTextField(3);
        filterPanel.add(yField);

        // Валидация через InputVerifier
        InputVerifier indexVerifier = new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                String text = field.getText().trim();
                if (text.isEmpty()) return true;
                return text.matches("^[A-Z][1-9]?$");
            }
        };
        xField.setInputVerifier(indexVerifier);
        yField.setInputVerifier(indexVerifier);

        // Добавляем подсказку
        xField.setToolTipText("Одна заглавная буква, опционально с цифрой (например: A, B, D1)");
        yField.setToolTipText("Одна заглавная буква, опционально с цифрой (например: C, E, D2)");

        filterPanel.add(new JLabel("Div:"));
        JComboBox<String> divCombo = new JComboBox<>(new String[]{
                "Не выбран", "Div. 1", "Div. 2", "Div. 3", "Div. 4", "Div. 1 + Div. 2"
        });
        filterPanel.add(divCombo);

        addRemoveButton(filterPanel, () -> {
            indexDivAdded = false;
            enableAddButtons();
        });

        addToContainer(filterPanel);
        enableAddButtons();
    }

    // ==================== Содержит теги (выпадающий список) ====================

    private void addContainsTagsFilter() {
        if (containsTagsAdded) return;
        containsTagsAdded = true;

        JPanel filterPanel = createFilterPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.add(new JLabel("Теги (выберите из списка):"));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topPanel.setOpaque(false);

        containsTagCombo = createTagComboBox(containsTags);
        topPanel.add(containsTagCombo);

        JButton addTagBtn = new JButton("+");
        addTagBtn.addActionListener(e -> addContainsTag());
        topPanel.add(addTagBtn);

        filterPanel.add(topPanel);

        containsTagsContainer = new JPanel();
        containsTagsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        containsTagsContainer.setOpaque(false);
        filterPanel.add(containsTagsContainer);

        addRemoveButton(filterPanel, () -> {
            containsTagsAdded = false;
            containsTags.clear();
            enableAddButtons();
        });

        addToContainer(filterPanel);
        enableAddButtons();
    }

    private void addContainsTag() {
        String selected = (String) containsTagCombo.getSelectedItem();
        if (selected == null || selected.isEmpty()) return;
        if (containsTags.contains(selected)) return;

        containsTags.add(selected);
        addTagToContainer(containsTagsContainer, selected, containsTags, containsTagCombo);
        updateTagComboBox(containsTagCombo, containsTags);
    }

    // ==================== Не содержит теги (выпадающий список) ====================

    private void addNotContainsTagsFilter() {
        if (notContainsTagsAdded) return;
        notContainsTagsAdded = true;

        JPanel filterPanel = createFilterPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.add(new JLabel("Исключить теги (выберите из списка):"));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topPanel.setOpaque(false);

        notContainsTagCombo = createTagComboBox(notContainsTags);
        topPanel.add(notContainsTagCombo);

        JButton addTagBtn = new JButton("+");
        addTagBtn.addActionListener(e -> addNotContainsTag());
        topPanel.add(addTagBtn);

        filterPanel.add(topPanel);

        notContainsTagsContainer = new JPanel();
        notContainsTagsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        notContainsTagsContainer.setOpaque(false);
        filterPanel.add(notContainsTagsContainer);

        addRemoveButton(filterPanel, () -> {
            notContainsTagsAdded = false;
            notContainsTags.clear();
            enableAddButtons();
        });

        addToContainer(filterPanel);
        enableAddButtons();
    }

    private void addNotContainsTag() {
        String selected = (String) notContainsTagCombo.getSelectedItem();
        if (selected == null || selected.isEmpty()) return;
        if (notContainsTags.contains(selected)) return;

        notContainsTags.add(selected);
        addTagToContainer(notContainsTagsContainer, selected, notContainsTags, notContainsTagCombo);
        updateTagComboBox(notContainsTagCombo, notContainsTags);
    }

    private void deleteBlock() {
        // Удаляем все задачи этого блока
        for (int i = listModel.getSize() - 1; i >= 0; i--) {
            MainWindowDesign.ProblemDisplayItem item = listModel.get(i);
            if (item.getBlockId() == id) {
                listModel.remove(i);
            }
        }
        mainWindow.removeFilterBlock(id);
    }

    // ==================== Вспомогательные методы для тегов ====================

    private JComboBox<String> createTagComboBox(Set<String> usedTags) {
        java.util.List<String> allTags = new ArrayList<>();
        for (TagEnum tag : TagEnum.values()) {
            allTags.add(tag.getEnglish());
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String tag : allTags) {
            if (!usedTags.contains(tag)) {
                model.addElement(tag);
            }
        }
        if (model.getSize() > 0) {
            model.setSelectedItem(model.getElementAt(0));
        }

        JComboBox<String> combo = new JComboBox<>(model);
        combo.setPreferredSize(new Dimension(150, 25));
        return combo;
    }

    private void updateTagComboBox(JComboBox<String> combo, Set<String> usedTags) {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) combo.getModel();
        model.removeAllElements();

        List<String> allTags = new ArrayList<>();
        for (TagEnum tag : TagEnum.values()) {
            allTags.add(tag.getEnglish());
        }

        for (String tag : allTags) {
            if (!usedTags.contains(tag)) {
                model.addElement(tag);
            }
        }

        if (model.getSize() > 0) {
            model.setSelectedItem(model.getElementAt(0));
        }
    }

    private void addTagToContainer(JPanel container, String tag, Set<String> tagsSet, JComboBox<String> combo) {
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        tagPanel.setOpaque(false);

        JLabel tagLabel = new JLabel(tag);
        tagLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        tagLabel.setBackground(new Color(220, 240, 255));
        tagLabel.setOpaque(true);
        tagLabel.setFont(new Font("Dialog", Font.PLAIN, 12));

        JButton removeTagBtn = new JButton("✕");
        removeTagBtn.setFont(new Font("Dialog", Font.BOLD, 10));
        removeTagBtn.setPreferredSize(new Dimension(20, 20));
        removeTagBtn.setBorderPainted(false);
        removeTagBtn.setFocusPainted(false);
        removeTagBtn.setBackground(new Color(255, 100, 100));
        removeTagBtn.setForeground(Color.WHITE);
        removeTagBtn.addActionListener(e -> {
            tagsSet.remove(tag);
            container.remove(tagPanel);
            container.revalidate();
            container.repaint();
            updateTagComboBox(combo, tagsSet);
        });

        tagPanel.add(tagLabel);
        tagPanel.add(removeTagBtn);
        container.add(tagPanel);
        container.revalidate();
        container.repaint();
    }

    // ==================== Общие вспомогательные методы ====================

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setOpaque(false);
        return panel;
    }

    private void addRemoveButton(JPanel filterPanel, Runnable onRemove) {
        JButton removeBtn = new JButton("✕");
        removeBtn.setFont(new Font("Dialog", Font.BOLD, 12));
        removeBtn.setPreferredSize(new Dimension(25, 25));
        removeBtn.setBorderPainted(false);
        removeBtn.setFocusPainted(false);
        removeBtn.setBackground(new Color(255, 100, 100));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.addActionListener(e -> {
            onRemove.run();
            Container parent = filterPanel.getParent();
            if (parent != null) {
                parent.remove(filterPanel);
                parent.revalidate();
                parent.repaint();
            }
        });
        filterPanel.add(removeBtn);
    }

    private void addToContainer(JPanel filterPanel) {
        JPanel container = (JPanel) panel.getClientProperty("filtersAddedPanel");
        if (container != null) {
            container.add(filterPanel);
            container.revalidate();
            container.repaint();
        }
    }

    private void enableAddButtons() {
        Component[] comps = panel.getComponents();
        for (Component c : comps) {
            if (!(c instanceof JPanel && ((JPanel) c).getComponentCount() > 0)) continue;
            for (Component btn : ((JPanel) c).getComponents()) {
                if (!(btn instanceof JButton b)) continue;
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

    // ==================== Генерация ====================
    protected abstract void generateSingleMashup(boolean showMessage);
}
