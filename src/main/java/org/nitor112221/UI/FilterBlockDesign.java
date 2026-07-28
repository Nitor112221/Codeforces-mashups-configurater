package org.nitor112221.UI;

import lombok.Getter;
import org.nitor112221.dto.TagEnum;
import org.nitor112221.dto.ContestTypeEnum;
import org.nitor112221.filters.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.Set;

public abstract class FilterBlockDesign {
    @Getter
    protected final int id;
    @Getter
    private JPanel panel;

    // Состояние фильтров
    protected FilterProblemWithRatingFromXToY ratingFilter = null;
    protected FilterProblemFromXtoYFromDivZ indexDivFilter = null;
    protected FilterProblemContainsTags containsTagsFilter = null;
    protected FilterProblemNotContainsTags notContainsTagsFilter = null;

    // Хранилище для выбранных тегов
    private final Set<TagEnum> containsTags = new HashSet<>();
    private final Set<TagEnum> notContainsTags = new HashSet<>();

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
                BorderFactory.createTitledBorder(""),
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
        addRating.setEnabled(ratingFilter == null);

        JButton addIndexDiv = new JButton("Индекс X-Y из Div Z");
        addIndexDiv.addActionListener(e -> addIndexDivFilter());
        addIndexDiv.setEnabled(indexDivFilter == null);

        JButton addContains = new JButton("Содержит теги");
        addContains.addActionListener(e -> addContainsTagsFilter());
        addContains.setEnabled(containsTagsFilter == null);

        JButton addNotContains = new JButton("Не содержит теги");
        addNotContains.addActionListener(e -> addNotContainsTagsFilter());
        addNotContains.setEnabled(notContainsTagsFilter == null);

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
        generateButton.addActionListener(e -> generate(true));
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
        if (ratingFilter != null) return;
        ratingFilter = new FilterProblemWithRatingFromXToY();

        JPanel filterPanel = createFilterPanel();
        filterPanel.add(new JLabel("Рейтинг от:"));
        JTextField xField = new JTextField(5);
        filterPanel.add(xField);
        filterPanel.add(new JLabel("до:"));
        JTextField yField = new JTextField(5);
        filterPanel.add(yField);

        xField.getDocument().addDocumentListener((SimpleDocumentListener) () -> {
            String text = xField.getText().trim();
            if (text.isEmpty()) ratingFilter.setX(null);
            else {
                try { ratingFilter.setX(Integer.parseInt(text)); }
                catch (NumberFormatException ignored) { }
            }
        });
        yField.getDocument().addDocumentListener((SimpleDocumentListener) () -> {
            String text = yField.getText().trim();
            if (text.isEmpty()) ratingFilter.setY(null);
            else {
                try { ratingFilter.setY(Integer.parseInt(text)); }
                catch (NumberFormatException ignored) { }
            }
        });

        addRemoveButton(filterPanel, () -> {
            ratingFilter = null;
            enableAddButtons();
        });

        addToContainer(filterPanel);
        enableAddButtons();
    }

    // ==================== Индекс + Div ====================

    private void addIndexDivFilter() {
        if (indexDivFilter != null) return;
        indexDivFilter = new FilterProblemFromXtoYFromDivZ();

        JPanel filterPanel = createFilterPanel();
        filterPanel.add(new JLabel("Индекс от:"));
        JTextField xField = new JTextField(3);
        filterPanel.add(xField);
        filterPanel.add(new JLabel("до:"));
        JTextField yField = new JTextField(3);
        filterPanel.add(yField);

        // Валидация
        InputVerifier indexVerifier = new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText().trim();
                if (text.isEmpty()) return true;
                return text.matches("^[A-Z][1-9]?$");
            }
        };
        xField.setInputVerifier(indexVerifier);
        yField.setInputVerifier(indexVerifier);

        filterPanel.add(new JLabel("Div:"));
        JComboBox<String> divCombo = new JComboBox<>(new String[]{
                "Не выбран", "Div. 1", "Div. 2", "Div. 3", "Div. 4", "Div. 1 + Div. 2"
        });
        filterPanel.add(divCombo);

        xField.getDocument().addDocumentListener((SimpleDocumentListener) () -> {
            String text = xField.getText().trim();
            indexDivFilter.setX(text.isEmpty() ? null : text);
        });
        yField.getDocument().addDocumentListener((SimpleDocumentListener) () -> {
            String text = yField.getText().trim();
            indexDivFilter.setY(text.isEmpty() ? null : text);
        });
        divCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selected = (String) e.getItem();
                if ("Не выбран".equals(selected)) {
                    indexDivFilter.setDiv(null);
                } else {
                    indexDivFilter.setDiv(ContestTypeEnum.fromContestName(selected));
                }
            }
        });

        addRemoveButton(filterPanel, () -> {
            indexDivFilter = null;
            enableAddButtons();
        });

        addToContainer(filterPanel);
        enableAddButtons();
    }

    // ==================== Содержит теги ====================

    private void addContainsTagsFilter() {
        if (containsTagsFilter != null) return;
        containsTagsFilter = new FilterProblemContainsTags();

        containsTagCombo = createTagComboBox(containsTags);

        containsTagsContainer = new JPanel();
        containsTagsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        containsTagsContainer.setOpaque(false);

        JPanel filterPanel = createTagFilterPanel(
                "Теги (выберите из списка):",
                containsTagCombo,
                containsTagsContainer,
                this::addContainsTag,
                () -> {
                    containsTagsFilter = null;
                    containsTags.clear();
                    enableAddButtons();
                }
        );

        addToContainer(filterPanel);
        enableAddButtons();
    }

    private void addContainsTag() {
        TagEnum selected = TagEnum.fromEnglish((String) containsTagCombo.getSelectedItem());
        if (selected == null) return;
        if (containsTags.contains(selected)) return;

        containsTags.add(selected);
        addTagToContainer(containsTagsContainer, selected, containsTags, containsTagCombo);
        updateTagComboBox(containsTagCombo, containsTags);
    }

    // ==================== Не содержит теги (выпадающий список) ====================

    private void addNotContainsTagsFilter() {
        if (notContainsTagsFilter != null) return;
        notContainsTagsFilter = new FilterProblemNotContainsTags();

        notContainsTagCombo = createTagComboBox(notContainsTags);

        notContainsTagsContainer = new JPanel();
        notContainsTagsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        notContainsTagsContainer.setOpaque(false);

        JPanel filterPanel = createTagFilterPanel(
                "Исключить теги (выберите из списка):",
                notContainsTagCombo,
                notContainsTagsContainer,
                this::addNotContainsTag,
                () -> {
                    notContainsTagsFilter = null;
                    notContainsTags.clear();
                    enableAddButtons();
                }
        );

        addToContainer(filterPanel);
        enableAddButtons();
    }

    private void addNotContainsTag() {
        TagEnum selected = TagEnum.fromEnglish((String) notContainsTagCombo.getSelectedItem());
        if (selected == null) return;
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
        Container parent = panel.getParent();
        if (parent != null) {
            parent.remove(panel);
            parent.revalidate();
            parent.repaint();
        }

        mainWindow.removeFilterBlock(id);
    }

    // ==================== Вспомогательные методы для тегов ====================

    /**
     * Создаёт панель для фильтра тегов (содержит комбобокс, кнопку "+" и контейнер для выбранных тегов).
     *
     * @param title          заголовок (например, "Теги (выберите из списка):")
     * @param tagCombo       комбобокс с доступными тегами
     * @param tagsContainer  панель, куда будут добавляться выбранные теги
     * @param onAddTag       действие при нажатии кнопки "+" (добавление тега)
     * @param onRemoveFilter действие при удалении всего фильтра (нажатие крестика)
     * @return готовая панель JPanel
     */
    private JPanel createTagFilterPanel(String title, JComboBox<String> tagCombo,
                                        JPanel tagsContainer, Runnable onAddTag,
                                        Runnable onRemoveFilter) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setOpaque(false);

        panel.add(new JLabel(title));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topPanel.setOpaque(false);
        topPanel.add(tagCombo);

        JButton addBtn = new JButton("+");
        addBtn.addActionListener(e -> onAddTag.run());
        topPanel.add(addBtn);

        panel.add(topPanel);

        panel.add(tagsContainer);

        addRemoveButton(panel, onRemoveFilter);

        return panel;
    }

    private JComboBox<String> createTagComboBox(Set<TagEnum> usedTags) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (TagEnum tag : TagEnum.values()) {
            if (!usedTags.contains(tag)) {
                model.addElement(tag.getEnglish());
            }
        }
        if (model.getSize() > 0) {
            model.setSelectedItem(model.getElementAt(0));
        }

        JComboBox<String> combo = new JComboBox<>(model);
        combo.setPreferredSize(new Dimension(150, 25));
        return combo;
    }

    private void updateTagComboBox(JComboBox<String> combo, Set<TagEnum> usedTags) {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) combo.getModel();
        model.removeAllElements();

        for (TagEnum tag : TagEnum.values()) {
            if (!usedTags.contains(tag)) {
                model.addElement(tag.getEnglish());
            }
        }

        if (model.getSize() > 0) {
            model.setSelectedItem(model.getElementAt(0));
        }
    }

    private void addTagToContainer(JPanel container, TagEnum tag, Set<TagEnum> tagsSet, JComboBox<String> combo) {
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        tagPanel.setOpaque(false);

        JLabel tagLabel = new JLabel(tag.toString());
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
                    case "Рейтинг от X до Y" -> b.setEnabled(ratingFilter == null);
                    case "Индекс X-Y из Div Z" -> b.setEnabled(indexDivFilter == null);
                    case "Содержит теги" -> b.setEnabled(containsTagsFilter == null);
                    case "Не содержит теги" -> b.setEnabled(notContainsTagsFilter == null);
                }
            }
        }
    }
    // // ==================== Вспомогательный интерфейс ====================

    private interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
        void update();
        @Override default void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
        @Override default void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
        @Override default void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
    }

    // ==================== Генерация ====================
    protected abstract void generate(boolean showMessage);
}
