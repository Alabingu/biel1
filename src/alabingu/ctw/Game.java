package alabingu.ctw;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import alabingu.ctw.events.Events;

@SuppressWarnings("deprecation")
public class Game {
	
	public static Boolean inProgress = false;
	
	private static JavaPlugin plugin = JavaPlugin.getProvidingPlugin(CTW.class);
	
	public static Boolean canStart() {
		if(Bukkit.getOnlinePlayers().size() > 2 || Bukkit.getOnlinePlayers().size() == 2) {
			return true;
		} else {
			return false;
		}

	}
	
	public static void resetMap() {
		Events.restore();
	}
	
	public static String getWinners() {
		if(Teams.getBlueWools() > Teams.getRedWools()) {
			return "blue";
		} else if(Teams.getBlueWools() == Teams.getRedWools()) {
			return "empate";
		} else if(Teams.getBlueWools() < Teams.getRedWools()) {
			return "red";
		} else {
			return null;
		}
	}
	public static void giveKit(Player p, String team) {
		if(team.equals("red")) {
			ItemStack redHelmet = new ItemStack(Material.LEATHER_HELMET, 1, DyeColor.RED.getData());
			ItemStack redChestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1, DyeColor.RED.getData());
			ItemStack redLeggings = new ItemStack(Material.LEATHER_LEGGINGS, 1, DyeColor.RED.getData());
			ItemStack redBoots = new ItemStack(Material.LEATHER_BOOTS, 1, DyeColor.RED.getData());
			ItemStack sword = new ItemStack(Material.STONE_SWORD);
			ItemStack bow = new ItemStack(Material.BOW);
			ItemStack arrow = new ItemStack(Material.ARROW, 64);
			ItemStack rod = new ItemStack(Material.FISHING_ROD);
			ItemStack wood = new ItemStack(Material.WOOD, 64);
			ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
			ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
			p.getInventory().setHelmet(redHelmet);
			p.getInventory().setChestplate(redChestplate);
			p.getInventory().setLeggings(redLeggings);
			p.getInventory().setBoots(redBoots);
			p.getInventory().setItem(0, sword);
			p.getInventory().setItem(1, bow);
			p.getInventory().setItem(2, rod);
			p.getInventory().setItem(3, wood);
			p.getInventory().setItem(8, food);
			p.getInventory().setItem(9, arrow);
			p.getInventory().setItem(4, axe);
		} else if(team.equals("blue")) {
			ItemStack blueHelmet = new ItemStack(Material.LEATHER_HELMET, 1, DyeColor.BLUE.getData());
			ItemStack blueChestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1, DyeColor.BLUE.getData());
			ItemStack blueLeggings = new ItemStack(Material.LEATHER_LEGGINGS, 1, DyeColor.BLUE.getData());
			ItemStack blueBoots = new ItemStack(Material.LEATHER_BOOTS, 1, DyeColor.BLUE.getData());
			ItemStack sword = new ItemStack(Material.STONE_SWORD);
			ItemStack bow = new ItemStack(Material.BOW);
			ItemStack arrow = new ItemStack(Material.ARROW, 64);
			ItemStack rod = new ItemStack(Material.FISHING_ROD);
			ItemStack wood = new ItemStack(Material.WOOD, 64);
			ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
			ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
			p.getInventory().setHelmet(blueHelmet);
			p.getInventory().setChestplate(blueChestplate);
			p.getInventory().setLeggings(blueLeggings);
			p.getInventory().setBoots(blueBoots);
			p.getInventory().setItem(0, sword);
			p.getInventory().setItem(1, bow);
			p.getInventory().setItem(2, rod);
			p.getInventory().setItem(3, wood);
			p.getInventory().setItem(8, food);
			p.getInventory().setItem(9, arrow);
			p.getInventory().setItem(4, axe);
		} else if(team.equals("spectator")) {
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "ci " + p.getName());
		}
	}
	
	public static Boolean gottaRestore = false;
	
	public static void startGame() {
		Teams.resetKills();
		gottaRestore = false;
		
		final Location lRed = new Location(Bukkit.getWorld("Warfare"), 0.5, 51.1, 36.5, 180, 0);
		final Location lBlue = new Location(Bukkit.getWorld("Warfare"), 0.5, 51.1, -43.5, 0, 0);
		
		inProgress = true;
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7¡La partida està &bempezando&7!"));
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b5..."));
			}
			
		}.runTaskLater(plugin, 40L);
		
		
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b4..."));
			}
			
		}.runTaskLater(plugin, 60L);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b3..."));
			}
			
		}.runTaskLater(plugin, 80L);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b2..."));
			}
			
		}.runTaskLater(plugin, 100L);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b1..."));
			}
			
		}.runTaskLater(plugin, 120L);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b¡Ya!"));
				for(final Player p : Bukkit.getOnlinePlayers()) {
					if(Teams.getTeam(p).equals("red")) {
						p.teleport(lRed);
					} else if(Teams.getTeam(p).equals("blue")) {
						p.teleport(lBlue);
					} else {
						Teams.setTeam(p, CTW.lastJoinToTeam);
						
						if(CTW.lastJoinToTeam.equals("red")) {
							CTW.lastJoinToTeam = "blue";
						} else {
							CTW.lastJoinToTeam = "red";
						}
						
						if(Teams.getTeam(p).equals("red")) {
							p.teleport(lRed);
						} else {
							p.teleport(lBlue);
						}
					}
				}
			}
			
		}.runTaskLater(plugin, 140L);
		
		
		new BukkitRunnable() {

			@Override
			public void run() {
				stopGame();
			}
			
		}.runTaskLater(plugin, 7640L);
		
	}
	
	public static Boolean isInProgress() {
		if(inProgress) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean hasToRestore() {
		return gottaRestore;
	}
	
	public static void stopGame() {
		inProgress = false;
		gottaRestore = true;
		Events.restore();
		for(Player p : Bukkit.getOnlinePlayers()) {
			Teams.setTeam(p, "spectator");
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7¡El juego ha terminado!"));
			if(getWinners().equals("blue")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo azul&7: &b" + Teams.getBlueWools()));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Ganadores: &b¡Equipo azul!"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo rojo&7: &b" + Teams.getRedWools()));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Perdedores: &bEquipo rojo"));
			} else if(getWinners().equals("red")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo rojo&7: &b" + Teams.getRedWools()));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aGanadores: &b¡Equipo rojo!"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo azul&7: &b" + Teams.getBlueWools()));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPerdedores: &bEquipo azul"));
			} else if(getWinners().equals("empate")) {
				if(Teams.getBlueKills() > Teams.getRedKills()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo azul&7: &b" + Teams.getBlueWools()));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Ganadores: &b¡Equipo azul!"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo rojo&7: &b" + Teams.getRedWools()));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Perdedores: &bEquipo rojo"));
				} else if(Teams.getBlueKills() == Teams.getRedKills()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b¡Empate!"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo rojo&7: &b" + Teams.getRedWools()));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo azul&7: &b" + Teams.getBlueWools()));
				} else if(Teams.getBlueWools() < Teams.getRedWools()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo rojo&7: &b" + Teams.getRedWools()));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aGanadores: &b¡Equipo rojo!"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lanas del &bequipo azul&7: &b" + Teams.getBlueWools()));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPerdedores: &bEquipo azul"));
				}
			}
			
		}
		Teams.resetWools();
		Teams.resetKills();
	}
	
}