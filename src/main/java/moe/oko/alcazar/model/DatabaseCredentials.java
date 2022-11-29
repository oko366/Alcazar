package moe.oko.alcazar.model;

public record DatabaseCredentials(
        String username,
        String password,
        String host,
        int port,
        String db,
        int poolSize,
        long connTimeout,
        long idleTimeout,
        long maxLifetime
) {}
