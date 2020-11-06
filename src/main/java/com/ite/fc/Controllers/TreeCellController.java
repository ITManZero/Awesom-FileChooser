package com.ite.fc.Controllers;

import com.ite.fc.tree.item.MyTreeItem;
import com.ite.fc.tree.util.TreeUtil;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;

public class TreeCellController extends TreeCell<MyTreeItem> {

    private Controller controller;

    public TreeCellController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void updateItem(MyTreeItem item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setWrapText(true);
            setText(getTreeItem().getValue().getName());
            setGraphic(getTreeItem().getGraphic());


            setOnMouseClicked(mouseEvent -> {
                /*
                 * handle tree selected item when you select one of cells
                 * every cell has path for it is directory so it will added to pathList
                 * we have to view the content of the new path so we will clear all items and add new items
                 */

                if (item.getPath() != null) {
                    controller.viewNewDirectory(item.getPath());
                    if (mouseEvent.getClickCount() == 2 && TreeUtil.validTreeRoot(getTreeItem()))
                        controller.generateRootChildren(getTreeItem());
                }
            });

        }
    }

}
