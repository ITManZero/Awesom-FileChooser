package com.ite.fc.Controllers;

import com.ite.fc.tree.util.TreeUtil;
import javafx.scene.control.ListCell;

import java.io.File;

/**
 * here we are implementing our cell form
 * so we can set form to list
 *
 * File item type
 * we can represent custom type
 */

public class listCellController extends ListCell<File> {

    private Controller controller;

    public listCellController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getItem().getName());
            setGraphic(TreeUtil.getIcon(getItem().getPath()));

            /*
             * handle list clicked item when you click twice one of the cells we have to add new path to list
             * also we have to view the content of the new path so we will clear all items and add new items
             */
            setOnMouseClicked(mouseEvent -> {
                String path = item.getAbsolutePath();
                boolean isDirectory = new File(path).isDirectory();
                if (mouseEvent.getClickCount() == 2 & isDirectory) {
                    controller.viewNewDirectory(path);
                } else if (mouseEvent.getClickCount() == 1)
                    controller.selectButtonStatus(false);
            });
        }
    }
}
