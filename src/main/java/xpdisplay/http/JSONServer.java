
package xpdisplay.http;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import xplanedata.IDisplay;
import xpdisplay.io.DataListener;
import xpdisplay.io.DataProvider;
import xpdisplay.io.DataRecorder;
import xpdisplay.io.PacketParser;
import xpdisplay.io.impl.PacketParser10xx;
import xpdisplay.model.SimState;


/**
 *
 * @author fred
 */
public class JSONServer implements DataListener, IDisplay{
    
    private PacketParser packetParser;
    private SimState simState = new SimState();
    private DataProvider dataProvider;
    private Vector<DataRecorder> dataRecorders = new Vector();
    private int packetsReceived;
    private int unknownPackets;
    private Set<Integer> packetTypes = new TreeSet<Integer>();
    private Set<Integer> unknownPacketTypes = new TreeSet<Integer>();

    public JSONServer(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        dataProvider.addListener(this);
        packetParser = new PacketParser10xx(this);
        clearPacketStats();
    
    }
    
    public void registerDataRecorder(DataRecorder recorder) {
        dataRecorders.add(recorder);
    }
    
    public String getJSONData(){
        String message = "no data";
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            message = ow.writeValueAsString(simState);
        } catch (IOException ex) {
            Logger.getLogger(JSONServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }
    
    public void incrementPacketsReceived() {
        packetsReceived++;
        //System.out.println("XPDisplay.packetsReceived " + packetsReceived);
    }

    public void incrementUnknownPackets() {
        unknownPackets++;
        //System.out.println("XPDisplay.unknownPackets " + unknownPackets);
    }

    public void addPacketType(int type) {
        if( !packetTypes.contains(type) ) {
            packetTypes.add(type);
            //System.out.println("XPDisplay.packetTypes " + packetTypes.toString());
        }
    }

    public void addUnknownPacketType(int type) {
        if( !unknownPacketTypes.contains(type) ) {
            unknownPacketTypes.add(type);
            //System.out.println("XPDisplay.unknownPacketTypes " + unknownPacketTypes.toString());
        }
    }

    

    public void clearPacketStats() {
        packetsReceived = 0;
        unknownPackets = 0;
        unknownPacketTypes.clear();
        packetTypes.clear();
        //System.out.println("XPDisplay.packetsReceived " + packetsReceived);
        //System.out.println("XPDisplay.unknownPackets " + unknownPackets);
        //System.out.println("XPDisplay.unknownPacketTypes " + unknownPacketTypes.toString());
        //System.out.println("XPDisplay.packetTypes " + packetTypes.toString());
    }
    
    
    @Override
    public void notifyDataArrived(byte[] data) {
         packetParser.updateSimStateWithData(simState, data);
    }

    
    
    
}
