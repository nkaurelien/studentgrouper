package Outils;

import java.sql.*;
import java.util.Vector;

public class Db {

    private static Connection CON = null;
    private static PreparedStatement PS;
    private static ResultSet RS;

    // parametres de connexion
    private static final String pilote = "org.postgresql.Driver";
    private static final String user = "postgres";
    private static final String passwd = "marthe";
    private static final String url = "jdbc:postgresql://localhost:5432/etudiant";

    public Db() {
    }

    public static Connection getConnexion(String url, String user, String passwd, String Pilote) {
        try {
            Class.forName(Pilote).newInstance();
            CON = DriverManager.getConnection(url, user, passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CON;
    }

    public static Connection getConnexion() {

        return Db.getConnexion(url, user, passwd, pilote);
    }

    /**
     * Methode qui éxécute une requêtte SQl dans la base de données
     *
     * @param requeteSQl
     */
    public static void execute(String chemin, String requeteSQl) {
        try {
            CON = getConnexion();
            PS = CON.prepareStatement(requeteSQl);
            PS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                CON.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String result(String chemin, String requeteSQl) {
        String res = "";
        try {
            CON = getConnexion();
            PS = CON.prepareStatement(requeteSQl);
            RS = PS.executeQuery();

            if (RS.next()) {
                res = RS.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                CON.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static String resultConcat(String chemin, String requeteSQl) {
        String res = "";
        try {
            CON = getConnexion();
            PS = CON.prepareStatement(requeteSQl);
            RS = PS.executeQuery();
            while (RS.next()) {
                res += RS.getString(1) + "%";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                //PS.close();
                CON.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

}
