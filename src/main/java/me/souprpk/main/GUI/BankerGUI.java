package me.souprpk.main.GUI;

import me.souprpk.main.Banker;
import me.souprpk.main.Events.InventoryEvent;
import me.souprpk.main.Tools.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BankerGUI {

    public Inventory bankerInv;
    public Inventory depositInv;
    public Inventory withdrawInv;
    public Inventory loanInv;
    public Inventory debtInv;

    private final Banker banker = Banker.getMain();
    private final Utilities utils = new Utilities(banker);

    public void createBankerInv(Player player){
        bankerInv = Bukkit.createInventory(player, 36, utils.Translate(utils.getConfigString("main.gui-display-name.banker-main")));
        int interestTime = banker.getConfig().getInt("money.every-hours");
        double interestRate = banker.getConfig().getDouble("money.interest-rate");
        BigDecimal playerBankMoney;
        if(banker.getConfig().getString("main.storage-system").equals("mysql")){
            playerBankMoney = banker.data.getMoney(player.getUniqueId());
        }
        else{
            playerBankMoney = banker.flatData.getMoney(player.getUniqueId());
        }

        //// GLASS PANE
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta mG = glass.getItemMeta();
        mG.setDisplayName(" ");
        glass.setItemMeta(mG);

        for(int i = 0; i < 36; i++) {
            bankerInv.setItem(i, glass);
        }

        //// Deposit
        ItemStack moneyDeposit = new ItemStack(Material.CHEST);
        List<String> loreD = new ArrayList<String>();
        ItemMeta mD = moneyDeposit.getItemMeta();
        loreD.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainDeposLore1")));
        loreD.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainDeposLore2")));
        loreD.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainDeposLore3")));
        mD.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainDeposName")));
        mD.setLore(loreD);
        moneyDeposit.setItemMeta(mD);

        //// Wyjecie
        ItemStack moneyWithdraw = new ItemStack(Material.DISPENSER);
        List<String> loreW = new ArrayList<String>();
        ItemMeta mW = moneyWithdraw.getItemMeta();
        loreW.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainWithdrawLore1")));
        loreW.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainWithdrawLore2")));
        loreW.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainWithdrawLore3")));
        mW.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainWithdrawName")));
        mW.setLore(loreW);
        moneyWithdraw.setItemMeta(mW);



        //// Amount of money in bank
        ItemStack moneyInBank = new ItemStack(Material.GOLD_INGOT);
        List<String> loreB = new ArrayList<String>();
        ItemMeta mB = moneyInBank.getItemMeta();
        loreB.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainCurrentLore1")));
        loreB.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainCurrentLore2")) + ChatColor.GOLD + utils.truncateDecimal(playerBankMoney, 2));
        mB.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainCurrentName")));
        mB.setLore(loreB);
        moneyInBank.setItemMeta(mB);



        //// Loan / debt menu
        ItemStack loanDebt = new ItemStack(Material.PAPER);
        List<String> loreLD = new ArrayList<String>();
        ItemMeta mLD = moneyInBank.getItemMeta();
        loreLD.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainLoansDebtLore1")));
        loreLD.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainLoansDebtLore2")));
        mLD.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainLoansDebtName")));
        mLD.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        mLD.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mLD.setLore(loreLD);
        loanDebt.setItemMeta(mLD);


        //// Exit
        ItemStack exit = new ItemStack(Material.BARRIER);
        List<String> loreC = new ArrayList<String>();
        ItemMeta mC = exit.getItemMeta();
        mC.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainClose")));
        mC.setLore(loreC);
        exit.setItemMeta(mC);

        new BukkitRunnable(){
            public void run(){
                if(InventoryEvent.open.contains(player.getUniqueId()) && player.isOnline()){
                    //// Info
                    String date = banker.flat.getConfig().getString("nextInterest");
                    LocalDateTime untilInterest = LocalDateTime.parse(date);

                    Duration beet = Duration.between(LocalDateTime.now(), untilInterest);

                    int hLeft = (int) (beet.toHours());
                    int mLeft = (int) (beet.toMinutes() - (beet.toHours() * 60));
                    int sLeft = (int) ((beet.toMillis() / 1000) - (beet.toMinutes() * 60));

                    ItemStack information = new ItemStack(Material.REDSTONE_TORCH);
                    List<String> loreI = new ArrayList<String>();
                    ItemMeta mI = information.getItemMeta();
                    loreI.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainInfoLore1")));
                    loreI.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainInfoLore2-1")) + ChatColor.BLUE + interestTime +
                            utils.Translate(banker.messageConfig.getConfig().getString("bankerMainInfoLore2-2")));
                    loreI.add(ChatColor.BLUE + "" + interestRate + "%" + utils.Translate(banker.messageConfig.getConfig().getString("bankerMainInfoLore3")));
                    loreI.add(" ");
                    loreI.add(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainInfoLore4")));
                    loreI.add(ChatColor.BLUE + "" + hLeft + "h " + mLeft + "m " + sLeft + "s" + ChatColor.GRAY + ".");
                    mI.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("bankerMainInfoName")));
                    mI.setLore(loreI);
                    information.setItemMeta(mI);
                    bankerInv.setItem(32, information);
                }else{
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(banker, 0,20L);


        //bankerMainInv.setItem(0, space);
        bankerInv.setItem(11, moneyDeposit);
        bankerInv.setItem(13, moneyWithdraw);
        bankerInv.setItem(15, moneyInBank);
        bankerInv.setItem(31, exit);
        if(banker.getConfig().getBoolean("money.loans.loans-on"))
            bankerInv.setItem(29, loanDebt);
    }

    public void createDeposInv(Player player) {
        depositInv = Bukkit.createInventory(null, 36, utils.Translate(utils.getConfigString("main.gui-display-name.banker-deposit")));

        BigDecimal playerMoney = utils.getMoney(player);

        //// Deposit 100%
        ItemStack moneyDeposit = new ItemStack(Material.CHEST, 64);
        List<String> lore1 = new ArrayList<String>();
        ItemMeta m1 = moneyDeposit.getItemMeta();
        lore1.add(utils.Translate(banker.messageConfig.getConfig().getString("deposInvLore1-1")));
        lore1.add(utils.Translate(banker.messageConfig.getConfig().getString("deposInvLore1-2")) + ChatColor.GOLD + utils.truncateDecimal(playerMoney, 2).doubleValue());//playerMoney);
        m1.setLore(lore1);
        m1.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("deposInvName1")));
        moneyDeposit.setItemMeta(m1);


        //// Deposit 50%
        ItemStack moneyDeposit2 = new ItemStack(Material.CHEST, 32);
        List<String> lore2 = new ArrayList<String>();
        ItemMeta m2 = moneyDeposit2.getItemMeta();
        lore2.add(utils.Translate(banker.messageConfig.getConfig().getString("deposInvLore2-1")));
        lore2.add(utils.Translate(banker.messageConfig.getConfig().getString("deposInvLore2-2")) + ChatColor.GOLD + utils.truncateDecimal(playerMoney.divide(BigDecimal.valueOf(2)), 2).doubleValue());//playerMoney / 2);
        m2.setLore(lore2);
        m2.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("deposInvName2")));
        moneyDeposit2.setItemMeta(m2);


        //// Deposit 10%
        ItemStack moneyDeposit3 = new ItemStack(Material.CHEST, 1);
        List<String> lore3 = new ArrayList<String>();
        ItemMeta m3 = moneyDeposit3.getItemMeta();
        lore3.add(utils.Translate(banker.messageConfig.getConfig().getString("deposInvLore3-1")));
        lore3.add(utils.Translate(banker.messageConfig.getConfig().getString("deposInvLore3-2")) + ChatColor.GOLD + utils.truncateDecimal(playerMoney.divide(BigDecimal.valueOf(10)), 2).doubleValue()); //playerMoney / 10);
        m3.setLore(lore3);
        m3.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("deposInvName3")));
        moneyDeposit3.setItemMeta(m3);

        ItemStack goBack = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = goBack.getItemMeta();
        //List<String> loreA = new ArrayList<String>();
        //loreA.add(ChatColor.GRAY + "Do ");
        arrowMeta.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("goBack")));
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

    public void createWithdrawInv(Player player) {
        withdrawInv = Bukkit.createInventory(null, 36, utils.Translate(utils.getConfigString("main.gui-display-name.banker-withdraw")));

        BigDecimal playerBankMoney;
        if(banker.getConfig().getString("main.storage-system").equals("mysql")){
            playerBankMoney = banker.data.getMoney(player.getUniqueId());
        }
        else{
            playerBankMoney = banker.flatData.getMoney(player.getUniqueId());
        }


        //// Withdraw 100%
        ItemStack moneyDeposit = new ItemStack(Material.DISPENSER, 64);
        List<String> lore1 = new ArrayList<String>();
        ItemMeta m1 = moneyDeposit.getItemMeta();
        lore1.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvLore1-1")));
        lore1.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvLore1-2")) + ChatColor.GOLD + utils.truncateDecimal(playerBankMoney , 2).doubleValue());//playerBankMoney);
        m1.setLore(lore1);
        m1.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvName1")));
        moneyDeposit.setItemMeta(m1);


        //// Withdraw 50%
        ItemStack moneyDeposit2 = new ItemStack(Material.DISPENSER, 32);
        List<String> lore2 = new ArrayList<String>();
        ItemMeta m2 = moneyDeposit2.getItemMeta();
        lore2.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvLore2-1")));
        lore2.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvLore2-2")) + ChatColor.GOLD + utils.truncateDecimal(playerBankMoney.divide(BigDecimal.valueOf(2)), 2).doubleValue());//playerBankMoney / 2);
        m2.setLore(lore2);
        m2.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvName2")));
        moneyDeposit2.setItemMeta(m2);


        //// Withdraw 10%
        ItemStack moneyDeposit3 = new ItemStack(Material.DISPENSER, 1);
        List<String> lore3 = new ArrayList<String>();
        ItemMeta m3 = moneyDeposit3.getItemMeta();
        lore3.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvLore3-1")));
        lore3.add(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvLore3-2")) + ChatColor.GOLD + utils.truncateDecimal(playerBankMoney.divide(BigDecimal.valueOf(10)), 2).doubleValue());//playerBankMoney / 10);
        m3.setLore(lore3);
        m3.setDisplayName(utils.Translate(banker.getMain().messageConfig.getConfig().getString("withdrawInvName3")));
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
            withdrawInv.setItem(i, glass);
        }

        withdrawInv.setItem(11, moneyDeposit);
        withdrawInv.setItem(13, moneyDeposit2);
        withdrawInv.setItem(15, moneyDeposit3);
        withdrawInv.setItem(31, goBack);
    }

    public void createLoanInv(Player player){
        loanInv = Bukkit.createInventory(null, 36, utils.Translate(utils.getConfigString("main.gui-display-name.banker-loan")));

        BigDecimal firstLoan = BigDecimal.valueOf(banker.getConfig().getDouble("money.loans.first-loan"));
        BigDecimal secondLoan = BigDecimal.valueOf(banker.getConfig().getDouble("money.loans.second-loan"));
        BigDecimal thirdLoan = BigDecimal.valueOf(banker.getConfig().getDouble("money.loans.third-loan"));

        //// Take loan
        ItemStack moneyToLoan = new ItemStack(Material.LIME_STAINED_GLASS);
        List<String> lore1 = new ArrayList<String>();
        ItemMeta m1 = moneyToLoan.getItemMeta();
        lore1.add(utils.Translate(banker.messageConfig.getConfig().getString("loansTakeInfo")) + ChatColor.GOLD + utils.truncateDecimal(firstLoan, 2) + ChatColor.DARK_GRAY + ".");//playerMoney);
        m1.setLore(lore1);
        m1.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("loansTakeName")));
        moneyToLoan.setItemMeta(m1);

        //// Take loan 2
        ItemStack moneyToLoan2 = new ItemStack(Material.LIME_STAINED_GLASS);
        List<String> lore2 = new ArrayList<String>();
        ItemMeta m2 = moneyToLoan2.getItemMeta();
        lore2.add(utils.Translate(banker.messageConfig.getConfig().getString("loansTakeInfo")) + ChatColor.GOLD + utils.truncateDecimal(secondLoan, 2) + ChatColor.DARK_GRAY + ".");//playerMoney);
        m2.setLore(lore2);
        m2.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("loansTakeName")));
        moneyToLoan2.setItemMeta(m2);

        //// Take loan 3
        ItemStack moneyToLoan3 = new ItemStack(Material.LIME_STAINED_GLASS);
        List<String> lore3 = new ArrayList<String>();
        ItemMeta m3 = moneyToLoan3.getItemMeta();
        lore3.add(utils.Translate(banker.messageConfig.getConfig().getString("loansTakeInfo")) + ChatColor.GOLD + utils.truncateDecimal(thirdLoan, 2) + ChatColor.DARK_GRAY + ".");//playerMoney);
        m3.setLore(lore3);
        m3.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("loansTakeName")));
        moneyToLoan3.setItemMeta(m3);

        // Go back
        ItemStack goBack = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = goBack.getItemMeta();
        //List<String> loreA = new ArrayList<String>();
        //loreA.add(ChatColor.GRAY + "Do ");
        arrowMeta.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("goBack")));
        goBack.setItemMeta(arrowMeta);

        //// GLASS PANE
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta mG = glass.getItemMeta();
        mG.setDisplayName(" ");
        glass.setItemMeta(mG);

        for(int i = 0; i < 36; i++) {
            loanInv.setItem(i, glass);
        }

        loanInv.setItem(11, moneyToLoan3);
        loanInv.setItem(13, moneyToLoan2);
        loanInv.setItem(15, moneyToLoan);
        loanInv.setItem(31, goBack);
    }

    public void createDebtInv(Player player){
        debtInv = Bukkit.createInventory(null, 36, utils.Translate(utils.getConfigString("main.gui-display-name.banker-debt")));

        BigDecimal debt = banker.loans.getDebt(player.getUniqueId());
        BigDecimal moneyOnPlayer = BigDecimal.valueOf(banker.eco.getBalance(player));
        if(debt.compareTo(moneyOnPlayer) > 0){
            debt = moneyOnPlayer;
        }


        //// Dept pay 100%
        ItemStack debtPay = new ItemStack(Material.CHEST, 64);
        List<String> lore1 = new ArrayList<String>();
        ItemMeta m1 = debtPay.getItemMeta();
        lore1.add(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayLore1-1")));
        lore1.add(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayLore1-2")) + ChatColor.GOLD + utils.truncateDecimal(debt, 2).doubleValue());//playerMoney);
        m1.setLore(lore1);
        m1.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayName1")));
        debtPay.setItemMeta(m1);


        //// Dept pay 50%
        ItemStack debtPay2 = new ItemStack(Material.CHEST, 32);
        List<String> lore2 = new ArrayList<String>();
        ItemMeta m2 = debtPay2.getItemMeta();
        lore2.add(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayLore2-1")));
        lore2.add(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayLore2-2")) + ChatColor.GOLD + utils.truncateDecimal(debt.divide(BigDecimal.valueOf(2)), 2).doubleValue());//playerMoney / 2);
        m2.setLore(lore2);
        m2.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayName2")));
        debtPay2.setItemMeta(m2);


        //// Dept pay 10%
        ItemStack debtPay3 = new ItemStack(Material.CHEST, 1);
        List<String> lore3 = new ArrayList<String>();
        ItemMeta m3 = debtPay3.getItemMeta();
        lore3.add(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayLore3-1")));
        lore3.add(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayLore3-2")) + ChatColor.GOLD + utils.truncateDecimal(debt.divide(BigDecimal.valueOf(10)), 2).doubleValue()); //playerMoney / 10);
        m3.setLore(lore3);
        m3.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("debtInvPayName3")));
        debtPay3.setItemMeta(m3);


        //// Amount of money in bank
        ItemStack moneyInBank = new ItemStack(Material.GOLD_INGOT);
        List<String> loreB = new ArrayList<String>();
        ItemMeta mB = moneyInBank.getItemMeta();
        loreB.add(utils.Translate(banker.messageConfig.getConfig().getString("debtInvCurrentMoneyLore1-1")) + ChatColor.GOLD + utils.truncateDecimal(moneyOnPlayer, 2));
        loreB.add(utils.Translate(banker.messageConfig.getConfig().getString("debtInvCurrentMoneyLore1-2"))  + ChatColor.GOLD + utils.truncateDecimal(banker.loans.getDebt(player.getUniqueId()), 2));
        mB.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("debtInvCurrentMoneyName")));
        mB.setLore(loreB);
        moneyInBank.setItemMeta(mB);

        // Go back
        ItemStack goBack = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = goBack.getItemMeta();
        //List<String> loreA = new ArrayList<String>();
        //loreA.add(ChatColor.GRAY + "Do ");
        arrowMeta.setDisplayName(utils.Translate(banker.messageConfig.getConfig().getString("goBack")));
        goBack.setItemMeta(arrowMeta);

        //// GLASS PANE
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta mG = glass.getItemMeta();
        mG.setDisplayName(" ");
        glass.setItemMeta(mG);

        for(int i = 0; i < 36; i++) {
            debtInv.setItem(i, glass);
        }

        debtInv.setItem(11, debtPay);
        debtInv.setItem(13, debtPay2);
        debtInv.setItem(15, debtPay3);
        debtInv.setItem(31, goBack);
        debtInv.setItem(32, moneyInBank);
    }

    public void resetInv(Player player){
        player.closeInventory();
        createBankerInv(player);
        player.openInventory(bankerInv);
    }
}
