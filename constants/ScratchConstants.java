package constants;

import java.io.FileInputStream;
import java.util.Properties;

public final class ScratchConstants {

    public static String[] WORLD_NAMES = {"Scania", "Bera", "Broa", "Windia", "Khaini",
            "Bellocan", "Mardia", "Kradia", "Yellonde", "Demethos", "Galicia",
            "El Nido", "Zenith", "Arcenia", "Kastia", "Judis", "Plana", "Kalluna", "Stius", "Croa", "Medere"};
    public static final int MASTER_SERVER_CONNECTION_MAXIMUM = 50;
    public static final int MASTER_SERVER_PORT = 8483;
    public static final String MASTER_SERVER_IP = "127.0.0.1";
    public static final String LOGIN_SERVER_IP  = "127.0.0.1";
    public static final String GAME_SERVER_IP   = "127.0.0.1";
    public static final int LOGIN_SERVER_PORT = 8484;
    public static final short VERSION = 83;
    public static final String PATCH = "1";
    public static final int MAXIMUM_CONNECTIONS = 500;
    public static final int GAME_SERVER_LOAD = 200;
    public static final int NUM_WORLDS = 2;
    // Database Configuration
    public static String DB_URL = "";
    public static String DB_USER = "";
    public static String DB_PASS = "";

    static {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("//Users//jonnguyen//Documents//Repositories//Scratch//configuration.ini"));
            // SQL DATABASE
            ScratchConstants.DB_URL = p.getProperty("URL");
            ScratchConstants.DB_USER = p.getProperty("DB_USER");
            ScratchConstants.DB_PASS = p.getProperty("DB_PASS");
        } catch (Exception e) {
            System.out.println("Failed to load configuration at ScratchConstants.");
            e.printStackTrace();
        }
    }

}
