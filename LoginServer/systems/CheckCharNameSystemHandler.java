package systems;

import database.DatabaseConnection;
import io.netty.channel.Channel;
import net.PacketHandler;
import net.opcodes.SendOpcode;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckCharNameSystemHandler extends PacketHandler {

	public static boolean charNameInUse(String name) {
		try (
			Connection con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE name = ?")) {
				ps.setString(1, name);
				try (ResultSet rs = ps.executeQuery()) {
					return rs.next();
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void receive(Channel channel, InboundPacket packet, OutboundPacket outBound) {
		String name = packet.readMapleAsciiString();
		boolean inUse = CheckCharNameSystemHandler.charNameInUse(name);
		channel.writeAndFlush(CheckCharNameSystemHandler.charNameResponse(name, inUse));
	}

	public static OutboundPacket charNameResponse(String charname, boolean nameUsed) {
		final OutboundPacket mplew = new OutboundPacket();
		mplew.writeShort(SendOpcode.CHAR_NAME_RESPONSE.getValue());
		mplew.writeMapleAsciiString(charname);
		mplew.write(nameUsed ? 1 : 0);
		return mplew;
	}

}
