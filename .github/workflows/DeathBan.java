package me.deathban;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.Random;

public class DeathBan extends JavaPlugin implements Listener {

    private static final long BAN_TIME = 15 * 60 * 1000L;
    private final Random random = new Random();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        if (getCommand("unbankacung") != null) {
            getCommand("unbankacung").setExecutor((sender, command, label, args) -> {

                if (!sender.hasPermission("deathban.unban")) {
                    sender.sendMessage("§cKamu tidak memiliki izin.");
                    return true;
                }

                if (args.length != 1) {
                    sender.sendMessage("§eGunakan: /unbankacung <player>");
                    return true;
                }

                String playerName = args[0];

                Bukkit.getBanList(BanList.Type.NAME).pardon(playerName);

                sender.sendMessage(
                        "§aBan §e" + playerName + " §aberhasil dibuka."
                );

                Player player = Bukkit.getPlayerExact(playerName);

                if (player != null) {
                    player.sendMessage("§aBan kamu telah dibuka oleh admin.");
                }

                return true;
            });
        }

        getLogger().info("DeathBan aktif!");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        String name = player.getName();

        Date banUntil = new Date(System.currentTimeMillis() + BAN_TIME);

        String reason =
                "§cKamu mati!\n" +
                "§7Kamu diban selama §e15 menit§7.";

        Bukkit.getBanList(BanList.Type.NAME).addBan(
                name,
                reason,
                banUntil,
                "DeathBan"
        );

        player.kickPlayer(
                "§c§lKAMU MATI!\n\n" +
                "§7Kamu diban selama §e15 menit§7.\n" +
                "§7Coba masuk lagi setelah waktunya habis."
        );

        randomSpawn(player);
    }

    private void randomSpawn(Player player) {
        World world = player.getWorld();

        int radius = 1000;

        int x = random.nextInt(radius * 2 + 1) - radius;
        int z = random.nextInt(radius * 2 + 1) - radius;

        int highestY = world.getHighestBlockYAt(x, z);

        Location location = new Location(
                world,
                x + 0.5,
                highestY + 1,
                z + 0.5
        );

        player.setBedSpawnLocation(location, true);
    }
}
