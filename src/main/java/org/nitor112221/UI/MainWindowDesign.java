package org.nitor112221.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.net.URISyntaxException;
import java.io.IOException;

public abstract class MainWindowDesign extends JFrame {

    protected static final String CARD_INIT = "INIT";
    protected static final String CARD_ERROR = "ERROR";
    protected static final String CARD_MAIN = "MAIN";

    protected JPanel mainPanel;
    protected JLabel errorMessageLabel;

    // Компоненты основного экрана
    protected JList<ProblemDisplayItem> mashupList;
    protected DefaultListModel<ProblemDisplayItem> listModel;
    protected JPanel filtersContainer;

    protected final List<FilterBlock> filterBlocks = new ArrayList<>();
    protected int blockCounter = 0;

    public MainWindowDesign() {
        super("Codeforces mashups configurator");
        setBounds(500, 300, 1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        initComponents();
    }

    protected void initComponents() {
        mainPanel = new JPanel(new CardLayout());

        // --- Экран инициализации ---
        JPanel initPanel = new JPanel(new BorderLayout());
        initPanel.setBackground(new Color(240, 248, 255));
        JLabel initLabel = new JLabel("Инициализация, пожалуйста, подождите...", SwingConstants.CENTER);
        initLabel.setFont(new Font("Dialog", Font.BOLD, 36));
        initLabel.setForeground(Color.DARK_GRAY);
        initPanel.add(initLabel, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(400, 30));
        JPanel progressWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        progressWrapper.add(progressBar);
        initPanel.add(progressWrapper, BorderLayout.SOUTH);

        mainPanel.add(initPanel, CARD_INIT);

        // --- Экран ошибки ---
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(new Color(255, 240, 240));
        errorMessageLabel = new JLabel("", SwingConstants.CENTER);
        errorMessageLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        errorMessageLabel.setForeground(Color.RED);
        errorPanel.add(errorMessageLabel, BorderLayout.CENTER);

        JButton backToMainFromError = new JButton("Вернуться на главную");
        backToMainFromError.addActionListener(e -> onBackFromError());
        JPanel errorButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorButtonPanel.add(backToMainFromError);
        errorPanel.add(errorButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(errorPanel, CARD_ERROR);

        // --- Основной экран ---
        JPanel mainInterfacePanel = new JPanel(new BorderLayout(10, 10));
        mainInterfacePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainInterfacePanel.setBackground(new Color(245, 245, 245));

        // Левая панель: список задач
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(new Color(230, 240, 250));
        leftPanel.setBorder(new TitledBorder("Список мэшапов"));

        listModel = new DefaultListModel<>();
        mashupList = new JList<>(listModel);
        mashupList.setFont(new Font("Dialog", Font.PLAIN, 14));
        mashupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Кастомный рендерер для отображения задач
        mashupList.setCellRenderer(new ProblemListRenderer());

        JScrollPane listScroll = new JScrollPane(mashupList);
        leftPanel.add(listScroll, BorderLayout.CENTER);

        mainInterfacePanel.add(leftPanel, BorderLayout.WEST);

        // Правая панель: фильтры
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBackground(new Color(250, 250, 250));
        rightPanel.setBorder(new TitledBorder("Фильтры"));

        filtersContainer = new JPanel();
        filtersContainer.setLayout(new BoxLayout(filtersContainer, BoxLayout.Y_AXIS));
        filtersContainer.setBackground(new Color(250, 250, 250));
        JScrollPane filterScroll = new JScrollPane(filtersContainer);
        filterScroll.setBorder(null);
        filterScroll.getVerticalScrollBar().setUnitIncrement(16);
        rightPanel.add(filterScroll, BorderLayout.CENTER);

        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(new Color(250, 250, 250));

        JButton addFilterBlockButton = new JButton("+ Добавить блок фильтров");
        addFilterBlockButton.setFont(new Font("Dialog", Font.BOLD, 14));
        addFilterBlockButton.setBackground(new Color(100, 200, 100));
        addFilterBlockButton.setForeground(Color.WHITE);
        addFilterBlockButton.setFocusPainted(false);
        addFilterBlockButton.addActionListener(e -> onAddFilterBlock());

        JButton generateAllButton = new JButton("Сгенерировать все мэшапы");
        generateAllButton.setFont(new Font("Dialog", Font.BOLD, 14));
        generateAllButton.setBackground(new Color(70, 130, 180));
        generateAllButton.setForeground(Color.WHITE);
        generateAllButton.setFocusPainted(false);
        generateAllButton.addActionListener(e -> onGenerateAll());

        controlPanel.add(addFilterBlockButton);
        controlPanel.add(generateAllButton);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        mainInterfacePanel.add(rightPanel, BorderLayout.CENTER);

        mainPanel.add(mainInterfacePanel, CARD_MAIN);

        setContentPane(mainPanel);
    }

    protected abstract void onBackFromError();
    protected abstract void onAddFilterBlock();
    protected abstract void onGenerateAll();

    // ---------- Навигация ----------
    public void showInit() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, CARD_INIT);
    }

    public void showMain() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, CARD_MAIN);
    }

    public void showError(String message) {
        errorMessageLabel.setText("<html><center>" + message + "</center></html>");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, CARD_ERROR);
    }

    // ---------- Внутренний класс для отображения задачи ----------
        public record ProblemDisplayItem(int blockId, int contestId, String index, String name) {

            @Override
            public String toString() {
                return String.format(
                        "%d%s %s</html>",
                        contestId, index, name
                );
            }

            // TODO: почему то lombok не хочет нормально работать с этим полем
            public int getBlockId() {
                return blockId;
        }
    }

    // ---------- Рендерер для списка задач ----------
    private static class ProblemListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            if (value instanceof ProblemDisplayItem item) {
                label.setText("<html>" + item.toString() + "</html>");
            }

            return label;
        }
    }
}