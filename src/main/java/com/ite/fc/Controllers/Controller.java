package com.ite.fc.Controllers;

import com.ite.fc.Model;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.ite.fc.paths.DefaultFolders;
import com.ite.fc.tree.item.MyTreeItem;
import com.ite.fc.tree.util.TreeUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    private JFXButton refresh;

    /**
     * Variables for tree_view
     */
    @FXML
    private TreeView<MyTreeItem> tree_view;
    private TreeItem<MyTreeItem> roots;
    private TreeItem<MyTreeItem> QuickAccessRoot;
    private TreeItem<MyTreeItem> ThisPcRoot;

    /**
     * Variables for list_view
     */
    @FXML
    private ListView<File> list_view;

    @FXML
    private JFXTextField text_field;
    @FXML
    private JFXButton forwardButton;

    @FXML
    private JFXButton backwardButton;

    @FXML
    private JFXButton select_button;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton minimizeButton;

    @FXML
    private Label draggableLabel;

    @FXML
    private Label Title;

    /**
     * root
     */

    @FXML
    private AnchorPane root;

    /**
     * logic
     */
    private Model model;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        model = new Model();
        defaultSettings();

        /*
         * initialize tree_view
         */
        QuickAccessRoot = new TreeItem<>(new MyTreeItem("Quick Access", null));
        QuickAccessRoot.setExpanded(true);
        model.generateDefaultFolders(QuickAccessRoot);
        ThisPcRoot = new TreeItem<>(new MyTreeItem("This PC", null));
        ThisPcRoot.setExpanded(true);
        model.generateTwoLevelChildren(ThisPcRoot);
        roots = new TreeItem(null, null);
        roots.getChildren().addAll(QuickAccessRoot, ThisPcRoot);
        tree_view.setRoot(roots);
        tree_view.setId("tree");
        tree_view.setShowRoot(false);
        tree_view.setCellFactory(stringTreeView -> new TreeCellController(this));

        /*
         * initialize list_view
         */

        list_view.setCellFactory(listView -> new listCellController(this));
        list_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }


    @FXML
    void click_backwardButton() {
        /* @1 when you go back you have to delete all item on list and fill it with the new items
         *  @2 also the pointer has to decrement by 1 to point previous path
         *  @3 when we go back the next button should be enabled
         *  @4 if there is no back path disable back button **/

        viewDirectory(model.getPreviousPath());//1
        model.pointToPreviousPath();//2
        forwardButton.setDisable(false);//3
        if (model.getIndexOfCurrentPath() == 0) //4
            backwardButton.setDisable(true);
    }

    @FXML
    void click_forwardButton() {
        /* @1 when you go next you have to delete all item on list and fill it with the new items
         *  @2 also the pointer has to increment by 1 to point next path
         *  @3 when we go next the back button should be enabled
         *  @4 if there is no next path disable next button **/
        viewDirectory(model.getNextPath());//1
        model.pointToNextPath();//2
        backwardButton.setDisable(false);//3
        if (model.getIndexOfCurrentPath() + 1 == model.getPathListSize())//4
            forwardButton.setDisable(true);
    }

    @FXML
    void click_cancel() {
        click_close();
    }

    @FXML
    void click_select() {
        ObservableList<File> selectedFiles =this.list_view.getSelectionModel().getSelectedItems();
        if (selectedFiles.size() == 1 && selectedFiles.get(0).isDirectory()) {
            viewNewDirectory(selectedFiles.get(0).getAbsolutePath());
            return;
        }
        selectedFiles = selectedFiles.filtered(file -> !file.isDirectory());
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
        List<File> files = selectedFiles;
        files.forEach((file -> System.out.println(file.getName())));
        this.list_view.getSelectionModel().clearSelection();
    }

    @FXML
    void click_close() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void click_minimize() {
        Stage stage = (Stage) root.getScene().getWindow();
        if (!stage.isIconified())
            stage.setIconified(true);
    }

    @FXML
    void click_refresh() {
        viewDirectory(model.getCurrentPath());
        tree_view.getSelectionModel().getSelectedItem().getValue().setHasChildren(false);
        if (tree_view.getSelectionModel().getSelectedItem() != null)
            generateRootChildren(tree_view.getSelectionModel().getSelectedItem());
    }

    private  double xOffset;
    private  double yOffset;

    @FXML
    void dragging_stage(MouseEvent mouseEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() + xOffset);
        stage.setY(mouseEvent.getScreenY() + yOffset);
    }

    @FXML
    void pressing_stage(MouseEvent mouseEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        xOffset = stage.getX() - mouseEvent.getScreenX();
        yOffset = stage.getY() - mouseEvent.getScreenY();
    }


    void generateRootChildren(TreeItem<MyTreeItem> treeRoot) {
        model.generateRootItems(treeRoot);
    }


    void selectButtonStatus(boolean status) {
        select_button.setDisable(status);
    }


    void viewNewDirectory(String path) {
        boolean notCurrent = addNewPath(path);
        if (notCurrent)
            viewDirectory(path);
    }

    /*
     * private modifiers
     */

    private boolean addNewPath(String newPath) {
        boolean notEqualCurrentPath = !(model.getCurrentPath().equals(newPath));
        boolean notEqualNextPath = !model.getNextPath().equals(newPath);
        boolean addable = notEqualCurrentPath && notEqualNextPath;

        if (addable)
            model.addPath(newPath);

        if (notEqualCurrentPath) {
            model.pointToNextPath();
            backwardButton.setDisable(false);
        }

        if (model.getLastPath().equals(newPath))
            forwardButton.setDisable(true);


        /*
         * trimming list when the new path not equal to next path
         */

        if (notEqualCurrentPath)
            model.trimPathList();

        return notEqualCurrentPath;
    }

    private void viewDirectory(String path) {
        text_field.setText(path);
        list_view.getItems().clear();
        list_view.setItems(model.getListItems(path));
    }

    private void defaultSettings() {
        model.resetSettings();
        backwardButton.setDisable(true);
        forwardButton.setDisable(true);
        selectButtonStatus(true);
        viewDirectory(model.getInitialPath());
    }


    /*
     * public modifiers
     */


    public void setTitle(String title) {
        Title.setText(title);
    }


    public void setFileExtFilter(boolean showFolder, String... extFilters) {
        model.addFileExtFilters(showFolder, extFilters.length == 0 ? null : extFilters);
        defaultSettings();
    }

    public void setInitialPath(DefaultFolders initialFolder) {
        model.setInitialPath(initialFolder.getPath());
    }

    public void intiStageEvent() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setOnHidden(event -> defaultSettings());
    }
}
