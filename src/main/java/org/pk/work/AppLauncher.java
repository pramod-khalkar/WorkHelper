package org.pk.work;

import java.awt.AWTException;
import org.pk.work.ui.MainWindow;

/**
 * Date: 21/12/21
 * Time: 5:40 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public class AppLauncher {

    public static void main(String[] args) throws AWTException {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}
