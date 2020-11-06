package com.ite.fc.tree.item;

public class MyTreeItem {

    private String name;
    private String path;
    private boolean hasChildren;

    public MyTreeItem(String name, String path) {
        this.name = name;
        this.path = path;
        hasChildren = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }



    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

}
