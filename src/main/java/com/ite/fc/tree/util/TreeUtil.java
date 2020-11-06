package com.ite.fc.tree.util;

import com.ite.fc.tree.item.MyTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import sun.awt.image.IntegerComponentRaster;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.nio.IntBuffer;
import java.util.Arrays;

public class TreeUtil {

    public static void generateRootItems(TreeItem<MyTreeItem> currentRoot, String rootPath) {

        File[] currentRootItems = getDirectories(rootPath);
        if (currentRootItems == null) return;


        Arrays.stream(currentRootItems).forEach(file -> {
            TreeItem<MyTreeItem> newRootItem = new TreeItem(new MyTreeItem(file.getName(), file.getPath()), getIcon(file.getAbsolutePath()));
            currentRoot.getChildren().add(newRootItem);
        });

        currentRoot.getValue().setHasChildren(true);
    }

    private static File[] getDirectories(String rootPath) {
        DirectoryTreeItem directoryTreeItem = new DirectoryTreeItem(rootPath);
        File[] rootItems = directoryTreeItem.directoryItems;
        return directoryTreeItem.notEmptyDirectory ? rootItems : null;
    }


    private static class DirectoryTreeItem {

        private File[] directoryItems;
        private boolean notEmptyDirectory;

        private DirectoryTreeItem(String rootPath) {
            File rootFile = new File(rootPath);
            directoryItems = rootFile.listFiles(file -> file.isDirectory() && !file.getAbsoluteFile().isHidden());
            notEmptyDirectory = rootFile.length() > 0;
        }
    }

    /**
     * i found this method to get file icon on stack overflow https://stackoverflow.com/questions/46978413/how-to-get-file-icon-for-tableview-in-javafx
     *
     * @param path file path
     * @return javafx ImageView
     */
    public static ImageView getIcon(String path) {
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(new File(path));
        ImageIcon swingImageIcon = (ImageIcon) icon;
        Image awtImage = swingImageIcon.getImage();
        BufferedImage bufferedImage;
        if (awtImage instanceof BufferedImage)
            bufferedImage = (BufferedImage) awtImage;
        else {
            bufferedImage = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(awtImage, 0, 0, null);
            graphics.dispose();
        }
        return new ImageView(toFXImage(bufferedImage, null));
    }

    public static boolean validTreeRoot(TreeItem<MyTreeItem> currentItem) {

        return (currentItem != null && currentItem.getValue().getPath() != null && !currentItem.getValue().hasChildren() && currentItem.getParent().getValue().getName() != "Quick Access");
    }

    /**
     * convert swing image to fx image
     * i am using this function from javafx.embed.swing library
     */

    private static WritableImage toFXImage(BufferedImage bimg, WritableImage wimg) {
        int bw = bimg.getWidth();
        int bh = bimg.getHeight();
        switch (bimg.getType()) {
            case 2:
            case 3:
                break;
            default:
                BufferedImage converted = new BufferedImage(bw, bh, 3);
                Graphics2D g2d = converted.createGraphics();
                g2d.drawImage(bimg, 0, 0, (ImageObserver) null);
                g2d.dispose();
                bimg = converted;
        }

        int[] empty;
        if (wimg != null) {
            int iw = (int) wimg.getWidth();
            int ih = (int) wimg.getHeight();
            if (iw >= bw && ih >= bh) {
                if (bw < iw || bh < ih) {
                    empty = new int[iw];
                    PixelWriter pw = wimg.getPixelWriter();
                    PixelFormat<IntBuffer> pf = PixelFormat.getIntArgbPreInstance();
                    if (bw < iw) {
                        pw.setPixels(bw, 0, iw - bw, bh, pf, empty, 0, 0);
                    }

                    if (bh < ih) {
                        pw.setPixels(0, bh, iw, ih - bh, pf, empty, 0, 0);
                    }
                }
            } else {
                wimg = null;
            }
        }

        if (wimg == null) {
            wimg = new WritableImage(bw, bh);
        }

        PixelWriter pw = wimg.getPixelWriter();
        IntegerComponentRaster icr = (IntegerComponentRaster) bimg.getRaster();
        empty = icr.getDataStorage();
        int offset = icr.getDataOffset(0);
        int scan = icr.getScanlineStride();
        PixelFormat<IntBuffer> pf = bimg.isAlphaPremultiplied() ? PixelFormat.getIntArgbPreInstance() : PixelFormat.getIntArgbInstance();
        pw.setPixels(0, 0, bw, bh, pf, empty, offset, scan);
        return wimg;
    }
}
