package bltip.util;

import java.io.*;

public class StreamerMalWieder {

    private static final String[] TEAMS = {"Bayern M�nchen", "Borussia Dortmund", "Hamburger SV", "Arminia Bielefeld", "Schalke 04",
            "Eintracht Frankfurt", "Bayer Leverkusen", "Alemannia Aachen", "VfB Stuttgart", "1. FC N�rnberg",
            "Borussia M�nchengladbach", "Energie Cottbus", "1. FSV Mainz 05", "VfL Bochum", "Hannover 96", "Werder Bremen",
            "VfL Wolfsburg", "Hertha BSC Berlin"};

    public static void main(String[] args) {
        StreamerMalWieder instance = new StreamerMalWieder();

        try {
            instance.doIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doIt() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File("C:/Dateien/BLTipp_2006_07/spielplan.txt"))));

        BufferedWriter writerHome = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                new File("C:/Dateien/BLTipp_2006_07/home.txt"))));

        BufferedWriter writerGuest = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
                "C:/Dateien/BLTipp_2006_07/guest.txt"))));

        String line;

        while (true) {
            line = reader.readLine();

            if (line == null) {
                break;
            } else if (line.length() > 0) {
                int pos = line.lastIndexOf('-');

                if (pos != -1) {
                    String home = line.substring(0, pos);
                    String guest = line.substring(pos + 1, line.length());

                    for (String team : TEAMS) {
                        if (home.contains(team)) {
                            writerHome.write(team + "\n");
                            System.out.println("home: " + team);
                        } else if (guest.contains(team)) {
                            writerGuest.write(team + "\n");
                            System.out.println("guest: " + team);
                        }
                    }
                } else {
                    if (line.contains(". Spieltag")) {
                        writerHome.write(line + "\n");
                        writerGuest.write("\n");
                    }
                }
            } else if (line.length() == 0) {
                writerHome.write("\n");
                writerGuest.write("\n");
            }
        }

        writerHome.flush();
        writerHome.close();
        writerGuest.flush();
        writerGuest.close();
        reader.close();
    }
}
