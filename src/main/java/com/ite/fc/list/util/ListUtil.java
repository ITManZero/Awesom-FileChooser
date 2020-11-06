package com.ite.fc.list.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class ListUtil {

    private static final String[] ignoredFiles = {"desktop.ini"};
    private String[] filterExtensions;
    private boolean showFolders = false;

    public ObservableList<File> getItems(String path) {
        ObservableList<File> items = FXCollections.observableArrayList();

        /*
         * in first case there is no file extension filter
         * we will add all files except folders we have to check if user want to show
         * folders or not
         * before going to condition let's talk about FileFilter interface
         * this interface allow us to filter file list in specific folder
         * the function returning false for rejected case
         * and true for accepted case
         * so in this situation the base case
         * when we don't need to show folders expect that we will show files and folders
         *
         * so let say we don't need to show folder
         * then first half of condition will be true
         * now we will check the current file if it is directory or not
         * also let say the current file is directory
         * the conditions is fulfilled returning false means we will not add this file to list
         *
         * when we set showFolder to true
         * the condition is exceeded
         * returning true means we will add this file to list
         */
        if (filterExtensions == null)
            Collections.addAll(items,
                    Objects.requireNonNull(new File(path).listFiles(file ->
                            !ignoredFiles(file.getName()) && !(!showFolders && file.isDirectory()))));
        else
            Collections.addAll(items,
                    Objects.requireNonNull(new File(path).listFiles(file -> !ignoredFiles(file.getName()) && ((showFolders && file.isDirectory()) ||
                            (!file.isDirectory() && checkExtension(getFileExtension(file.getName())))))));

        return items;
    }

    private boolean checkExtension(String fileExtension) {
        return Arrays.stream(filterExtensions).anyMatch(extension -> extension.equals(fileExtension));
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.'));
    }

    private boolean ignoredFiles(String fileName) {
        return Arrays.stream(ignoredFiles).anyMatch(ignoredFileName -> fileName.equals(ignoredFileName));
    }

    public void setFilterExtensions(boolean showFolders, String[] filterExtensions) {
        this.filterExtensions = filterExtensions;
        this.showFolders = showFolders;
    }
}


