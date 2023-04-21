package de.patboyhd.boarderteleporter.listeners;

import de.patboyhd.boarderteleporter.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class MovementListener implements Listener {

    //east = +x
    //west = -x

    double x_min = -12000;
    double x_max = 12100;

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
        double x = event.getTo().getX() - event.getFrom().getX();
        double y = event.getTo().getY() - event.getFrom().getY();
        double z = event.getTo().getZ() - event.getFrom().getZ();



        //Wenn man im Westen ist (- Richtung)
        if (player.getLocation().getX() <= x_min) {
            location.setX(x_max - 1);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the west direction, you came out in the east!");
        } else if (player.getLocation().getX() >= x_max) { //Wenn man im Osten ist (+ Richtung)
            location.setX(x_min + 1);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the east direction, you came out in the west!");
        }


    }

}
