package components;

import com.artemis.Component;
import net.encryption.MapleAESOFB;
import constants.ScratchConstants;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AESOFB extends Component {
	
	public MapleAESOFB sendCypher, recvCypher;
	public final Lock encodeLock = new ReentrantLock(true);
	
	private byte key[] = {
            (byte) 0x61, 0x00, 0x00, 0x00, 
            (byte) 0xD8, 0x00, 0x00, 0x00, 
            (byte) 0x4A, 0x00, 0x00, 0x00,
            (byte) 0x49, 0x00, 0x00, 0x00,
            (byte) 0xE0, 0x00, 0x00, 0x00, 
                   0x1E, 0x00, 0x00, 0x00, 
            (byte) 0xF8, 0x00, 0x00, 0x00, 
                   0x7A, 0x00, 0x00, 0x00
        };
	
	public byte ivRecv[] = {70, 114, 122, 82};
	public byte ivSend[] = {82, 48, 120, 115};
	
	
	public AESOFB() {
        ivRecv[3] = (byte) (Math.random() * 255);
        ivSend[3] = (byte) (Math.random() * 255);

	    sendCypher = new MapleAESOFB(key, ivSend, (short) (0xFFFF - ScratchConstants.VERSION));
	    recvCypher = new MapleAESOFB(key, ivRecv, (short) ScratchConstants.VERSION);
	}

}
