package MasterServer.src.requests;

import com.artemis.Component;
import io.netty.channel.Channel;
import net.packets.InboundPacket;

public class ServerListRequest extends Component {
    public Channel ch;
    public InboundPacket packet;
}
