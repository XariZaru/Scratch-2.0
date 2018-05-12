package ecs.components.item;

import com.artemis.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Expiration extends Component {
    public long expiration = -1;

    public static void generate(ResultSet rs, Expiration expiration) throws SQLException {
        expiration.expiration = rs.getLong("expiration");
    }
}
