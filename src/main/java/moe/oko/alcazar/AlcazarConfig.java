package moe.oko.alcazar;

import moe.oko.alcazar.model.DatabaseCredentials;
import org.bukkit.configuration.file.FileConfiguration;

public class AlcazarConfig {
    private final FileConfiguration config;
    public AlcazarConfig(FileConfiguration config) { this.config = config; }

    public String getWelcomeMessage() { return config.getString("welcome.welcomeMessage"); }
    public AlcazarDB createDB(Alcazar plugin) { return new AlcazarDB(plugin, getCredentials()); }

    private DatabaseCredentials getCredentials() {
        var sql = config.getConfigurationSection("sql");
        return new DatabaseCredentials(
                sql.getString("username"),
                sql.getString("password"),
                sql.getString("host"),
                sql.getInt("port"),
                sql.getString("database"),
                sql.getInt("poolSize"),
                sql.getLong("connectionTimeout"),
                sql.getLong("idleTimeout"),
                sql.getLong("maxLifetime"));
    }
}
