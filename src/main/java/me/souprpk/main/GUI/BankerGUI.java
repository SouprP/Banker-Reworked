package me.souprpk.main.GUI;

import me.souprpk.main.Banker;
import me.souprpk.main.Tools.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankerGUI {

    private Inventory bankerInv;
    private Inventory depositInv;
    private Inventory withdrawInv;

    private Utilities utils;
    private Banker banker;

    private void createBankerInv(Player player){
        bankerInv = Bukkit.createInventory(player, 36, utils.Translate(utils.getConfigString("main.gui-display-name.banker-main")));
        int interestTime = banker.getMain().getConfig().getInt("every-hours");
        double interestRate = banker.getMain().getConfig().getDouble("interest-rate");

        //// Deposit
        ItemStack moneyDeposit = new ItemStack(Material.CHEST);
        List<String> loreD = new ArrayList<String>();
        ItemMeta mD = moneyDeposit.getItemMeta();
        loreD.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainDeposLore1")));
        loreD.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainDeposLore2")));
        loreD.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainDeposLore3")));
        mD.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainDeposName")));
        mD.setLore(loreD);
        moneyDeposit.setItemMeta(mD);

        //// Wyjecie
        ItemStack moneyWithdraw = new ItemStack(Material.DISPENSER);
        List<String> loreW = new ArrayList<String>();
        ItemMeta mW = moneyWithdraw.getItemMeta();
        loreW.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainWithdrawLore1")));
        loreW.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainWithdrawLore2")));
        loreW.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainWithdrawLore3")));
        mW.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainWithdrawName")));
        mW.setLore(loreW);
        moneyWithdraw.setItemMeta(mW);



        //// Amount of money in bank
        ItemStack moneyInBank = new ItemStack(Material.GOLD_INGOT);
        List<String> loreB = new ArrayList<String>();
        ItemMeta mB = moneyInBank.getItemMeta();
        loreB.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainCurrentLore1")));
        loreB.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainCurrentLore2")) + ChatColor.GOLD + utils.truncateDecimal((banker.getMain().data.getMoney(player.getUniqueId())), 2));
        mB.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainCurrentName")));
        mB.setLore(loreB);
        moneyInBank.setItemMeta(mB);

        //// Exit
        ItemStack exit = new ItemStack(Material.BARRIER);
        List<String> loreC = new ArrayList<String>();
        ItemMeta mC = exit.getItemMeta();
        mC.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainClose")));
        mC.setLore(loreC);
        exit.setItemMeta(mC);


        //// Info
        ItemStack information = new ItemStack(Material.REDSTONE_TORCH);
        List<String> loreI = new ArrayList<String>();
        ItemMeta mI = information.getItemMeta();
        loreI.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainInfoLore1")));
        loreI.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainInfoLore2-1")) + ChatColor.BLUE + interestTime +
                utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainInfoLore2-2")));
        loreI.add(ChatColor.BLUE + "" + interestRate + "%" + utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainInfoLore3")));
        mI.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("bankerMainInfoName")));
        mI.setLore(loreI);
        information.setItemMeta(mI);


        //// GLASS PANE
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta mG = glass.getItemMeta();
        mG.setDisplayName(" ");
        glass.setItemMeta(mG);

        for(int i = 0; i < 36; i++) {
            bankerInv.setItem(i, glass);
        }

        //bankerMainInv.setItem(0, space);
        bankerInv.setItem(11, moneyDeposit);
        bankerInv.setItem(13, moneyWithdraw);
        bankerInv.setItem(15, moneyInBank);
        bankerInv.setItem(31, exit);
        bankerInv.setItem(32, information);
    }

    public void createDeposInv(Player player) {
        depositInv = Bukkit.createInventory(null, 36, utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInv")));

        BigDecimal playerMoney = utils.getMoney(player);

        //// Depozyt 100%
        ItemStack moneyDeposit = new ItemStack(Material.CHEST, 64);
        List<String> lore1 = new ArrayList<String>();
        ItemMeta m1 = moneyDeposit.getItemMeta();
        lore1.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvLore1-1")));
        lore1.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvLore1-2")) + ChatColor.GOLD + utils.truncateDecimal(playerMoney, 2).doubleValue());//playerMoney);
        m1.setLore(lore1);
        m1.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvName1")));
        moneyDeposit.setItemMeta(m1);


        //// Depozyt 50%
        ItemStack moneyDeposit2 = new ItemStack(Material.CHEST, 32);
        List<String> lore2 = new ArrayList<String>();
        ItemMeta m2 = moneyDeposit2.getItemMeta();
        lore2.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvLore2-1")));
        lore2.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvLore2-2")) + ChatColor.GOLD + utils.truncateDecimal(playerMoney, 2).doubleValue());//playerMoney / 2);
        m2.setLore(lore2);
        m2.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvName2")));
        moneyDeposit2.setItemMeta(m2);


        //// Depozyt 10%
        ItemStack moneyDeposit3 = new ItemStack(Material.CHEST, 1);
        List<String> lore3 = new ArrayList<String>();
        ItemMeta m3 = moneyDeposit3.getItemMeta();
        lore3.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvLore3-1")));
        lore3.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvLore3-2")) + ChatColor.GOLD + utils.truncateDecimal(playerMoney, 2).doubleValue()); //playerMoney / 10);
        m3.setLore(lore3);
        m3.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("deposInvName3")));
        moneyDeposit3.setItemMeta(m3);

        ItemStack goBack = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = goBack.getItemMeta();
        //List<String> loreA = new ArrayList<String>();
        //loreA.add(ChatColor.GRAY + "Do ");
        arrowMeta.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("goBack")));
        goBack.setItemMeta(arrowMeta);

        //// GLASS PANE
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta mG = glass.getItemMeta();
        mG.setDisplayName(" ");
        glass.setItemMeta(mG);

        for(int i = 0; i < 36; i++) {
            depositInv.setItem(i, glass);
        }

        depositInv.setItem(11, moneyDeposit);
        depositInv.setItem(13, moneyDeposit2);
        depositInv.setItem(15, moneyDeposit3);
        depositInv.setItem(31, goBack);
    }

    public void resetInv(Player player){
        player.closeInventory();
        createBankerInv(player);
        player.openInventory(bankerInv);
    }
}
