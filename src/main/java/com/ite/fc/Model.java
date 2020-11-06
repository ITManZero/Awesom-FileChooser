package com.ite.fc;

import com.ite.fc.list.util.ListUtil;
import com.ite.fc.paths.DefaultFolders;
import com.ite.fc.tree.item.MyTreeItem;
import com.ite.fc.tree.util.TreeUtil;
import com.sun.jna.platform.win32.KnownFolders;
import com.sun.jna.platform.win32.Shell32Util;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.ArrayList;

public class Model {

    private static final String defaultPath = Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_Desktop);
    private String initialPath;
    private ObservableList<File> selectedFiles;
    private ArrayList<String> pathList;
    private int indexOfCurrentPath;

    private TreeUtil treeUtil;
    private ListUtil listUtil;


    public Model() {
        resetSettings();
        treeUtil = new TreeUtil();
        listUtil = new ListUtil();
    }


    public void addPath(String path) {
        pathList.add(path);
    }

    public void pointToNextPath() {
        indexOfCurrentPath++;
    }


    public void pointToPreviousPath() {
        indexOfCurrentPath++;
    }

    public void trimPathList() {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < indexOfCurrentPath; i++)
            temp.add(pathList.get(i));
        temp.add(pathList.get(pathList.size() - 1));
        pathList = temp;
    }

    public void resetSettings() {
        initialPath = defaultPath;
        pathList = new ArrayList();
        this.addPath(initialPath);
        indexOfCurrentPath = 0;
        selectedFiles = null;
    }

    public int getPathListSize() {
        return pathList.size();
    }

    public ObservableList<File> getListItems(String path) {
        return listUtil.getItems(path);
    }

    public void addFileExtFilters(boolean showFolders, String[] filterExtensions) {
        listUtil.setFilterExtensions(showFolders, filterExtensions);
    }

    public void setInitialPath(String initialPath) {
        this.initialPath = initialPath;
    }

    public String getInitialPath() {
        return initialPath;
    }

    public String getNextPath() {
        return indexOfCurrentPath + 1 < pathList.size() ? pathList.get(indexOfCurrentPath + 1) : getCurrentPath();
    }

    public String getPreviousPath() {
        return pathList.get(indexOfCurrentPath - 1);
    }


    public String getCurrentPath() {
        return pathList.get(indexOfCurrentPath);
    }

    public String getLastPath() {
        return pathList.get(pathList.size() - 1);
    }

    public int getIndexOfCurrentPath() {
        return indexOfCurrentPath;
    }


    public void generateTwoLevelChildren(TreeItem<MyTreeItem> treeRoot) {
        generateDefaultFolders(treeRoot); //<----||
        //     ||--- DefaultFolders
        generateLocalDisks(treeRoot);   //<----||

        generateOneLevelChildren(treeRoot); //<---- OneLevelChildren
    }

    public void generateDefaultFolders(TreeItem<MyTreeItem> treeRoot) {
        for (DefaultFolders shellFolder : DefaultFolders.values())
            if (new File(shellFolder.getPath()).listFiles() != null)
                treeRoot.getChildren().add(new TreeItem(new MyTreeItem(shellFolder.getName(),
                        shellFolder.getPath()), TreeUtil.getIcon(shellFolder.getPath())));
    }

    /**
     * File.listRoot returns local disks on current computer ex: C://
     */
    private void generateLocalDisks(TreeItem<MyTreeItem> treeRoot) {
        for (File file : File.listRoots())
            treeRoot.getChildren().add(new TreeItem(new MyTreeItem(file.getPath(),
                    file.getPath()), TreeUtil.getIcon(file.getPath())));
    }

    private void generateOneLevelChildren(TreeItem<MyTreeItem> treeRoot) {
        for (TreeItem<MyTreeItem> currentRoot : treeRoot.getChildren())
            generateRootItems(currentRoot);
    }

    public void generateRootItems(TreeItem<MyTreeItem> treeRoot) {
        TreeUtil.generateRootItems(treeRoot, treeRoot.getValue().getPath());
    }
}
