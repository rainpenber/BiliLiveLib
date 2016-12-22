package charlie.bililivelib.danmaku;

import charlie.bililivelib.BiliLiveLib;
import charlie.bililivelib.danmaku.dispatch.*;
import charlie.bililivelib.danmaku.event.DanmakuAdapter;
import charlie.bililivelib.danmaku.event.DanmakuEvent;
import charlie.bililivelib.datamodel.Room;
import charlie.bililivelib.i18n.I18n;
import charlie.bililivelib.util.LogUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class DanmakuReceiverTest {
    @BeforeClass
    public static void init() {
        I18n.init();
        LogUtil.init();
    }

    public static final int SIXTEEN_ROOMID = 10313;
    public static final int CHARLIEJIANG_ROOMID = 39249;
    public static final int MARCOV_ROOMID = 17029;

    @Test
    public void test() throws Exception {
        Room room = new Room();
        room.setRoomID(CHARLIEJIANG_ROOMID);
        DanmakuReceiver receiver = new DanmakuReceiver(room);
        //receiver.getDispatchManager().registerDispatcher(new DanmakuDispatcher());
        //receiver.getDispatchManager().registerDispatcher(new StartStopDispatcher());
        //receiver.getDispatchManager().registerDispatcher(new WelcomeVipDispatcher());
        //receiver.getDispatchManager().registerDispatcher(new GiveGiftDispatcher());
        receiver.getDispatchManager().registerDispatcher(new GlobalGiftDispatcher());
        receiver.addDanmakuListener(new TestListener());
        receiver.connect();
        synchronized (this) {
            wait();
        }
    }

    private class TestListener extends DanmakuAdapter {
        private Logger logger = LogManager.getLogger(BiliLiveLib.class);

        @Override
        public void welcomeVipEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "Welcome VIP:" + event.getParam());
        }

        @Override
        public void danmakuEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "New Danmaku:" + event.getParam());
        }

        @Override
        public void watcherCountEvent(DanmakuEvent event) {
            //logger.log(Level.INFO, "WatcherCount:" + event.getParam());
        }

        @Override
        public void startStopEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "StartStop:" + event.getParam());
        }

        @Override
        public void errorEvent(DanmakuEvent event) {
            logger.log(Level.ERROR, event.getParam());
        }

        @Override
        public void statusEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "STATUS:" + event.getParam());
        }

        @Override
        public void giveGiftEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "Give gift:" + event.getParam());
        }
    }
}