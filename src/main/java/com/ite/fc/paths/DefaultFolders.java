package com.ite.fc.paths;

import com.sun.jna.platform.win32.Shell32Util;


public enum DefaultFolders {
    Desktop("Desktop", Shell32Util.getKnownFolderPath(com.sun.jna.platform.win32.KnownFolders.FOLDERID_Desktop)),
    Documents("Documents", Shell32Util.getKnownFolderPath(com.sun.jna.platform.win32.KnownFolders.FOLDERID_Documents)),
    Download("Download", Shell32Util.getKnownFolderPath(com.sun.jna.platform.win32.KnownFolders.FOLDERID_Downloads)),
    Music("Music", Shell32Util.getKnownFolderPath(com.sun.jna.platform.win32.KnownFolders.FOLDERID_Music)),
    Pictures("Pictures", Shell32Util.getKnownFolderPath(com.sun.jna.platform.win32.KnownFolders.FOLDERID_Pictures)),
    Videos("Videos", Shell32Util.getKnownFolderPath(com.sun.jna.platform.win32.KnownFolders.FOLDERID_Videos));

    private String name;
    private String path;

    DefaultFolders(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
