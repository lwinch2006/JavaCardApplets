package dka.javacard.helloworld;

import javacard.framework.*;

public class HelloWorld extends Applet {

    public static final byte HW_CLA = (byte)0x80;

    public static final byte HW_GET_RESPONSE_INS = (byte)0x01;

    public static final byte[] _response = { 'H', 'e', 'l', 'l', 'o', ',', ' ', 'D', 'm', 'i', 't', 'r', 'y' };

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new HelloWorld();
    }

    protected HelloWorld() {
        register();
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();

        if (buffer[ISO7816.OFFSET_CLA] != HW_CLA) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }

        switch (buffer[ISO7816.OFFSET_INS]) {
            case HW_GET_RESPONSE_INS: getResponse(apdu);
                return;

            default: ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    private void getResponse(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short le = apdu.setOutgoing();
        short length = (short)_response.length;

        if (le < length) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        apdu.setOutgoingLength(length);
        Util.arrayCopyNonAtomic(_response, (short)0, buffer, (short)0, length);
        apdu.sendBytes((short)0, (short)_response.length);
    }
}
