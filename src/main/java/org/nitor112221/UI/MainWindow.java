package org.nitor112221.UI;

import javax.swing.*;

public class MainWindow extends MainWindowDesign {

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
        for (FilterBlock block : filterBlocks) {
            block.generate(false);
        }
        JOptionPane.showMessageDialog(this,
                "Все мэшапы перегенерированы!",
                "Генерация завершена",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void removeFilterBlock(int id) {
        for (int i = 0; i < filterBlocks.size(); i++) {
            if (filterBlocks.get(i).getId() == id) {
                filterBlocks.remove(id);
                return;
            }
        }
    }
}