package org.nitor112221.UI;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class FilterBlock extends FilterBlockDesign{
    public FilterBlock(int id, MainWindow main, DefaultListModel<MainWindowDesign.ProblemDisplayItem> listModel) {
        super(id, main, listModel);
    }

    @Override
    protected void generate(boolean showMessage) {
        int count = (int) problemCountSpinner.getValue();

        // TODO: заменить на MashupBuilder
        // Сейчас заглушка
        List<MainWindowDesign.ProblemDisplayItem> newProblems = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String index = String.valueOf((char)('A' + i));
            newProblems.add(new MainWindowDesign.ProblemDisplayItem(id,
                    String.valueOf(2244 + id),
                    index,
                    "Задача " + index + " (блок #" + id + ")"
            ));
        }

        // Удаляем все старые задачи этого блока
        for (int i = listModel.getSize() - 1; i >= 0; i--) {
            MainWindowDesign.ProblemDisplayItem item = listModel.get(i);
            if (item.getBlockId() == id) {
                listModel.remove(i);
            }
        }

        // Добавляем новые задачи
        for (int i = 0; i < count; i++) {
            listModel.addElement(newProblems.get(i));
        }

        if (showMessage) {
            JOptionPane.showMessageDialog(mainWindow,
                    "Мэшап для блока #" + id + " сгенерирован (" + count + " задач).",
                    "Генерация",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}