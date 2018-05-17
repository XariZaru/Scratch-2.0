package systems;

import com.artemis.ComponentMapper;
import ecs.EntityCreationSystem;
import ecs.components.movement.Movement;
import io.netty.channel.Channel;
import net.Key;
import net.PacketHandler;
import net.opcodes.SendOpcode;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlayerMoveSystemHandler extends PacketHandler {

    Map<Byte, Integer> occurences = new HashMap<>();
    ComponentMapper<Movement> movements;
    EntityCreationSystem ecs;

    private void log(byte command) {
        switch (command) {
            case 0: /* normal move */
            case 5:
            case 17: // Float
                command = 0;
                break;
            case 1:
            case 2:
            case 6: // fj
            case 12:
            case 13: // Shot-jump-back thing
            case 16: // Float
            case 18:
            case 19: // Springs on maps
            case 20: // Aran Combat Step
            case 22:
                command = 1;
                break;
            case 3:
            case 4: // tele... -.-
            case 7: // assaulter
            case 8: // assassinate
            case 9: // rush
            case 11: //chair
//                case 14: {
                command = 2;
                break;
            case 10: // Change Equip
                command = 3;
                break;
            case 14: //This case causes map objects to disappear and reappear
                command = 4;
                break;
            case 15: // jumpdown
                command = 5;
                break;
            case 21://Causes aran to do weird stuff when attacking o.o
                    /*byte newstate = inBound.readByte();
                     short unk = inBound.readShort();
                     AranMovement am = new AranMovement(command, null, unk, newstate);
                     res.add(am);*/
                command = 6;
                break;
        }
        if (!occurences.containsKey(command))
            occurences.put(command, 0);
        occurences.put(command, 1 + occurences.get(command));
        for (Map.Entry entry : occurences.entrySet()) {
            System.out.print(entry.getKey() + "-" + entry.getValue() + ", ");
        }
        System.out.println();
    }

    @Override
    public void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound) {
        inBound.readByte();//fieldKey
        inBound.readInt();//fieldCRC?
        Point newPos = new Point(inBound.readShort(), inBound.readShort());
        byte numCommands = inBound.readByte();
        // TODO: player shop check or stuff like that
        // TODO: also check for status effects to prevent packet editing.
        if (numCommands == 0)
            return;
        int e = ecs.create();
        Movement movement = movements.create(e);
        movement.ch = channel;
        movement.newPosition = newPos;
        movement.read(numCommands, inBound);

    }

    @Override
    protected void process(int e) {
        try {
            Movement movement = movements.get(e);
            Channel ch = movement.ch;
            OutboundPacket outboundPacket = PlayerMoveSystemHandler.movePlayer(ch.attr(Key.ENTITY).get(), movement.newPosition, movement.movements);
            // TODO: Broadcast to all other players in the map
        } finally {
            world.delete(e);
        }
    }

    public static OutboundPacket movePlayer(int playerEntityId, Point newPosition, LinkedList<Movement.MovementCodec> movements) {
        final OutboundPacket send = new OutboundPacket();
        send.writeShort(SendOpcode.MOVE_PLAYER.getValue());
        send.writeInt(playerEntityId);
        send.writePos(newPosition);
        send.write(movements.size());
        movements.forEach(movement -> movement.encode(send));
        return send;
    }
}
