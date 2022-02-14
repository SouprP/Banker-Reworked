package me.souprpk.main.Commands;

import me.souprpk.main.Banker;
import me.souprpk.main.GUI.BankerGUI;
import me.souprpk.main.Tools.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BankerCommand implements CommandExecutor {

    private Banker banker = Banker.getMain();
    Utilities utils = new Utilities(banker);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("banker"))
            if(args.length == 0) {
                if (sender instanceof Player) {
                    try {
                        Player player = (Player) sender;
                        BankerGUI gui = new BankerGUI();
                        if (banker.getConfig().getString("main.storage-system").equals("mysql")) {
                            banker.data.createPlayer(player);
                        } else {
                            banker.flatData.createPlayer(player.getUniqueId());
                        }

                        gui.createBankerInv(player);
                        player.openInventory(gui.bankerInv);
                    } catch (Exception e) {
                        sender.sendMessage(utils.Translate(banker.messageConfig.getConfig().getString("mysql-error")));
                        e.printStackTrace();
                    }
                }
            }else if(args.length >= 1){
                if(args[0].equalsIgnoreCase("top")){
                    Map<String, BigDecimal> map;
                    if (banker.getConfig().getString("main.storage-system").equals("mysql")) {
                        map = banker.data.getTopInfo();
                    } else {
                        map = banker.flatData.getTopInfo();
                    }
                    try{
                        Map<String, BigDecimal> sorted = map.entrySet().stream().sorted(
                                Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10).collect(Collectors.toMap(
                                        Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) ->
                                 oldValue, LinkedHashMap::new));
                        Utilities utils = new Utilities(banker);
                        sender.sendMessage(utils.Translate(
                                banker.getConfig().getString("main.prefix")
                                        + banker.messageConfig.getConfig().getString("bankerTop")));
                        AtomicInteger i = new AtomicInteger();
                        sorted.forEach((k, v) -> {
                            i.getAndIncrement();
                            sender.sendMessage(utils.Translate("&a" + i + ". "
                                    + "&8" + Bukkit.getPlayer(UUID.fromString(k)).getName()
                                            + ": &a" + v + "$&8."));
                        });
                        i.set(0);
                    }catch (Exception e){

                    }
                }
        }
        return false;
    }
}
