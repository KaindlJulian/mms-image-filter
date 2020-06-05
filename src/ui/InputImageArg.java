package ui;

import java.io.File;

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
