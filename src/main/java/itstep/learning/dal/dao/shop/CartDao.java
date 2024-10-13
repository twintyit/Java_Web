package itstep.learning.dal.dao.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CartDao {
    private final Connection connection;
    private final Logger logger;

    @Inject
    public CartDao(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    public boolean add(UUID userId, UUID productId, int cnt ) {
        UUID cartId = null;
        String sql = "SELECT c.cart_id FROM carts c WHERE c.user_id = ? AND c.close_dt is NULL";
        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setString(1, userId.toString() );
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                cartId = UUID.fromString( rs.getString(1) );
            }
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }

        if(cartId == null) {
            cartId = UUID.randomUUID();
            sql = "INSERT INTO carts (cart_id, user_id) VALUES (?, ?)";
            try( PreparedStatement prep = connection.prepareStatement(sql)){
                prep.setString(1, cartId.toString() );
                prep.setString(2, userId.toString());
                prep.executeUpdate();
            }
            catch( SQLException ex ) {
                logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
                return false;
            }
        }

        int count;
        sql = "SELECT COUNT(*) from cart_items c where c.cart_id = ? and c.product_id = ?";
        try( PreparedStatement prep = connection.prepareStatement(sql)){
            prep.setString(1, cartId.toString() );
            prep.setString(2, productId.toString());
            ResultSet rs = prep.executeQuery();
            rs.next();
            count = rs.getInt(1);
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }
        if(count == 0 ){
            sql = "INSERT INTO cart_items (cnt, cart_id, product_id) VALUES (?, ?, ?)";
        }
        else {
            sql = "UPDATE cart_items c SET c.cnt = c.cnt + ? WHERE c.cart_id = ? AND c.product_id = ?";
        }
        try( PreparedStatement prep = connection.prepareStatement(sql)){
            prep.setInt(1,( cnt) );
            prep.setString(2, cartId.toString());
            prep.setString(3, productId.toString());
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }
        return true;
    }

    public boolean installTables() {
        String sql =
                "CREATE TABLE IF NOT EXISTS carts (" +
                        "cart_id      CHAR(36)      PRIMARY KEY  DEFAULT( UUID() )," +
                        "user_id      CHAR(36)   NOT NULL," +
                        "open_dt      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        "close_dt     DATETIME          NULL," +
                        "is_canceled  TINYINT       NOT NULL DEFAULT 0" +
                        ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci";

        try( Statement stmt = connection.createStatement() ) {
            stmt.executeUpdate( sql );
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }

        sql =
                "CREATE TABLE IF NOT EXISTS cart_items (" +
                        "cart_id     CHAR(36)      DEFAULT( UUID() )," +
                        "product_id  CHAR(36)  NOT NULL," +
                        "cnt         INT       NOT NULL DEFAULT 1," +
                        "  PRIMARY KEY (cart_id, product_id )" +
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
