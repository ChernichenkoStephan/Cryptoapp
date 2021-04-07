package com.encryption;

import javax.swing.*;
import java.util.Arrays;
import java.util.logging.Logger;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Application {

    private static final Logger log = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            log.warning(Arrays.toString(ex.getStackTrace()) + ex.getMessage());
            ex.printStackTrace();
        } finally {
            SwingUtilities.invokeLater(() -> AssemblyBuilder.getMainWindow(AssemblyBuilder.BuildType.FULL_PRODUCTION).draw());
        }

    }


}

