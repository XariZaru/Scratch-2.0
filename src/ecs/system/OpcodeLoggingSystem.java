package ecs.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import database.DatabaseConnection;
import ecs.EntityCreationSystem;
import ecs.components.requests.LogOpcodeRequest;
import net.opcodes.RecvOpcode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OpcodeLoggingSystem extends IntervalIteratingSystem {

    EntityCreationSystem ecs;
    ComponentMapper<LogOpcodeRequest> logOpcodeRequests;
    Connection con;
    PreparedStatement ps;

    public OpcodeLoggingSystem() {
        super(Aspect.all(LogOpcodeRequest.class), 100);
    }

    @Override
    protected void begin() {
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("INSERT INTO logged_opcodes (opcode) values (?) ON DUPLICATE KEY UPDATE occurence = occurence + 1");
        } catch (SQLException e) {
            e.printStackTrace();;
        }
    }

    @Override
    protected void process(int entityId) {
        LogOpcodeRequest request = logOpcodeRequests.get(entityId);
        try {
            ps.setString(1, request.opcode.toString());
            ps.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        world.delete(entityId);
    }

    @Override
    protected void end() {
        try {
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();;
        } finally {
            try {
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void create(RecvOpcode opcode) {
        int e = ecs.create();
        LogOpcodeRequest request = logOpcodeRequests.create(e);
        request.opcode = opcode;
    }

}
