package main.java.bungeJoin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class BungeJoin extends Plugin implements Listener {
	private Map<ProxiedPlayer, Boolean> Players = new HashMap<ProxiedPlayer, Boolean>();
	
    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
    }
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
    	final String playername = event.getPlayer().getName();
    	final ProxiedPlayer player = event.getPlayer();
    	onJoinSetPlayer(event.getPlayer());
    	getProxy().getScheduler().schedule(this, new Runnable() {
			public void run() {
				onJoinCheckPlayer(player,playername);
			}
        }, 1L, TimeUnit.SECONDS);
    }
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onLogout(PlayerDisconnectEvent event) {
    	if (Players.get(event.getPlayer()) == true) { 
	        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
	            player.sendMessage("§e" + event.getPlayer().getName()+" left the server");
	        }
    	}
    	Players.put(event.getPlayer(), false);
    }
    public void onJoinSetPlayer(ProxiedPlayer playername) {
    	Players.put(playername, false);
    }
    
    @SuppressWarnings("deprecation")
	public void onJoinCheckPlayer(ProxiedPlayer player,String playername) {
    	if (getProxy().getPlayer(playername) != null) { Players.put(player, true); }
    	if (Players.get(player) == true) { 
	        for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
	            players.sendMessage("§e" + playername+" joined the server");
	        }
    	}
    }
}
