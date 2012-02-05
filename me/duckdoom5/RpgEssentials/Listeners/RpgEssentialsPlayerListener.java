package me.duckdoom5.RpgEssentials.Listeners;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import me.duckdoom5.RpgEssentials.RpgEssentials;
import me.duckdoom5.RpgEssentials.config.ConfigAdd;
import me.duckdoom5.RpgEssentials.levels.Farming;
import me.duckdoom5.RpgEssentials.levels.Firemaking;
import me.duckdoom5.RpgEssentials.levels.Fishing;
import me.duckdoom5.RpgEssentials.util.Hashmaps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.RenderDistance;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RpgEssentialsPlayerListener implements Listener{
	
    public static RpgEssentials plugin;
    public final Logger log = Logger.getLogger("Minecraft");
    static ConfigAdd addtoconfig = new ConfigAdd(plugin);
    static YamlConfiguration config = new YamlConfiguration();
    static YamlConfiguration playerconfig = new YamlConfiguration();
    static YamlConfiguration regionconfig = new YamlConfiguration();
    static YamlConfiguration levelconfig = new YamlConfiguration();
	private int currentlevel;
	private String skilltype;
	private int addexp;
    
    public RpgEssentialsPlayerListener(RpgEssentials instance) {
        plugin = instance; 
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
    	try {
    		levelconfig.load("plugins/RpgEssentials/Leveling.yml");
		} catch (Exception e) {
		}
    	Player player = event.getPlayer();
    	Block block = event.getClickedBlock();
    	Action action = event.getAction();
    	ItemStack inhand = player.getItemInHand();
    	if(levelconfig.getBoolean("Survival Gamemode Required") == true){
	    	if(player.getGameMode() == GameMode.SURVIVAL){
	    		if(action == Action.RIGHT_CLICK_BLOCK){
	    			Firemaking.check(inhand, block, player, plugin);
		    		Farming.soil(block, player, inhand, plugin, event);
		    	}
	    	}
    	}else{
    		if(action == Action.RIGHT_CLICK_BLOCK){
    			Firemaking.check(inhand, block, player, plugin);
	    		Farming.soil(block, player, inhand, plugin, event);
	    	}
    	}
    }
    
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
    	Fishing.check(event, plugin);
    }
    
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event){
    	try {
			playerconfig.load("plugins/RpgEssentials/players.yml");
		} catch (Exception e) {
		}
    	Player player = event.getPlayer();
    	SpoutPlayer splayer = (SpoutPlayer) player;
    	ItemStack pickedup = event.getItem().getItemStack();
    	int amount = pickedup.getAmount();
    	
    	if(pickedup.getDurability() == Hashmaps.customitemsmap.get("Bronze Coin").getCustomId()){
    		int money = playerconfig.getInt("players." + splayer.getName() + ".money");
    		playerconfig.set("players." + splayer.getName() + ".money", money + (1 * amount));
    		event.getItem().teleport(player.getLocation());
    		SpoutManager.getSoundManager().playCustomSoundEffect(plugin, splayer, "http://82.74.70.243/server/music/getmoney.wav", false, splayer.getLocation(), 0, 100);
    		event.getItem().remove();
    		event.setCancelled(true);
    	}else if(pickedup.getDurability() == Hashmaps.customitemsmap.get("Silver Coin").getCustomId()){
    		int money = playerconfig.getInt("players." + splayer.getName() + ".money");
    		playerconfig.set("players." + splayer.getName() + ".money", money + (5 * amount));
    		event.getItem().teleport(player.getLocation());
    		SpoutManager.getSoundManager().playCustomSoundEffect(plugin, splayer, "http://82.74.70.243/server/music/getmoney.wav", false, splayer.getLocation(), 0, 100);
    		event.getItem().remove();
    		event.setCancelled(true);
    	}else if(pickedup.getDurability() == Hashmaps.customitemsmap.get("Gold Coin").getCustomId()){
    		int money = playerconfig.getInt("players." + splayer.getName() + ".money");
    		playerconfig.set("players." + splayer.getName() + ".money", money + (10 * amount));
    		event.getItem().teleport(player.getLocation());
    		SpoutManager.getSoundManager().playCustomSoundEffect(plugin, splayer, "http://82.74.70.243/server/music/getmoney.wav", false, splayer.getLocation(), 0, 100);
    		event.getItem().remove();
    		event.setCancelled(true);
    	}
    	try {
			playerconfig.save("plugins/RpgEssentials/players.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
    	
    	ItemStack droped = event.getItemDrop().getItemStack();
    	
    	if(droped.getDurability() == Hashmaps.customitemsmap.get("Bronze Coin").getCustomId()){
    		event.getItemDrop().remove();
    	}else if(droped.getDurability() == Hashmaps.customitemsmap.get("Silver Coin").getCustomId()){
    		event.getItemDrop().remove();
    	}else if(droped.getDurability() == Hashmaps.customitemsmap.get("Gold Coin").getCustomId()){
    		event.getItemDrop().remove();
    	}
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
    	try {
			config.load("plugins/RpgEssentials/config.yml");
			playerconfig.load("plugins/RpgEssentials/players.yml");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	Player player = event.getPlayer();
    	
    	//set playername to config
    	addtoconfig.addplayer(player);
    	if(!plugin.useSpout){	
    		player.sendMessage(config.getString("spout.join.message"));
    	} else {
        	Player onplayer[];
            int j = (onplayer = plugin.getServer().getOnlinePlayers()).length;
            for(int i = 0; i < j; i++){
                Player joining = onplayer[i];
                SpoutPlayer sPlayer = (SpoutPlayer)joining;
                if(player.getName().length() > 26){
                    this.log.info(ChatColor.RED + "Player name is too long");
                } else {
                    sPlayer.sendNotification(player.getName(), "has joined the game", Material.getMaterial(config.getInt("spout.join.messageicon")));
                }
            }
    	}
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
    	Player player = event.getPlayer();
    	Player onplayer[];
        int j = (onplayer = plugin.getServer().getOnlinePlayers()).length;
        for(int i = 0; i < j; i++){
            Player leaveing = onplayer[i];
            SpoutPlayer sPlayer = (SpoutPlayer)leaveing;
            if(player.getName().length() > 26){
                this.log.info(ChatColor.RED + "Player name is too long");
            } else {
                sPlayer.sendNotification(player.getName(), "has left the game", Material.getMaterial(config.getInt("spout.leave.messageicon")));
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
    	try {
    		regionconfig.load("plugins/RpgEssentials/Regions.yml");
		} catch (Exception e) {
		}
    	
    	HashMap<LocalPlayer, String> inregion = new LinkedHashMap<LocalPlayer, String>();
    	Player player = event.getPlayer();
    	SpoutPlayer splayer = (SpoutPlayer)player;
    	Location to = event.getTo();
        WorldGuardPlugin worldguard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        LocalPlayer localplayer = worldguard.wrapPlayer(event.getPlayer());
        Vector curpos = localplayer.getPosition();
        World world = to.getWorld();
        RegionManager rm = worldguard.getRegionManager(world);
        ApplicableRegionSet regions = rm.getApplicableRegions(curpos);
        
        if(regions.size() == 0){
        	if(inregion.containsKey(localplayer)){
        		SpoutManager.getSoundManager().stopMusic(splayer);
        	}
        	inregion.remove(localplayer);
            return;
        }
        
        String regionname = "";
        for(Iterator iterator = regions.iterator(); iterator.hasNext();)
        {
            ProtectedRegion protectedregion = (ProtectedRegion)iterator.next();
            regionname = protectedregion.getId();
        }
        
        if(inregion.containsKey(localplayer) && inregion.get(localplayer).equals(regionname))
            return;
        
        String message = regionconfig.getString("Regions." + regionname + "message");
        String sub = regionconfig.getString("Regions." + regionname + "submessage");
        int icon = regionconfig.getInt("Regions." + regionname + "iconId");
        String music = regionconfig.getString("Regions." + regionname + "music");
        
        if(message != null && sub != null && icon != 0)
            if(message.length() <= 26 && sub.length() <= 26)
                splayer.sendNotification(message, sub, Material.getMaterial(icon));
            else
                System.out.println("SpoutEssentials: A region message is greater than 26chars");
        if(music != null)
            SpoutManager.getSoundManager().playCustomMusic(plugin, splayer, music, false);
        String fog = regionconfig.getString("Regions." + regionname + "fog");
        if(fog != null && fog.equalsIgnoreCase("tiny"))
            splayer.setRenderDistance(RenderDistance.TINY);
        if(fog != null && fog.equalsIgnoreCase("short"))
            splayer.setRenderDistance(RenderDistance.SHORT);
        if(fog != null && fog.equalsIgnoreCase("normal"))
            splayer.setRenderDistance(RenderDistance.NORMAL);
        if(fog != null && fog.equalsIgnoreCase("far"))
            splayer.setRenderDistance(RenderDistance.FAR);
        
    }
    
    //onPlayerPortal,onPlayerLogin,onPlayerRespawn,onPlayerTeleport,onPlayerKick


}
