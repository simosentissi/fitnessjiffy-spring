package net.steveperkins.fitnessjiffy.service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationService {

    private static class Session {

        private UUID id;
        private UUID userId;
        private Date expiration;

        public Session(
                @Nonnull UUID userId,
                @Nonnull Date expiration
        ) {
            this.id = UUID.randomUUID();
            this.userId = userId;
            this.expiration = expiration;
        }

        @Nonnull
        public UUID getId() {
            return id;
        }
        public void setId(@Nonnull UUID id) {
            this.id = id;
        }
        @Nonnull
        public UUID getUserId() {
            return userId;
        }
        public void setUserId(@Nonnull UUID userId) {
            this.userId = userId;
        }
        @Nonnull
        public Date getExpiration() {
            return expiration;
        }
        public void setExpiration(@Nonnull Date expiration) {
            this.expiration = expiration;
        }

    }

    private Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    @Nullable
    public UUID validateSessionToken(@Nullable String token) {
        UUID userId = null;
        try {
            UUID id = UUID.fromString(token);
            Session session = sessions.get(id);
            if(session != null && session.getExpiration().getTime() < new Date().getTime()) {
                // When a session exists but has expired, clean it up.
                sessions.remove(id);
            } else if(session != null) {
                userId = session.getUserId();
            }
        } catch(Exception e) {
            // TODO: log warning or info level
        }
        return userId;
    }

    // TODO: Add methods to insert new authenticated sessions in the "sessions" Map (i.e. either through successful login form submission or OpenID/OAuth).

}