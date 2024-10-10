package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.Token;
import itstep.learning.dal.dto.User;

import java.sql.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TokenDao {
    private final Connection connection;
    private final Logger logger;
    private static final long TOKEN_LIFETIME = 1000 * 60 * 60 * 3;

    @Inject
    public TokenDao( Connection connection, Logger logger ) {
        this.connection = connection;
        this.logger = logger;
    }

    public User getUserByTokenId(UUID tokenId) throws Exception {
        String sql = "SELECT * FROM tokens t JOIN users u ON t.user_id = u.id WHERE t.token_id = ?";
        try (PreparedStatement prep = connection.prepareStatement( sql )){
         prep.setString( 1, tokenId.toString() );
         ResultSet rs = prep.executeQuery();
         if( rs.next() ) {
             Token token = new Token( rs );
             if(token.getExp().before( new Date() ) ) {
                 throw new Exception( "Token is expired" );
             }
             return new User( rs );
         }
         else {
             throw new Exception( "Token rejected" );
         }
        }
        catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- SQL Error", ex);
            throw new Exception( "Server error. Details on server logs" );
        }
    }

    public Token create(User user) {
        try {
            // Проверяем наличие активного токена
            String checkSql = "SELECT token_id, exp FROM tokens WHERE user_id = ? AND exp > ?";
            try (PreparedStatement checkPrep = connection.prepareStatement(checkSql)) {
                checkPrep.setString(1, user.getId().toString());
                checkPrep.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

                ResultSet rs = checkPrep.executeQuery();
                if (rs.next()) {
                    UUID tokenId = UUID.fromString(rs.getString("token_id"));
                    Timestamp exp = rs.getTimestamp("exp");

                    long halfExtension = TOKEN_LIFETIME / 2;
                    long newExpTime = exp.getTime() + halfExtension;

                    String updateSql = "UPDATE tokens SET exp = ? WHERE token_id = ?";
                    try (PreparedStatement updatePrep = connection.prepareStatement(updateSql)) {
                        updatePrep.setTimestamp(1, new Timestamp(newExpTime));
                        updatePrep.setString(2, tokenId.toString());
                        updatePrep.executeUpdate();
                    }

                    Token token = new Token();
                    token.setTokenId(tokenId);
                    token.setUserId(user.getId());
                    token.setIat(new Date(System.currentTimeMillis()));
                    token.setExp(new Date(newExpTime));

                    return token;
                }
            }

            Token newToken = new Token();
            newToken.setTokenId(UUID.randomUUID());
            newToken.setUserId(user.getId());
            newToken.setIat(new Date(System.currentTimeMillis()));
            newToken.setExp(new Date(System.currentTimeMillis() + TOKEN_LIFETIME));

            String insertSql = "INSERT INTO tokens (token_id, user_id, iat, exp) VALUES (?, ?, ?, ?)";
            try (PreparedStatement prep = connection.prepareStatement(insertSql)) {
                prep.setString(1, newToken.getTokenId().toString());
                prep.setString(2, newToken.getUserId().toString());
                prep.setTimestamp(3, new Timestamp(newToken.getIat().getTime()));
                prep.setTimestamp(4, new Timestamp(newToken.getExp().getTime()));
                prep.executeUpdate();
            }

            return newToken;

        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- SQL Error", ex);
            return null;
        }
    }

    public boolean installTables() {
        String sql =
                "CREATE TABLE IF NOT EXISTS tokens (" +
                        "token_id  CHAR(36)     PRIMARY KEY  DEFAULT( UUID() )," +
                        "user_id   CHAR(36)     NOT NULL," +
                        "exp       DATETIME     NULL," +
                        "iat       DATETIME     NOT NULL   DEFAULT CURRENT_TIMESTAMP" +
                        ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci";

        try( Statement stmt = connection.createStatement() ) {
            stmt.executeUpdate( sql );
            return true;
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }
    }
}
