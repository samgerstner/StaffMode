package pro.samgerstner.staffmode;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

public class StaffMode extends JavaPlugin
{
    public static Logger logger = null;
    public static FileConfiguration config = null;
    public static SQLHelper sql = null;
    public static Economy economy = null;
    public static Permission permission = null;
    public static Chat chat = null;

    @Override
    public void onEnable()
    {
        logger = getLogger();
        logger.info("Enabling StaffMode v1.0.0...");

        //Check if the config.yml file exists
        File configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists())
        {
            this.saveDefaultConfig();
        }

        //Get config file
        config = this.getConfig();

        //Call database setup if logging is enabled
        if(config.getBoolean("general-settings.log-staffmode-enable") ||
                config.getBoolean("general-settings.log-staffmode-disable"))
        {
            try
            {
                sql = new SQLHelper(config.getString("database-settings.host"),
                        config.getString("database-settings.port"),
                        config.getString("database-settings.database"),
                        config.getString("database-settings.username"),
                        config.getString("database-settings.password"));
                sql.init();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
                logger.severe("Failed to enable required dependency: MySQL");
            }
        }

        //Setup Vault
        if (!setupEconomy())
        {
            getLogger().severe("Disabling plugin due to missing required dependency: Vault");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();

        //Register the plugin command
        this.getCommand("staffmode").setExecutor(new StaffModeCommand());
    }

    @Override
    public void onDisable()
    {
        logger.info("Disabling StaffMode v1.0.0...");
    }

    private boolean setupEconomy()
    {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }
}