package com.encryption.Model;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.encryption.Utils.clenchTo32b;
import static com.encryption.Utils.unclenchFrom32b;

public class CodeFileService implements FileWorker {

    private static final Logger log = Logger.getLogger(CodeFileService.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    protected File file;

    public CodeFileService(String path) throws IllegalArgumentException {
        file = new File(path);
    }

    public CodeFileService(File file) throws IllegalArgumentException {
        this.file = file;
    }

    synchronized public List<Integer> read() {
        ArrayList<Integer> list = new ArrayList<>(2000);
        try(InputStream is = new FileInputStream(file)) {
            int c;
            while ((c = is.read()) != -1) {
                list.add(c);
            }
            return list;
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
            return list;
        }
    }

    synchronized public String readString() {
        try(FileReader reader = new FileReader(file)) {
            final StringBuilder builder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
            return builder.toString();
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
            return "";
        }
    }

    synchronized public List<Byte> readBytes() {
        final ArrayList<Byte> list = new ArrayList<>(2000);
        try(InputStream is = new FileInputStream(file)) {
            int c;
            while ((c = is.read()) != -1) {
                list.add((byte) c);
            }
            return list;
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
            return list;
        }
    }

    synchronized public void write(String data) {
        try(Writer writer = new FileWriter(file)) {
            writer.write(data);
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    synchronized public void writeBites(List<Integer> data) {
        try(OutputStream os = new FileOutputStream(file)) {
            for (int i: data) {
                os.write(i);
            }
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    synchronized public void writeCoded(List<Integer> data) {
        try(OutputStream os = new FileOutputStream(file)) {
            for (Integer datum : data) {
                byte[] intInBytes = new byte[4];
                unclenchFrom32b(datum, intInBytes, 0);
                os.write(intInBytes);
            }
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    synchronized public List<Integer> readCoded() {
        final ArrayList<Integer> list = new ArrayList<>(100);
        try(InputStream is = new FileInputStream(file)) {
            int c;
            int i = 0;
            final byte[] intInBytes = new byte[4];

            while ((c = is.read()) != -1) {
                intInBytes[i] = (byte) c;
                if (i == 3) {
                    list.add(clenchTo32b(intInBytes, 0));
                    i = 0;
                } else
                    i++;
            }
            return list;
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
            return list;
        }

    }

    public int getFileLength() {
        final String path = file.getPath();
        try(FileInputStream file = new FileInputStream(path)) {
            return file.available();
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
            return -1;
        }
    }

    synchronized public void renameFile(String name) {
        final File newName = new File(file.getParent() + "/" + name);
        if(!file.renameTo(newName)) log.warning("Error. Rename was unsuccessful!");
    }

    public String getExtension() {
        return file.getName().substring(file.getName().lastIndexOf("."));
    }

    synchronized public void changeExtension(String newExt) {
        final String oldExt  = getExtension();

        final String newName = file.getName().replace(oldExt, newExt);
        final File newAbsName = new File(file.getParent() + "/" + newName);
        if(!file.renameTo(newAbsName)) log.warning("Error. Rename was unsuccessful!");
    }

    @Override
    public void clear() {
        try(OutputStream os = new FileOutputStream(file)) {
            os.write(0);
        } catch (IOException e) {
            log.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void createFile(File file) {
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            log.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public File createKeyFile() {
        final String[] route = file.getName().split("/");
        final String keyFileName = route[route.length - 1].replace('.', '_');
        final File keyFile = new File(file.getParent() + "/" + keyFileName + ".CKey");
        this.createFile(keyFile);
        return keyFile;
    }

    private boolean checkExtension(String extension) {
        return extension.split("\\.").length == 2;
    }

    public static void main(String[] args) {
        CodeFileService fs = new CodeFileService("/Users/stephansavchenko/Desktop/CryptoappTests/obj.txt");

        List<Integer> dataToWrite = List.of((int)'a', (int)'Z', (int)'а',(int)'Я');
        fs.writeCoded(dataToWrite);
        List<Integer> res = fs.readCoded();

        for (int i = 0; i < 4; i++) {
            System.out.println(dataToWrite.get(i) + "->" + res.get(i));
        }
    }

}
