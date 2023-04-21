package de.patboyhd.boarderteleporter.listeners;

import de.patboyhd.boarderteleporter.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {



    double x_min = -12000;  //west = -x
    double x_max = 12100;   //east = +x

    Main plugin;

    public MovementListener(Main plugin) {
        this.plugin = plugin;
    }

    private Location findSaveSpot(Location location, Player player) {
        double i = player.getWorld().getHighestBlockYAt(location);
        location.setY(i + 1);
        return location;
    }



    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();


        //if at west side (-x direction)
        if (player.getLocation().getX() <= x_min) {
            location.setX(x_max - 1);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the west direction, you came out in the east!");
        } else if (player.getLocation().getX() >= x_max) { //if at east side (+x direction)
            location.setX(x_min + 1);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the east direction, you came out in the west!");
        }


    }

}
