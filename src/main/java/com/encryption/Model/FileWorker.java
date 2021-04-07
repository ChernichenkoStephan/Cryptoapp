package com.encryption.Model;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileWorker {
    public List<Integer> read();
    public List<Byte> readBytes();
    public void write(String data);
    public void writeBites(List<Integer> data);
    public int getFileLength();
    public void renameFile(String name);
    public String getExtension();
    public void changeExtension(String newExt);
    public void clear();
    public void createFile(File file);

    public static File createFile(String path) {
        File file = new File(path);
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            return file;
        }
    }

}
