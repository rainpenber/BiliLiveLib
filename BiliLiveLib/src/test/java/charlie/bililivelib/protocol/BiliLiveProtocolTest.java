package charlie.bililivelib.protocol;

import charlie.bililivelib.BiliLiveException;
import charlie.bililivelib.Globals;
import charlie.bililivelib.datamodel.Room;
import charlie.bililivelib.i18n.I18n;
import charlie.bililivelib.net.datamodel.LiveAddresses;
import charlie.bililivelib.util.LogUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.Level;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class BiliLiveProtocolTest {
    private BiliLiveProtocol protocol;

    @BeforeClass
    public static void init() {
        I18n.init();
        LogUtil.init();
    }

    @org.junit.Before
    public void setUp() throws Exception {
        protocol = new BiliLiveProtocol();
    }

    @org.junit.Test
    public void getRealRoomID() throws Exception {
        int roomID = protocol.getRealRoomID(148);
        assertEquals(10313, roomID);
    }

    @org.junit.Test
    public void getRealRoomIDInvalid() throws Exception {
        try {
            protocol.getRealRoomID(1487152472);
        } catch (BiliLiveException ex){
            LogUtil.logException(Level.ERROR, "Error getting room id!", ex);
        }
    }

    @org.junit.Test
    public void getRealRoomIDNetworkError() throws Exception {
        try {
            HttpClient client = createInvalidHttpClient();
            HttpClient origin = forceReplaceAndReturnHttpClient(client);
            protocol.getRealRoomID(148);
            forceReplaceAndReturnHttpClient(client);
        } catch (BiliLiveException ex){
            LogUtil.logException(Level.ERROR, "Error getting room id!", ex);
        }
    }

    private HttpClient forceReplaceAndReturnHttpClient(HttpClient httpClient) {
        try {
            Field clientField = Globals.get()
                    .getHttpHelper().getClass().getDeclaredField("httpClient");
            clientField.setAccessible(true);
            HttpClient original = (HttpClient) clientField.get(Globals.get().getHttpHelper());
            clientField.set(Globals.get().getHttpHelper(), httpClient);
            return original;
        } catch (Exception e) {
            LogUtil.logException(Level.ERROR, "Error replacing Http Client!", e);
        }
        return null;
    }

    private HttpClient createInvalidHttpClient() {
        return HttpClientBuilder.create().setProxy(new HttpHost("127.0.0.1", 33112)).build();
    }

    @Test
    public void getLiveAddresses() throws Exception {
        System.out.println(protocol.getLiveAddresses(294140));
    }

    @org.junit.Test
    public void getLiveAddressesInvalid() throws Exception {
        try {
            LiveAddresses addresses = protocol.getLiveAddresses(1744048751);
            assertTrue(addresses.getLineMain().isEmpty());
        } catch (BiliLiveException ex){
            LogUtil.logException(Level.ERROR, "Failed getting live address!", ex);
        }
    }

    @org.junit.Test
    public void getRoomInfo() throws Exception {
        try {
            Room room = new Room();
            room.setRoomID(459985);
            protocol.fillRoomInfo(room);
            assertEquals("山新直播间", room.getRoomTitle());
            System.out.println(room);
        } catch (BiliLiveException ex){
            LogUtil.logException(Level.ERROR, "Error getting room id!", ex);
        }
    }
}