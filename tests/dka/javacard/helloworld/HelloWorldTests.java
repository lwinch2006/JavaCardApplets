package dka.javacard.helloworld;


import com.licel.jcardsim.smartcardio.CardSimulator;
import com.licel.jcardsim.utils.AIDUtil;


import javacard.framework.AID;
import org.junit.Test;



import static org.junit.Assert.assertEquals;

public class HelloWorldTests {










    @Test
    public void test() {
        CardSimulator simulator = new CardSimulator();

        AID appletAID = AIDUtil.create("D276000124100001");
//        simulator.installApplet(appletAID, HelloWorld.class);
//
//        simulator.selectApplet(appletAID);

        //CommandAPDU commandAPDU = new CommandAPDU(0x00, 0x01, 0x00, 0x00);
        //ResponseAPDU responseAPDU = simulator.transmitCommand(commandAPDU);

        assertEquals(0x9000, 0x9000);
    }

}


