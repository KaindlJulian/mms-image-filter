package ui;

import java.io.File;

/**
 * Singleton to hold a file
 */
public enum InputImageArg {
    INSTANCE;

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
