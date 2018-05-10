package requests;

import com.artemis.Component;
import io.netty.channel.Channel;
import net.packets.InboundPacket;

public class CreateCharRequest extends Component {
    public String name;
    public Channel ch;
    public InboundPacket packet;
}
