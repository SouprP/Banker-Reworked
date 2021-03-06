package me.souprpk.main.Systems.StorageSystem.MySQL;

import com.google.common.collect.Maps;
import me.souprpk.main.Banker;
import me.souprpk.main.Systems.StorageSystem.StorageSystem;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class SQLGetter implements StorageSystem {

    private Banker banker;

    public SQLGetter(Banker banker){
        this.banker = banker;
    }
    @Override
    public void deposit(UUID uuid, BigDecimal amount) {
        try {
            PreparedStatement ps = banker.mySQL.getConnection().prepareStatement("UPDATE banker SET MONEY=? WHERE UUID=?");

            ps.setDouble(1, getMoney(uuid).add(amount).doubleValue());//Main.truncateDecimal(money, 2).doubleValue());
            ps.setString(2,  uuid.toString());
            ps.executeUpdate();

            //Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + Bukkit.getOfflinePlayer(uuid).getName() + " deposited: " + Main.truncateDecimal(money, 2) + "$");
            //Logs log = new Logs();
            //log.log(Bukkit.getOfflinePlayer(uuid).getName() + " deposited: " + Main.truncateDecimal(money, 2) + "$");

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void withdraw(UUID uuid, BigDecimal amount) {
        try {
            PreparedStatement ps = banker.mySQL.getConnection().prepareStatement("UPDATE banker SET MONEY=? WHERE UUID=?");
            BigDecimal moneyToWithdraw = amount;
            ps.setDouble(1, getMoney(uuid).subtract(moneyToWithdraw).doubleValue());
            ps.setString(2,  uuid.toString());
            ps.executeUpdate();

            //Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + Bukkit.getOfflinePlayer(uuid).getName() + " withdrew: " + Main.truncateDecimal(moneyToWithdraw, 2) + "$");
            //Logs log = new Logs();
            //log.log(Bukkit.getOfflinePlayer(uuid).getName() + " withdrew: " + Main.truncateDecimal(moneyToWithdraw, 2) + "$");


        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal getMoney(UUID uuid) {
        try {
            PreparedStatement ps = banker.mySQL.getConnection().prepareStatement("SELECT MONEY FROM banker WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            BigDecimal money;

            if(results.next()) {
                money = BigDecimal.valueOf(results.getDouble("MONEY"));

                //return (Math.floor(money*100) / 100);
                //return Main.truncateDecimal(money, 2);

                return money;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.valueOf(0);
    }

    public void createTable() {
        PreparedStatement ps;

        try {
            ps = banker.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS banker " +
                    "(NAME VARCHAR(100), UUID VARCHAR(100), MONEY DOUBLE, PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();

            if(!exists(uuid)) {
                PreparedStatement ps2 = banker.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO banker" +
                        " (NAME,UUID) VALUES(?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.executeUpdate();
                //Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must have Vault and an Economy plugin installed, disabling plugin");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = banker.mySQL.getConnection().prepareStatement("SELECT * FROM banker WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if(results.next()) {
                // player is found
                return true;
            }
            // otherwise return false
            return false;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void interestRateIncrease(BigDecimal interestRate) {
        try {
            PreparedStatement ps = banker.mySQL.getConnection().prepareStatement("SELECT UUID, MONEY FROM banker");
            ResultSet results = ps.executeQuery();
            while(results.next()) {
                String uuid = results.getString(1);
                BigDecimal money = BigDecimal.valueOf(results.getDouble(2));
                // interestRate / 100 ===== 5% / 100 ===== money * 0,05
                money = money.add(money.multiply(interestRate).divide(BigDecimal.valueOf(100)));
                PreparedStatement ps2 = banker.mySQL.getConnection().prepareStatement("UPDATE banker SET MONEY=? WHERE UUID=?");
                ps2.setDouble(1, money.doubleValue());
                ps2.setString(2, uuid);
                ps2.executeUpdate();
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeCustom(String uuid, BigDecimal amount) {
        try {
            PreparedStatement ps = banker.mySQL.getConnection().prepareStatement("UPDATE banker SET MONEY=? WHERE UUID=?");
            ps.setDouble(1, amount.doubleValue());
            ps.setString(2,  uuid);
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeToFlatFile(){
        try {
            PreparedStatement ps = banker.mySQL.getConnection().prepareStatement("SELECT UUID, MONEY FROM banker");
            ResultSet results = ps.executeQuery();

            while(results.next()){
                banker.flatData.writeCustom(results.getString("UUID"), BigDecimal.valueOf(results.getDouble("MONEY")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, BigDecimal> getTopInfo(){
        Map<String, BigDecimal> map = Maps.newHashMap();
        try {
            PreparedStatement ps = banker.mySQL.getConnection().prepareStatement("SELECT UUID, MONEY FROM banker");
            ResultSet results = ps.executeQuery();

            while(results.next()){
                map.put(results.getString("UUID"), BigDecimal.valueOf(results.getDouble("MONEY")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}
