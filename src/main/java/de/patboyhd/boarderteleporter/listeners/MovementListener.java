package de.patboyhd.boarderteleporter.listeners;

import de.patboyhd.boarderteleporter.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class MovementListener implements Listener {



    double x_min = -12000;  //west = -x
    double x_max = 12100;   //east = +x
    int distancetpboarder = 10;

    Main plugin;

    public MovementListener(Main plugin) {
        this.plugin = plugin;
    }

    private Location findSaveSpot(Location location, Player player) {
        double i = player.getWorld().getHighestBlockYAt(location);
        location.setY(i + 1);
        return location;
    }


    private void teleportPlayer(Player player) {
        Location location = player.getLocation();


        //if at west side (-x direction)
        if (player.getLocation().getX() <= x_min) {
            location.setX(x_max - distancetpboarder);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the west direction, you came out in the east!");
        } else if (player.getLocation().getX() >= x_max) { //if at east side (+x direction)
            location.setX(x_min + distancetpboarder);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the east direction, you came out in the west!");
        }

    }

    private void scheduledVehicleTeleport(List entitylist, Location safelocation, Entity vehicle) {
        for (int i = 0; i < entitylist.size(); i++) {
            Entity entity = ((Entity) entitylist.get(i));
            vehicle.addPassenger(entity);
        }
    }

    private void teleportVehicle(Player player) {
        Location location = player.getLocation();
        Entity vehicle = player.getVehicle();
        List entitylist = vehicle.getPassengers();
        Location safelocation;

        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        //if at west side (-x direction)
        if (player.getLocation().getX() <= x_min) {
            location.setX(x_max - distancetpboarder);
            safelocation = findSaveSpot(location, player);
            player.getWorld().loadChunk(safelocation.getChunk());
            for (int i = 0; i < entitylist.size(); i++) {
                Entity entity = ((Entity) entitylist.get(i));
                entity.leaveVehicle();
            }
            vehicle.teleport(safelocation);
            scheduler.runTaskLater(plugin, () -> {
                scheduledVehicleTeleport(entitylist, safelocation, vehicle);
            }, 20L );
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the west direction, you came out in the east!");

        } else if (player.getLocation().getX() >= x_max) { //if at east side (+x direction)
            location.setX(x_min + distancetpboarder);
            safelocation = findSaveSpot(location, player);
            player.getWorld().loadChunk(safelocation.getChunk());
            for (int i = 0; i < entitylist.size(); i++) {
                Entity entity = ((Entity) entitylist.get(i));
                entity.leaveVehicle();
            }
            vehicle.teleport(safelocation);
            scheduler.runTaskLater(plugin, () -> {
                scheduledVehicleTeleport(entitylist, safelocation, vehicle);
            }, 20L );
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the east direction, you came out in the west!");
        }


    }



    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = plugin.getServer().getWorld("world");

        if (player.getWorld().equals(world)) {
            if (player.isInsideVehicle()) {
                teleportVehicle(player);
            } else {
                teleportPlayer(player);
            }
        }

    }

}
