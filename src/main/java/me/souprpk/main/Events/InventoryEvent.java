package me.souprpk.main.Events;

import me.souprpk.main.Banker;
import me.souprpk.main.GUI.BankerGUI;
import me.souprpk.main.Tools.Transaction;
import me.souprpk.main.Tools.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryEvent implements Listener {

    private Banker banker = Banker.getMain();
    private Utilities utils = new Utilities(banker);
    public static List<UUID> open = new ArrayList<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        BankerGUI gui = new BankerGUI();
        Transaction tran;
        BigDecimal bankMoney;
        if(banker.getConfig().getString("main.storage-system").equals("mysql")){
            bankMoney = banker.data.getMoney(event.getWhoClicked().getUniqueId());
        }
        else{
            bankMoney = banker.flatData.getMoney(event.getWhoClicked().getUniqueId());
        }
        // Banker Main Inventory
        if(event.getView().getTitle().equalsIgnoreCase(utils.Translate(banker.getConfig().getString("main.gui-display-name.banker-main")))) {

            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getItemMeta() == null) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            event.setCancelled(true);

            // Deposit
            if(event.getSlot() == 11) {
                player.closeInventory();
                gui.createDeposInv(player);
                player.openInventory(gui.depositInv);
            }

            // Withdraw
            if(event.getSlot() == 13) {
                player.closeInventory();
                //withdrawInv(player)
                gui.createWithdrawInv(player);
                player.openInventory(gui.withdrawInv);
            }

            if(banker.getConfig().getBoolean("money.loans-on"))
                if(event.getSlot() == 29){
                    player.closeInventory();

                    if(banker.loans.getDebt(player.getUniqueId()).compareTo(BigDecimal.ZERO) > 0){
                        gui.createDebtInv(player);
                        player.openInventory(gui.debtInv);
                    }else{
                        gui.createLoanInv(player);
                        player.openInventory(gui.loanInv);
                    }

                }

            if(event.getSlot() == 31) {
                player.closeInventory();
            }

        }

        if(event.getView().getTitle().equalsIgnoreCase(utils.Translate(banker.getConfig().getString("main.gui-display-name.banker-deposit")))) {

            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getItemMeta() == null) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            event.setCancelled(true);

            // Depozyt 100%
            if(event.getSlot() == 11) {
                tran = new Transaction(player.getUniqueId(), utils.getMoney(player), Transaction.TransactionType.Deposit);
                banker.eco.withdrawPlayer(player, utils.getMoney(player).doubleValue());
                gui.resetInv(player);

            }

            // Depozyt 50%
            if(event.getSlot() == 13) {
                tran = new Transaction(player.getUniqueId(), utils.getMoney(player).divide(BigDecimal.valueOf(2)), Transaction.TransactionType.Deposit);
                banker.eco.withdrawPlayer(player, utils.getMoney(player).divide(BigDecimal.valueOf(2)).doubleValue());
                gui.resetInv(player);
            }

            // Depozyt 10%
            if(event.getSlot() == 15) {
                tran = new Transaction(player.getUniqueId(), utils.getMoney(player).divide(BigDecimal.valueOf(10)), Transaction.TransactionType.Deposit);
                banker.eco.withdrawPlayer(player, utils.getMoney(player).divide(BigDecimal.valueOf(10)).doubleValue());
                gui.resetInv(player);
            }

            // Wracanie
            if(event.getSlot() == 31) {
                player.closeInventory();
                gui.createBankerInv(player);
                player.openInventory(gui.bankerInv);
            }
        }

        if(event.getView().getTitle().equalsIgnoreCase(utils.Translate(banker.getConfig().getString("main.gui-display-name.banker-withdraw")))) {

            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getItemMeta() == null) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            event.setCancelled(true);

            // Wyjecie 100%
            if(event.getSlot() == 11) {
                banker.eco.depositPlayer(player, bankMoney.doubleValue());
                tran = new Transaction(player.getUniqueId(), bankMoney, Transaction.TransactionType.Withdraw);
                gui.resetInv(player);

            }

            // Wyjecie 50%
            if(event.getSlot() == 13) {
                banker.eco.depositPlayer(player, bankMoney.divide(BigDecimal.valueOf(2)).doubleValue());
                tran = new Transaction(player.getUniqueId(), bankMoney.divide(BigDecimal.valueOf(2)), Transaction.TransactionType.Withdraw);
                gui.resetInv(player);
            }

            // Wyjecie 10%
            if(event.getSlot() == 15) {
                banker.eco.depositPlayer(player, bankMoney.divide(BigDecimal.valueOf(10)).doubleValue());
                tran = new Transaction(player.getUniqueId(), bankMoney.divide(BigDecimal.valueOf(10)), Transaction.TransactionType.Withdraw);
                gui.resetInv(player);
            }

            // Wracanie
            if(event.getSlot() == 31) {
                player.closeInventory();
                gui.createBankerInv(player);
                player.openInventory(gui.bankerInv);
            }

        }

        if(event.getView().getTitle().equalsIgnoreCase(utils.Translate(banker.getConfig().getString("main.gui-display-name.banker-loan")))){
            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getItemMeta() == null) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            event.setCancelled(true);

            BigDecimal firstLoan = BigDecimal.valueOf(banker.getConfig().getDouble("money.loans.first-loan"));
            BigDecimal secondLoan = BigDecimal.valueOf(banker.getConfig().getDouble("money.loans.second-loan"));
            BigDecimal thirdLoan = BigDecimal.valueOf(banker.getConfig().getDouble("money.loans.third-loan"));

            // Loan 100%
            if(event.getSlot() == 11) {
                banker.loans.takeLoan(player.getUniqueId(), thirdLoan);
                gui.resetInv(player);

            }

            // Loan 50%
            if(event.getSlot() == 13) {
                banker.loans.takeLoan(player.getUniqueId(), secondLoan);
                gui.resetInv(player);
            }

            // Loan 10%
            if(event.getSlot() == 15) {
                banker.loans.takeLoan(player.getUniqueId(), firstLoan);
                gui.resetInv(player);
            }

            // Wracanie
            if(event.getSlot() == 31) {
                player.closeInventory();
                gui.createBankerInv(player);
                player.openInventory(gui.bankerInv);
            }
        }

        if(event.getView().getTitle().equalsIgnoreCase(utils.Translate(banker.getConfig().getString("main.gui-display-name.banker-debt")))){
            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getItemMeta() == null) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event){
        if(event.getView().getTitle().equalsIgnoreCase(utils.Translate(banker.getConfig().getString("main.gui-display-name.banker-main")))) {
            open.add(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(event.getView().getTitle().equalsIgnoreCase(utils.Translate(banker.getConfig().getString("main.gui-display-name.banker-main")))) {
            if(open.contains(event.getPlayer().getUniqueId()))
                open.remove(event.getPlayer().getUniqueId());
        }
    }

}
