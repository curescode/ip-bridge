package com.curescode.bridge;

import com.curescode.bridge.swing.ServerMainWindow;
import com.curescode.bridge.system.SystemEnv;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

/**
 * client main class
 * @author Cure
 * @date 2024/01/31 20:27
 */
public class Client {

    public static void main(String[] args) {
        SystemEnv systemEnv = new SystemEnv();
        systemEnv.init();
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        new ServerMainWindow();

    }
}
