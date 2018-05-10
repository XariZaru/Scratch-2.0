package src.net;

public enum ClientType {

    MASTER(0),
    LOGIN(1),
    GAME_SERVER(2),
    CHANNEL(3),
    PLAYER(4);

    private int value;

    private ClientType(int value) {
        this.value = value;

    }

    public int getValue() {
        return value;
    }

}
