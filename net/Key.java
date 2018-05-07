package net;

import io.netty.util.AttributeKey;
import net.components.AESOFB;
import net.components.Client;
import net.components.Pipeline;

public class Key {

    public static final AttributeKey<ClientType> TYPE = AttributeKey.valueOf("TYPE");
    public static final AttributeKey<Integer> PLAYER = AttributeKey.valueOf("PLAYER");
    public static final AttributeKey<Integer> SERVER_SELECT = AttributeKey.valueOf("SERVER_SELECT");
    public static final AttributeKey<Integer> ENTITY = AttributeKey.valueOf("ENTITY");
    public static final AttributeKey<Byte> WORLD = AttributeKey.valueOf("WORLD");
    public static final AttributeKey<Integer> CONNECTED_AMOUNT = AttributeKey.valueOf("CONNECTED_AMOUNT");

    public static final AttributeKey<AESOFB> AESOFB = AttributeKey.valueOf("AESOFB");
    public static final AttributeKey<Pipeline> PIPELINE = AttributeKey.valueOf("PIPELINE");
    public static final AttributeKey<Client> CLIENT = AttributeKey.valueOf("CLIENT");
}
