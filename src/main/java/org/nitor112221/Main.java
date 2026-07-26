package org.nitor112221;

import org.nitor112221.Database.Database;
import org.nitor112221.UI.MainWindow;
import org.nitor112221.core.ApiManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MainWindow mainWindow = new MainWindow();

        Database.conn();
        Database.loadContests(ApiManager.loadContests());
        Database.loadProblems(ApiManager.loadProblems());

        mainWindow.showMain();
    }
}