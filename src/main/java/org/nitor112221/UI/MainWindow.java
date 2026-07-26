package org.nitor112221.UI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends MainWindowDesign {

    private final List<FilterBlock> filterBlocksLocal = new ArrayList<>();

    public MainWindow() {
        super();
        setVisible(true);
        showInit();
    }

    @Override
    protected void onBackFromError() {
        showMain();
    }

    @Override
    protected void onDeleteSelected() {
        int idx = mashupList.getSelectedIndex();
        if (idx != -1) {
            listModel.remove(idx);
        }
    }

    @Override
    protected void onAddFilterBlock() {
        blockCounter++;
        FilterBlock block = new FilterBlock(blockCounter, this, listModel);
        filterBlocks.add(block);
        filtersContainer.add(block.getPanel());
        filtersContainer.revalidate();
        filtersContainer.repaint();
    }

    @Override
    protected void onGenerateAll() {
        // Генерируем мэшапы для всех блоков (заглушка)
        for (FilterBlock block : filterBlocks) {
            String name = "Мэшап #" + (listModel.getSize() + 1) + " (блок " + block.getId() + ")";
            listModel.addElement(name);
        }
        JOptionPane.showMessageDialog(this,
                "Сгенерировано " + filterBlocks.size() + " мэшапов!",
                "Генерация завершена",
                JOptionPane.INFORMATION_MESSAGE);
    }
}