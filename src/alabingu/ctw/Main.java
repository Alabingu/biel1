package alabingu.ctw;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import alabingu.ctw.events.Events;

public class CTW extends JavaPlugin {
	
	PluginDescriptionFile pdffile = getDescription();
	String version = pdffile.getVersion();
	public static String lastJoinToTeam = "red";
	
	public void onEnable() {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7¡El plugin de &bcaptura la lana&7 versión &b" + version + "&7 ha sido activado!"));
		registerEvents();
		new BukkitRunnable() {

			@Override
			public void run() {
				if(!Game.isInProgress()) {
					if(Game.canStart()) {
						Game.startGame();
					}
				}
			}
			
		}.runTaskTimer(this, 500L, 500L);
	}
	
	public void onDisable() {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7¡El plugin de &bcaptura la lana&7 versión &b" + version + "&7 ha sido desactivado!"));
		Events.restore();
	}
	
	public void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new Events(), this);
	}
}