package alabingu.ctw.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import alabingu.ctw.CTW;
import alabingu.ctw.Game;
import alabingu.ctw.Teams;

@SuppressWarnings("deprecation")
public class Events implements Listener {

    private static JavaPlugin plugin = JavaPlugin.getProvidingPlugin(CTW.class);

    public static void restore() {
        Game.gottaRestore = true;
        new BukkitRunnable() {

            @Override
            public void run() {
                Game.gottaRestore = false;
            }

        }.runTaskLater(plugin, 5L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        Teams.setTeam(p, "spectator");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "vanish " + e.getPlayer() + " disable");

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
		
/*
		if(Teams.getTeam(e.getPlayer()).equals("red")) {
			Teams.setTeam(e.getPlayer(), "spectator");
		} else if(Teams.getTeam(e.getPlayer()).equals("blue")) {
			Teams.setTeam(e.getPlayer(), "spectator");
		} else {
			Teams.setTeam(e.getPlayer(), "spectator");
		}
*/
        //LMU: Same action for all 3 if branches -> simplify
        Teams.setTeam(e.getPlayer(), "spectator");


    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

/*
        Player damager = (Player) e.getDamager();
        Player receiver = (Player) e.getEntity();
		if(Teams.getTeam(receiver).equals(Teams.getTeam(damager))) {
			e.setCancelled(true);
		} else {
			
		}
*/

        //LMU: else what?
        e.setCancelled(true);
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        //LMU readability
        String message = e.getMessage().toLowerCase();
        Player player = e.getPlayer();

        if (message.equals("/warfare start")) {

            if (player.hasPermission("warfare.admin")) {
                if (Game.canStart()) {
                    Game.startGame();
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡No hay suficientes jugadores!"));
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡No tienes permiso para ejecutar ese comando!"));
            }
        } else if (message.equals("/warfare stop")) {
            if (player.hasPermission("warfare.admin")) {
                Game.stopGame();
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡No tienes permiso para ejecutar ese comando!"));
            }
        } else if (message.startsWith("/warfare")) {
            if (player.hasPermission("warfare.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsa: /warfare start o /warfare stop"));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡No tienes permiso para ejecutar ese comando!"));
            }

        }
    }

    @EventHandler
    public void OnDeath(PlayerDeathEvent e) {
        if (teamIsRed(Teams.getTeam(e.getEntity().getPlayer()))) {
            Teams.addKill("blue");
        } else if (teamIsBlue(Teams.getTeam(e.getEntity().getPlayer()))) {
            Teams.addKill("red");
        }
    }

    @EventHandler
    public void OnRespawn(final PlayerRespawnEvent e) {
        final Location lRed = new Location(Bukkit.getWorld("Warfare"), 0.5, 51.1, 36.5, 180, 0);
        final Location lBlue = new Location(Bukkit.getWorld("Warfare"), 0.5, 51.1, -43.5, 0, 0);
/*
LMU: too complicated, too much copy/paste
        if (Teams.getTeam(e.getPlayer()).equals("red")) {
            Game.giveKit(e.getPlayer(), "red");
            new BukkitRunnable() {

                @Override
                public void run() {
                    e.getPlayer().teleport(lRed);
                }

            }.runTaskLater(plugin, 5L);

        } else if (Teams.getTeam(e.getPlayer()).equals("blue")) {
            Game.giveKit(e.getPlayer(), "blue");
            new BukkitRunnable() {

                @Override
                public void run() {
                    e.getPlayer().teleport(lBlue);
                }

            }.runTaskLater(plugin, 5L);
        } else {
            //LMU: else what???!

        }
*/


        //Much more readable
        Player player = e.getPlayer();
        String team = Teams.getTeam(e.getPlayer());
        final Location newPlace;
        if (teamIsRed(team)) {
            newPlace = lRed
        } else if (teamIsBlue(team)) {
            newPlace = lBlue;
        } else {
            return;
        }

        Game.giveKit(e.getPlayer(), team);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(newPlace); //LMU: this is readable!!
            }
        }.runTaskLater(plugin, 5L);
    }

    private boolean teamIsBlue(String team) {
        return team.equals("blue");
    }

    private boolean teamIsRed(String team) {
        return team.equals("red");
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        String team = Teams.getTeam(e.getPlayer());
        String message = e.getMessage();

        if (teamIsRed(team)) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', "&4[Rojo] &c" + message));
        } else if (teamIsBlue(team)) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', "&3[Azul] &b" + message));
        } else if (team.equals("spectator")) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', "&8[Espectador] &7" + message));
        }
    }

    private boolean blockIsRed(Block b) {
        if ((b.getX() == -19 && b.getY() == 51 && b.getZ() == 36)
            || (b.getX() == 19 && b.getY() == 51 && b.getZ() == 36)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean playerIsRed(Player p) {
        return teamIsRed(Teams.getTeam(p));
    }

    private boolean blockIsBlue(Block b) {
        if ((b.getX() == 19 && b.getY() == 51 && b.getZ() == -44)
            || (b.getX() == -19 && b.getY() == 51 && b.getZ() == -44)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean playerIsBlue(Player p) {
        return teamIsBlue(Teams.getTeam(p));
    }

    @EventHandler
    public void BlockBreak(BlockBreakEvent e) {
        final ItemStack redWool = new ItemStack(Material.WOOL, 1);
        final ItemStack blueWool = new ItemStack(Material.WOOL, 1);
        final Block b = e.getBlock();

        //Too much copy/paste
        if (b.getType().equals(Material.WOOD)) {
            //What?!??!?!
        } else {
            Player player = e.getPlayer();
            if (blockIsRed(b)) {
                if (playerIsRed(player)) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Rompe la lana del equipo contrario en vez de la tuya por favor!"));
                } else {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    Teams.addWool("red", player);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            b.setType(redWool.getType());
                        }

                    }.runTaskLater(plugin, 100L);
                }
            } else if (blockIsBlue(b)) {
                if (playerIsBlue(player)) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Rompe la lana del equipo contrario en vez de la tuya por favor!"));
                } else {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    Teams.addWool("blue", player);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            b.setType(blueWool.getType());
                        }

                    }.runTaskLater(plugin, 100L);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void BlockPlace(BlockPlaceEvent e) {
        final Block b = e.getBlock();
        new BukkitRunnable() {

            @Override
            public void run() {
                if (Game.hasToRestore()) {
                    b.setType(Material.AIR);
                }
            }

        }.runTaskTimer(plugin, 1L, 1L);
    }

}