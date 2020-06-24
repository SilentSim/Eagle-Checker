package me.Simo.EagleChecker;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		saveConfig();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			//check %player%
			if (label.equalsIgnoreCase("check")) {
				if (sender.hasPermission("eaglechecker.admin")) {
					if (args.length == 0) {
						p.sendMessage(getConfig().getString(format("Incorrect usage message: ")));
					}
					if (args.length >= 1) {
						if (Bukkit.getPlayerExact(args[0]) != null) {
							Player target = Bukkit.getPlayerExact(args[0]);
							//Location targetLoc = target.getLocation();
							UUID targetID = target.getUniqueId();
							String targetName = targetID.toString();
							Location targetLoc = target.getLocation();
							getConfig().set(targetName, targetLoc);
							target.teleport((Location) getConfig().get("JailLoc"));
							return true;
						}
						sender.sendMessage(ChatColor.DARK_RED + "Player not found");
						
						if (args[0].equalsIgnoreCase("free")) {
							if (Bukkit.getPlayerExact(args[1]) != null) {
								Player targetFree = Bukkit.getPlayerExact(args[1]);
								Location location = (Location) getConfig().get(targetFree.getUniqueId().toString());
								if (location != null) {
									targetFree.teleport(location);
									
								} else {
									sender.sendMessage(ChatColor.RED + "No location stored for that player!");
								}
							}
						}
						if (args[0].equalsIgnoreCase("cheater")) {
							if (Bukkit.getPlayerExact(args[1]) != null) {
								Player playerBanned = Bukkit.getPlayerExact(args[1]);
								StringBuilder sb = new StringBuilder();
								for (int i = 2; i < args.length; i++) {
									sb.append(args[i]).append(" ");
								}
								String str = sb.toString();
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + playerBanned.getName() + " " + str);
								}
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You need the eaglechecker.admin perm!!");
					}
				}
			
			if (label.equalsIgnoreCase("checksettp")) {
				getConfig().set("JailLoc", p.getLocation());
			}
			
			} else {
				sender.sendMessage(ChatColor.RED + "You ain't no human!");
			}
		
		return true;
	}
	
	public String format(String format){
		return ChatColor.translateAlternateColorCodes('&', format);
		}
	
	@EventHandler
	final public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
		Player csender = e.getPlayer();
		if (getConfig().contains(csender.getUniqueId().toString())) {
			if (!e.getMessage().startsWith("/helpop")) {
				e.setCancelled(true);
			}
		}
	}
	
}
