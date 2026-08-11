import java.io.*;

public class DeathTracker {
    private int deaths = 0;
    private final String FILE_PATH = "deaths.txt";

    public DeathTracker() {
        loadDeaths();
    }

    private void loadDeaths() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                if (line != null && !line.isEmpty()) {
                    deaths = Integer.parseInt(line);
                }
                br.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading deaths.");
        }
    }

    public void addDeath() {
        deaths++;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
            bw.write(String.valueOf(deaths));
            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving deaths.");
        }
    }

    public int getDeaths() {
        return deaths;
    }
}