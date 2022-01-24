package me.souprpk.main.Commands;

import me.souprpk.main.Banker;
import me.souprpk.main.GUI.BankerGUI;
import me.souprpk.main.Tools.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankerCommand implements CommandExecutor {

    private Banker banker = Banker.getMain();
    Utilities utils = new Utilities(banker);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("banker"))
            if(sender instanceof Player) {
                try {
                    Player player = (Player) sender;
                    BankerGUI gui = new BankerGUI();
                    if(banker.getConfig().getString("main.storage-system").equals("mysql")){
                        banker.data.createPlayer(player);
                    }else{
                        banker.flatData.createPlayer(player.getUniqueId());
                    }

                    gui.createBankerInv(player);
                    player.openInventory(gui.bankerInv);
                } catch(Exception e) {
                    sender.sendMessage(utils.Translate(banker.messageConfig.getConfig().getString("mysql-error")));
                    e.printStackTrace();
                }
            }
        return false;
    }
}
