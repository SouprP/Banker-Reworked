package me.souprpk.main.Commands;

import com.google.common.collect.Maps;
import me.souprpk.main.Banker;
import me.souprpk.main.GUI.BankerGUI;
import me.souprpk.main.Tools.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

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
                        map.forEach((uuid, money) -> {
                            sender.sendMessage(Bukkit.getPlayer(UUID.fromString(uuid)).getName() + ": " + money);
                        });
                    }catch (Exception e){

                    }
                }
        }
        return false;
    }
}
