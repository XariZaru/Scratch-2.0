package systems;

import com.artemis.ComponentMapper;
import ecs.EntityCreationSystem;
import net.MaplePacketCreator;
import requests.LoginRequest;
import database.DatabaseConnection;
import io.netty.channel.Channel;
import net.Key;
import net.PacketHandler;
import net.components.Client;
import net.encryption.BCrypt;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;
import net.packets.PacketTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSystemHandler extends PacketHandler {

    ComponentMapper<Client> clients;
    ComponentMapper<LoginRequest> requests;
    EntityCreationSystem ecs;

    public LoginSystemHandler() {
        super(LoginRequest.class);
    }

    @Override
    protected void process(int e) {
        try {
            PacketTask task = packetTasks.get(e);
            InboundPacket packet = task.inPacket;
            String login = packet.readMapleAsciiString();
            String pwd   = packet.readMapleAsciiString();
            Result result = attempt(task.ch, login, pwd);
            if (result != Result.SUCCESS) {
                task.ch.writeAndFlush(MaplePacketCreator.getLoginFailed(result.getValue()));
            }
        } finally {
            requests.remove(e);
            super.process(e);
        }
    }

    @Override
    public void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound) {
        int e = channel.attr(Key.ENTITY).get();
        create(channel, inBound, outBound);
        requests.create(e);
    }

    public Result attempt(Channel ch, String login, String pwd) {

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM accounts WHERE name = ?")) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                Result result = attempt(rs, pwd);
                if (result != Result.SUCCESS) return result;

                Client client = clients.create(ch.attr(Key.ENTITY).get());
                client.accountId = rs.getInt("id");
                client.accountName = rs.getString("name");
                client.gender = rs.getInt("gender");
                client.gmLevel = rs.getInt("gm");
                ch.attr(Key.CLIENT).set(client);

                ch.writeAndFlush(MaplePacketCreator.getAuthSuccess(client));

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Result.SUCCESS;
    }

    private Result attempt(ResultSet rs, String pwd) throws SQLException {
        if (!rs.next()) return Result.NOT_REGISTERED;
        if (!BCrypt.checkpw(pwd, rs.getString("password")))
            return Result.INCORRECT_PASSWORD;
        return Result.SUCCESS;
    }

    public enum Result {

        SUCCESS(0),
        DELETE_OR_BLOCKED(3),
        INCORRECT_PASSWORD(4),
        NOT_REGISTERED(5),
        SYSTEM_ISSUE(6),
        ALREADY_LOGGED_IN(7),
        SYSTEM_ERROR_2(8),
        SYSTEM_ERROR_3(9),
        TOO_MANY_CONNECTIONS(10),
        AGE_RESTRICTED(11),
        SUCCESS2(12),
        INVALID_MASTER_IP(13),
        WRONG_GATEWAY(14),
        STILL_PROCESSING(15),
        NEEDS_VERIFICATION(16),
        INVALID_GATEWAY(17),
        // 18-20 un-clicks box
        NEEDS_VERIFICATION2(21);


        private int value;
        private Result(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

}
