package org.nitor112221.UI;

import org.nitor112221.core.MashupBuilder;

import javax.swing.*;
import java.util.List;

public class FilterBlock extends FilterBlockDesign {
    public FilterBlock(int id, MainWindow main, DefaultListModel<MainWindowDesign.ProblemDisplayItem> listModel) {
        super(id, main, listModel);
    }

    @Override
    protected void generate(boolean showMessage) {
        int count = (int) problemCountSpinner.getValue();
        MashupBuilder mb = new MashupBuilder();
        mb.setFilter(ratingFilter);
        mb.setFilter(indexDivFilter);
        mb.setFilter(containsTagsFilter);
        mb.setFilter(notContainsTagsFilter);
        mb.setNumProblem(count);

        List<MainWindowDesign.ProblemDisplayItem> newProblems = mb.build()
                .getProblems()
                .stream()
                .map((problem) -> new MainWindowDesign.ProblemDisplayItem(id, problem.getContestId(), problem.getIndex(), problem.getName()))
                .toList();

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