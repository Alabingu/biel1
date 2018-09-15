package alabingu.ctw;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Teams {
	
	public static ArrayList<Player> red = new ArrayList<Player>();
	public static ArrayList<Player> blue = new ArrayList<Player>();
	public static ArrayList<Player> spectator = new ArrayList<Player>();
	
	public static ArrayList<Integer> redWools = new ArrayList<Integer>();
	public static ArrayList<Integer> blueWools = new ArrayList<Integer>();
	public static ArrayList<Integer> redKills = new ArrayList<Integer>();
	public static ArrayList<Integer> blueKills = new ArrayList<Integer>();
	
	static {
		redWools.add(0);
		blueWools.add(0);
	}
	
	public static String getTeamColor(Player p) {
		if(red.contains(p)) {
			return "red";
		} else if(blue.contains(p)) {
			return "blue";
		} else {
			return "spectator";
		}
	}
	
	public static void setTeam(Player p, String team) {
		if(!team.equals("red")) {
			if(!team.equals("blue")) {
				if(!team.equals("spectator")) {
					throw new ArithmeticException("¡Equipo no valido!");
				} else {
					removeFromTeams(p);
					spectator.add(p);
					p.setGameMode(GameMode.SPECTATOR);
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "spawn " + p.getName());
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "heal " + p.getName());
					Game.giveKit(p, "spectator");
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams leave " + p.getName());
					
				}
			} else {
				removeFromTeams(p);
				blue.add(p);
				p.setGameMode(GameMode.SURVIVAL);
				Game.giveKit(p, "blue");
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams leave " + p.getName());
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams join blue " + p.getName());
			}
		} else {
			removeFromTeams(p);
			red.add(p);
			p.setGameMode(GameMode.SURVIVAL);
			Game.giveKit(p, "red");
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams leave " + p.getName());
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams join red " + p.getName());
		}
	}
	
	public static void removeFromTeams(Player p) {
		if(red.contains(p)) {
			red.remove(p);
		} else if(blue.contains(p)) {
			blue.remove(p);
		} else if(spectator.contains(p)) {
			spectator.remove(p);
		} else {
			
		}
	}
	
	public static int getBlueWools() {
		return blueWools.get(0);
	}
	
	public static int getRedWools() {
		return redWools.get(0);
	}
	
	public static int getBlueKills() {
		return blueKills.get(0);
	}
	
	public static int getRedKills() {
		return redKills.get(0);
	}
	
	public static void addWool(String team, Player scorer) {
		if(team.equals("blue")) {
			blueWools.set(0, blueWools.get(0) + 1);
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[Azul] &7¡El jugador &b" + scorer.getName() + "&7 ha sumado un punto! (&9" + getBlueWools() + "&7 - &c" + getRedWools() + "&7)"));
			}
			
		} else if(team.equals("red")) {
			redWools.set(0, redWools.get(0) + 1);
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[Rojo] &7¡El jugador &b" + scorer.getName() + "&7 ha sumado un punto! (&9" + getBlueWools() + "&7 - &c" + getRedWools() + "&7)"));
			}
		}
	}
	
	public static void addKill(String team) {
		if(team.equals("blue")) {
			blueKills.set(0, blueWools.get(0) + 1);
		} else if(team.equals("red")) {
			redKills.set(0, redWools.get(0) + 1);
		}
	}
	
	public static void resetWools() {
		blueWools.set(0, 0);
		redWools.set(0, 0);
	}
	
	public static void resetKills() {
		blueWools.set(0, 0);
		redWools.set(0, 0);
	}
	
}