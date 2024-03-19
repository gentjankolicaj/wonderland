package io.wonderland.rh.base;

import javafx.scene.control.TreeCell;

public final class TreeCellImpl extends TreeCell<String> {

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getItem() == null ? "" : getItem());
            setGraphic(getTreeItem().getGraphic());
            if(getTreeItem() instanceof AbstractTreeItem) {
                setContextMenu(((AbstractTreeItem<?>) getTreeItem()).getMenu());
            }
        }
    }
}