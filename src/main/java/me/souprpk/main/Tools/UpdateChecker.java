package me.souprpk.main.Tools;

import me.souprpk.main.Banker;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private Banker banker;
    private final int resourceId;

    public UpdateChecker(Banker banker, int resourceId){
        this.banker = banker;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer){
        Bukkit.getScheduler().runTaskAsynchronously(this.banker, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                banker.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}
