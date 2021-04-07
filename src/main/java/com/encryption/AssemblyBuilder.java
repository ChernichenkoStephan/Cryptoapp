package com.encryption;

import com.encryption.Encryption.BlowfishScrambler;
import com.encryption.Encryption.LSBScrambler;
import com.encryption.Encryption.RSAScrambler;
import com.encryption.Encryption.PlainScrambler;
import com.encryption.Model.BMPFileService;
import com.encryption.Model.CodeFileService;
import com.encryption.View.*;
import com.encryption.ViewModel.*;

import java.awt.*;
import java.util.logging.Level;

public class AssemblyBuilder {

    public enum BuildType {
        FULL_DEBUG, FULL_PRODUCTION, VIEW_DEBUG, LOGIC_DEBUG
    }

    public static MainWindow getMainWindow(BuildType type) {
        setLogType(type);

        MainWindow window           = MainWindow.defaultWindow;
        MenuView menuView           = new MenuView(window);

        window.getPanel().add(ViewType.MENU_VIEW.name(), menuView);

        window.getPanel().add(ViewType.SIMPLE_VIEW.name(),   buildSimple(window));
        window.getPanel().add(ViewType.RSA_VIEW.name(),      buildRSA(window));
        window.getPanel().add(ViewType.LSB_VIEW.name(),      buildLSB(window));
        window.getPanel().add(ViewType.BLOWFISH_VIEW.name(), buildBlowfish(window));

        return window;
    }

    private static Component buildSimple(MainWindow window) {
        PlainView plainView = new PlainView();
        PlainViewModel plainViewModel = new PlainViewModel(plainView);

        connectCodeView(window, plainView, plainViewModel);
        return plainView;
    }

    private static Component buildRSA(MainWindow window) {
        RSAView rsaView                 = new RSAView();
        RSAViewModel rsaViewModel       = new RSAViewModel(rsaView);

        connectCodeView(window, rsaView, rsaViewModel);
        rsaView.getGenKeyButton().addActionListener(rsaViewModel);
        return rsaView;
    }

    private static Component buildLSB(MainWindow window) {
        LSBView lsbView             = new LSBView();
        LSBViewModel lsbViewModel   = new LSBViewModel(lsbView);

        connectCodeView(window, lsbView, lsbViewModel);
        return lsbView;
    }

    private static Component buildBlowfish(MainWindow window) {
        BlowfishView blowfishView            = new BlowfishView();
        BlowfishViewModel blowfishViewModel  = new BlowfishViewModel(blowfishView);

        connectCodeView(window, blowfishView, blowfishViewModel);
        return blowfishView;
    }

    private static void connectCodeView(MainWindow window, CodeView view, ViewModel vModel) {
        view.getMenuButton().addActionListener(window);
        view.getMenuButton().addActionListener(vModel);
        view.getCodeButton().addActionListener(vModel);
        view.addActionListener(vModel);
    }

    private static void setLogType(BuildType type) {
        switch (type) {
            case FULL_DEBUG         -> performLogType(Level.ALL, Level.ALL);
            case FULL_PRODUCTION    -> performLogType(Level.OFF, Level.OFF);
            case VIEW_DEBUG         -> performLogType(Level.ALL, Level.OFF);
            case LOGIC_DEBUG        -> performLogType(Level.OFF, Level.ALL);
        }
    }

    private static void performLogType(Level viewLevel, Level logicLevel) {
        CodeView.setLoggerLevel(viewLevel);
        PlainView.setLoggerLevel(viewLevel);
        RSAView.setLoggerLevel(viewLevel);
        LSBView.setLoggerLevel(viewLevel);
        BlowfishView.setLoggerLevel(viewLevel);

        CodeViewModel.setLoggerLevel(logicLevel);
        PlainViewModel.setLoggerLevel(logicLevel);
        RSAViewModel.setLoggerLevel(logicLevel);
        LSBViewModel.setLoggerLevel(logicLevel);
        BlowfishViewModel.setLoggerLevel(logicLevel);

        PlainScrambler.setLoggerLevel(logicLevel);
        RSAScrambler.setLoggerLevel(logicLevel);
        LSBScrambler.setLoggerLevel(logicLevel);
        BlowfishScrambler.setLoggerLevel(logicLevel);

        CodeFileService.setLoggerLevel(logicLevel);
        BMPFileService.setLoggerLevel(logicLevel);
    }

}

