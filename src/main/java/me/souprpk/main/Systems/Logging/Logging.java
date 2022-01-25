package me.souprpk.main.Systems.Logging;

import me.souprpk.main.Banker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logging {

    private Banker banker;

    public Logging(Banker banker){
        this.banker = banker;
        createLog();
    }

    public void createLog() {

        File log = new File(banker.getDataFolder(), "logs");

        if(!log.exists()) {

            log.mkdir();
        }
    }

    public void log(String string){
        try {
            LocalDateTime now = LocalDateTime.now();
            String date = String.valueOf(now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth());

            File file = new File(banker.getDataFolder() + "/logs", date + ".txt");
            if(!file.exists())
                file.createNewFile();

            String hour = String.valueOf("[" + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond() + "] ");
            FileWriter write = new FileWriter(file, true);
            write.write(hour + string + "\n");
            write.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
