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
		
		if(Teams.getTeam(e.getPlayer()).equals("red")) {
			Teams.setTeam(e.getPlayer(), "spectator");
		} else if(Teams.getTeam(e.getPlayer()).equals("blue")) {
			Teams.setTeam(e.getPlayer(), "spectator");
		} else {
			Teams.setTeam(e.getPlayer(), "spectator");
		}
		
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		Player damager = (Player) e.getDamager();
		Player receiver = (Player) e.getEntity();
		
		if(Teams.getTeam(receiver).equals(Teams.getTeam(damager))) {
			e.setCancelled(true);
		} else {
			
		}
	}
	
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().toLowerCase().equals("/warfare start")) {
			
			if(e.getPlayer().hasPermission("warfare.admin")) {
				if(Game.canStart()) {
					Game.startGame();
				} else {
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡No hay suficientes jugadores!"));
				}	
			} else {
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡No tienes permiso para ejecutar ese comando!"));
			}
		} else if(e.getMessage().toLowerCase().equals("/warfare stop")) {
			if(e.getPlayer().hasPermission("warfare.admin")) {
				Game.stopGame();	
			} else {
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡No tienes permiso para ejecutar ese comando!"));
			}
		} else if(e.getMessage().toLowerCase().startsWith("/warfare")) {
			if(e.getPlayer().hasPermission("warfare.admin")) {
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsa: /warfare start o /warfare stop"));
			} else {
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡No tienes permiso para ejecutar ese comando!"));
			}
			
		}
	}
	
	@EventHandler
	public void OnDeath(PlayerDeathEvent e) {
		if(Teams.getTeam(e.getEntity().getPlayer()).equals("red")) {
			Teams.addKill("blue");
		} else if(Teams.getTeam(e.getEntity().getPlayer()).equals("blue")) {
			Teams.addKill("red");
		}
	}
	
	@EventHandler
	public void OnRespawn(final PlayerRespawnEvent e) {
		final Location lRed = new Location(Bukkit.getWorld("Warfare"), 0.5, 51.1, 36.5, 180, 0);
		final Location lBlue = new Location(Bukkit.getWorld("Warfare"), 0.5, 51.1, -43.5, 0, 0);
		if(Teams.getTeam(e.getPlayer()).equals("red")) {
			Game.giveKit(e.getPlayer(), "red");
			new BukkitRunnable() {

				@Override
				public void run() {
					e.getPlayer().teleport(lRed);
				}
				
			}.runTaskLater(plugin, 5L);
			
		} else if(Teams.getTeam(e.getPlayer()).equals("blue")) {
			Game.giveKit(e.getPlayer(), "blue");
			new BukkitRunnable() {

				@Override
				public void run() {
					e.getPlayer().teleport(lBlue);
				}
				
			}.runTaskLater(plugin, 5L);
		} else {
			
		}
	}
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		if(Teams.getTeam(e.getPlayer()).equals("red")) {
			e.setMessage(ChatColor.translateAlternateColorCodes('&', "&4[Rojo] &c" + e.getMessage()));
		} else if(Teams.getTeam(e.getPlayer()).equals("blue")) {
			e.setMessage(ChatColor.translateAlternateColorCodes('&', "&3[Azul] &b" + e.getMessage()));
		} else if(Teams.getTeam(e.getPlayer()).equals("spectator")) {
			e.setMessage(ChatColor.translateAlternateColorCodes('&', "&8[Espectador] &7" + e.getMessage()));
		}
	}

	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		final ItemStack redWool = new ItemStack(Material.WOOL, 1);
		final ItemStack blueWool = new ItemStack(Material.WOOL, 1);
		final Block b = e.getBlock();
		if(b.getType().equals(Material.WOOD)) {
			
		} else if(b.getX() == -19 && b.getY() == 51 && b.getZ() == 36) {
			if(Teams.getTeam(e.getPlayer()).equals("red")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Rompe la lana del equipo contrario en vez de la tuya por favor!"));
			} else {
				e.setCancelled(true);
				b.setType(Material.AIR);
				Teams.addWool("red", e.getPlayer());
				new BukkitRunnable() {

					@Override
					public void run() {
						b.setType(redWool.getType());
					}
					
				}.runTaskLater(plugin, 100L);
			}
		} else if(b.getX() == 19 && b.getY() == 51 && b.getZ() == 36) {
			if(Teams.getTeam(e.getPlayer()).equals("red")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Rompe la lana del equipo contrario en vez de la tuya por favor!"));
			} else {
				e.setCancelled(true);
				b.setType(Material.AIR);
				Teams.addWool("red", e.getPlayer());
				new BukkitRunnable() {

					@Override
					public void run() {
						b.setType(redWool.getType());
					}
					
				}.runTaskLater(plugin, 100L);
			}
		} else if(b.getX() == 19 && b.getY() == 51 && b.getZ() == -44) {
			if(Teams.getTeam(e.getPlayer()).equals("blue")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Rompe la lana del equipo contrario en vez de la tuya por favor!"));
			} else {
				e.setCancelled(true);
				b.setType(Material.AIR);
				Teams.addWool("blue", e.getPlayer());
				new BukkitRunnable() {

					@Override
					public void run() {
						b.setType(blueWool.getType());
					}
					
				}.runTaskLater(plugin, 100L);
			}
		} else if(b.getX() == -19 && b.getY() == 51 && b.getZ() == -44) {
			if(Teams.getTeam(e.getPlayer()).equals("blue")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Rompe la lana del equipo contrario en vez de la tuya por favor!"));
			} else {
				e.setCancelled(true);
				b.setType(Material.AIR);
				Teams.addWool("blue", e.getPlayer());
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
	
	@EventHandler
	public void BlockPlace(BlockPlaceEvent e) {
		final Block b = e.getBlock();
		new BukkitRunnable() {

			@Override
			public void run() {
				if(Game.hasToRestore()) {
					b.setType(Material.AIR);
				}
			}
			
		}.runTaskTimer(plugin, 1L, 1L);
	}
	
}