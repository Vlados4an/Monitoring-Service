package ru.erma;

import ru.erma.factory.AppFactory;
import ru.erma.in.MonitoringConsole;

/**
 * This is the main class of the application.
 * It creates an instance of the MonitoringConsole and starts it.
 */
public class App {
    /**
     * The main method of the application.
     * It creates an instance of the MonitoringConsole using the AppFactory and starts the console.
     *
     * @param args command line arguments
     */
    public static void main( String[] args )
    {
        MonitoringConsole monitoringConsole = AppFactory.createApp();

        monitoringConsole.start();
    }
}