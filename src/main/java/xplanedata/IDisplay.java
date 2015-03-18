package xplanedata;

/**
 *
 * @author fred
 */
public interface IDisplay {
    public void incrementPacketsReceived();
    public void incrementUnknownPackets();
    public void addPacketType(int type);
    public void addUnknownPacketType(int type);
    
}
