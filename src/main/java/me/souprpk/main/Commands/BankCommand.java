package me.souprpk.main.Commands;

import me.souprpk.main.Banker;
import me.souprpk.main.Tools.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BankCommand implements CommandExecutor {

    private Banker banker = Banker.getMain();
    private Utilities utils = new Utilities(banker);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("bank"))
            if(args.length == 1)
                if(args[0].equalsIgnoreCase("reload")){
                    banker.reloadConfig();
                    banker.flat.reloadConfig();
                    banker.messageConfig.reloadConfig();
                    sender.sendMessage(utils.Translate(banker.messageConfig.getConfig().getString("reload-message")));
                }
        return false;
    }
}
