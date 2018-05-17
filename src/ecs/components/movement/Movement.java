package ecs.components.movement;

import com.artemis.Component;
import io.netty.channel.Channel;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;

import java.awt.*;
import java.util.LinkedList;

public class Movement extends Component {

    public LinkedList<MovementCodec> movements = new LinkedList<>();
    public Channel ch;
    public Point newPosition;

    public void read(byte numCommands, InboundPacket inBound) {

        MovementCodec movement = null;

        for (byte i = 0; i < numCommands; i++) {
            byte command = inBound.readByte();

            switch (command) {
                case 0: // normal move
                case 5:
                case 17: // Float
                    movements.add(new AbsoluteMovement());
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
                    movements.add(new RelativeMovement());
                    break;
                case 3:
                case 4: // tele... -.-
                case 7: // assaulter
                case 8: // assassinate
                case 9: // rush
                case 11: //chair
                    movements.add(new TeleportMovement());
                    break;
                case 10: // Change Equip
                    movements.add(new EquipChange());
                    break;
                case 14: {//This case causes map objects to disappear and reappear TODO: UnknownMovement ?
                    short xpos = inBound.readShort();
                    short ypos = inBound.readShort();
                    short fhId = inBound.readShort();
                    byte newstate = inBound.readByte();
                    short duration = inBound.readShort();

				/*	UnknownMovement unkM = new UnknownMovement(command, new Point(xpos, ypos), duration, newstate);
					unkM.setUnk(fhId);
					res.add(unkM); */
                    break;
                }
                case 15:
                    movements.add(new JumpDownMovement());
                    break;
                case 21: {//Causes aran to do weird stuff when attacking o.o
                    /*byte newstate = inBound.readByte();
                     short unk = inBound.readShort();
                     AranMovement am = new AranMovement(command, null, unk, newstate);
                     res.add(am);*/
                    byte newstate = inBound.readByte();
                    short duration = inBound.readShort();
                    break;
                }
                default:
            }
            if (movement != movements.getLast()) {
                movement = movements.getLast();
                movements.getLast().decode(inBound);
            }
        }
    }

    public static abstract class MovementCodec {

        byte command, newState;
        short duration;

        public abstract void encode(OutboundPacket packet);
        public abstract void decode(InboundPacket packet);
    }

    public static class EquipChange extends MovementCodec {

        byte wui;

        @Override
        public void encode(OutboundPacket packet) {
            packet.write(10);
            packet.write(wui);
        }

        @Override
        public void decode(InboundPacket packet) {
            wui = packet.readByte();
        }
    }

    public static class JumpDownMovement extends AbsoluteMovement {

        short fh;

        @Override
        public void encode(OutboundPacket packet) {
            packet.write(command);
            packet.writeShort(xPos);
            packet.writeShort(yPos);
            packet.writeShort(velocityX);
            packet.writeShort(velocityY);
            packet.writeShort(fhId);
            packet.writeShort(fh);
            packet.write(newState);
            packet.writeShort(duration);
        }

        @Override
        public void decode(InboundPacket packet) {
            xPos = packet.readShort();
            yPos = packet.readShort();
            velocityX = packet.readShort();
            velocityY = packet.readShort();
            fhId = packet.readShort();
            fh = packet.readShort();
            command = packet.readByte();
            duration = packet.readShort();
        }
    }
    
    public static class TeleportMovement extends MovementCodec {

        short xPos, yPos, fhId;
        
        @Override
        public void encode(OutboundPacket packet) {
            packet.write(command);
            packet.writeShort(xPos);
            packet.writeShort(yPos);
            packet.writeShort(fhId);
            packet.write(newState);
            packet.writeShort(duration);
        }

        @Override
        public void decode(InboundPacket packet) {
            xPos = packet.readShort();
            yPos = packet.readShort();
            fhId = packet.readShort();
            command = packet.readByte();
            duration = packet.readShort();
        }
    }

    public static class RelativeMovement extends MovementCodec {

        short velocityX, velocityY;

        @Override
        public void encode(OutboundPacket packet) {
            packet.write(command);
            packet.writeShort(velocityX);
            packet.writeShort(velocityY);
            packet.write(newState);
            packet.writeShort(duration);
        }

        @Override
        public void decode(InboundPacket packet) {
            velocityX = packet.readShort();
            velocityY = packet.readShort();
            newState = packet.readByte();
            duration = packet.readShort();
        }
    }

    public static class AbsoluteMovement extends MovementCodec {

        short xPos;
        short yPos;
        short velocityX;
        short velocityY;
        short fhId;

        @Override
        public void encode(OutboundPacket packet) {
            packet.write(command);
            packet.writeShort(xPos);
            packet.writeShort(yPos);
            packet.writeShort(velocityX);
            packet.writeShort(velocityY);
            packet.writeShort(fhId);
            packet.write(newState);
            packet.writeShort(duration);
        }

        @Override
        public void decode(InboundPacket packet) {
            xPos = packet.readShort();
            yPos = packet.readShort();
            velocityX = packet.readShort();
            velocityY = packet.readShort();
            fhId = packet.readShort();
            newState = packet.readByte();
            duration = packet.readShort();
        }
    }

}
