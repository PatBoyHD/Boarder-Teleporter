package de.patboyhd.boarderteleporter.listeners;

import de.patboyhd.boarderteleporter.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class MovementListener implements Listener {



    double x_min = -12000;  //west = -x
    double x_max = 12100;   //east = +x
    double z_min = -6144;  //north = -z
    double z_max = 6143;   //east = +z
    int distancetpborder = 10;

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
            location.setX(x_max - distancetpborder);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the west direction, you came out in the east!");
        } else if (player.getLocation().getX() >= x_max) { //if at east side (+x direction)
            location.setX(x_min + distancetpborder);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the east direction, you came out in the west!");
        }
        //if at north side (-z direction)
        if (player.getLocation().getZ() <= z_min) {
            location.setZ(z_min + distancetpborder);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You have reached the end of the world.");
        } else if (player.getLocation().getZ() >= z_max) { //if at south side (+z direction)
            location.setZ(z_max - distancetpborder);
            player.teleport(findSaveSpot(location, player));
            player.sendMessage(ChatColor.GOLD + "You have reached the end of the world.");
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
        //Location finalSafelocation;

        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        //if at west side (-x direction)
        if (player.getLocation().getX() <= x_min) {
            location.setX(x_max - distancetpborder);
            Location finalSafelocation = findSaveSpot(location, player);
            player.getWorld().loadChunk(finalSafelocation.getChunk());
            for (int i = 0; i < entitylist.size(); i++) {
                Entity entity = ((Entity) entitylist.get(i));
                entity.leaveVehicle();
            }
            vehicle.teleport(finalSafelocation);
            scheduler.runTaskLater(plugin, () -> {
                scheduledVehicleTeleport(entitylist, finalSafelocation, vehicle);
            }, 20L );
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the west direction, you came out in the east!");

        } else if (player.getLocation().getX() >= x_max) { //if at east side (+x direction)
            location.setX(x_min + distancetpborder);
            Location finalSafelocation = findSaveSpot(location, player);
            player.getWorld().loadChunk(finalSafelocation.getChunk());
            for (int i = 0; i < entitylist.size(); i++) {
                Entity entity = ((Entity) entitylist.get(i));
                entity.leaveVehicle();
            }
            vehicle.teleport(finalSafelocation);
            scheduler.runTaskLater(plugin, () -> {
                scheduledVehicleTeleport(entitylist, finalSafelocation, vehicle);
            }, 20L );
            player.sendMessage(ChatColor.GOLD + "You traveled so far into the east direction, you came out in the west!");
        }


        //if at north side (-z direction)
        if (player.getLocation().getZ() <= z_min) {
            location.setZ(z_min + distancetpborder);
            Location finalSafelocation = findSaveSpot(location, player);
            player.getWorld().loadChunk(finalSafelocation.getChunk());
            for (int i = 0; i < entitylist.size(); i++) {
                Entity entity = ((Entity) entitylist.get(i));
                entity.leaveVehicle();
            }
            vehicle.teleport(finalSafelocation);
            scheduler.runTaskLater(plugin, () -> {
                scheduledVehicleTeleport(entitylist, finalSafelocation, vehicle);
            }, 20L );
            player.sendMessage(ChatColor.GOLD + "You have reached the end of the world.");

        } else if (player.getLocation().getZ() >= z_max) { //if at south side (+z direction)
            location.setZ(z_max - distancetpborder);
            Location finalSafelocation = findSaveSpot(location, player);
            player.getWorld().loadChunk(finalSafelocation.getChunk());
            for (int i = 0; i < entitylist.size(); i++) {
                Entity entity = ((Entity) entitylist.get(i));
                entity.leaveVehicle();
            }
            vehicle.teleport(finalSafelocation);
            scheduler.runTaskLater(plugin, () -> {
                scheduledVehicleTeleport(entitylist, finalSafelocation, vehicle);
            }, 20L );
            player.sendMessage(ChatColor.GOLD + "You have reached the end of the world.");
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
