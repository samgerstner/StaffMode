package pro.samgerstner.staffmode;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.SQLException;

public class StaffModeCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("Only players can use this command");
        }

        Player player = (Player) sender;
        switch(args[0])
        {
            case "enable":
            case "e":
                return enable(player, args[1]);

            case "disable":
            case "d":
                return disable(player, args[1]);

            case "help":
                player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                        "Usage: /staffmode <enable|disable> <role>");
                return true;

            default:
                player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                        "Invalid usage. Valid usage: /staffmode <enable|disable> <role>");
                return false;
        }
    }

    private boolean enable(Player player, String role)
    {
        Permission permission = StaffMode.permission;
        SQLHelper sql = StaffMode.sql;

        //Verify that user has appropriate permission
        String permString = String.format("staffmode.%s", role);
        if(!permission.has(player, permString) && !permission.has(player, "staffmode.*"))
        {
            player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                    "You do not have permission to enable the requested role.");
            return true;
        }

        //Check if role exists
        String groups[] = permission.getGroups();
        boolean found = false;
        for(String group : groups)
        {
            if(group.equals(role))
            {
                found = true;
                break;
            }
        }

        if(!found)
        {
            player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                  "Could not find the requested role.");
            return true;
        }

        //If logging is enabled, create log entry
        if(StaffMode.config.getBoolean("general-settings.log-staffmode-enable"))
        {
            try
            {
                sql.logEnable(player.getUniqueId().toString(), player.getName(), role);
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
                player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                      "There was an error logging the operation. Please contact your administrator.");
                return true;
            }
        }

        //Use Vault API to add permission group for role
        permission.playerAddGroup(player, role);

        player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.GRAY +
              "Successfully enabled your requested role.");
        return true;
    }

    private boolean disable(Player player, String role)
    {
        Permission permission = StaffMode.permission;
        SQLHelper sql = StaffMode.sql;

        //Verify that user has appropriate permission
        String permString = String.format("staffmode.%s", role);
        if(!permission.has(player, permString) && !permission.has(player, "staffmode.*"))
        {
            player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                  "You do not have permission to enable the requested role.");
            return true;
        }

        //Check if role exists
        String groups[] = permission.getGroups();
        boolean found = false;
        for(String group : groups)
        {
            if(group.equals(role))
            {
                found = true;
                break;
            }
        }

        if(!found)
        {
            player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                  "Could not find the requested role.");
            return true;
        }

        //Verify that player is a member of role
        if(!permission.playerInGroup(player, role))
        {
            player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                  "The specified role is not currently active.");
            return true;
        }

        //If logging is enabled, create log entry
        if(StaffMode.config.getBoolean("general-settings.log-staffmode-disable"))
        {
            try
            {
                sql.logDisable(player.getUniqueId().toString(), player.getName(), role);
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
                player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.RED +
                      "There was an error logging the operation. Please contact your administrator.");
                return true;
            }
        }

        //Use Vault API to add permission group for role
        permission.playerRemoveGroup(player, role);

        player.sendMessage(ChatColor.AQUA + "[Staff Mode] " + ChatColor.GRAY +
              "Successfully disabled your requested role.");
        return true;
    }
}