package dka.javacard.helloworld;

import javacard.framework.*;

public class HelloWorld extends Applet {
    public static final byte HW_CLA = (byte) 0x80;

    public static final byte HW_GET_RESPONSE_INS = (byte) 0x01;
    public static final byte HW_GET_FIRSTNAME_INS = (byte) 0x02;
    public static final byte HW_GET_SURNAME_INS = (byte) 0x03;
    public static final byte HW_GET_EMAIL_INS = (byte) 0x04;
    public static final byte HW_GET_PHONE_INS = (byte) 0x05;
    public static final byte HW_GET_PHOTO_INS = (byte) 0x06;

    public static byte FIRSTNAME_MAX_LENGTH = (byte) 25;
    public static byte SURNAME_MAX_LENGTH = (byte) 25;
    public static byte EMAIL_MAX_LENGTH = (byte) 50;
    public static byte PHONE_MAX_LENGTH = (byte) 25;
    public static short PHOTO_MAX_LENGTH = (short) 16384; // 128x128

    public static final byte[] _response = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'D', 'm', 'i', 't', 'r', 'y'};
    public byte[] _firstName = new byte[FIRSTNAME_MAX_LENGTH];
    public byte[] _surName = new byte[SURNAME_MAX_LENGTH];
    public byte[] _email = new byte[EMAIL_MAX_LENGTH];
    public byte[] _phone = new byte[PHONE_MAX_LENGTH];
    public byte[] _photo = new byte[PHOTO_MAX_LENGTH];

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new HelloWorld();
    }

    protected HelloWorld() {
        seedTestData();
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
            case HW_GET_FIRSTNAME_INS:
                getFirstName(apdu);
                return;

            case HW_GET_SURNAME_INS:
                getSurName(apdu);
                return;

            case HW_GET_EMAIL_INS:
                getEmail(apdu);
                return;

            case HW_GET_PHONE_INS:
                getPhone(apdu);
                return;

            case HW_GET_RESPONSE_INS:
                getResponse(apdu);
                return;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    private void getFirstName(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short le = apdu.setOutgoing();

        if (le < FIRSTNAME_MAX_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        apdu.setOutgoingLength(FIRSTNAME_MAX_LENGTH);
        Util.arrayCopyNonAtomic(_firstName, (short) 0, buffer, (short) 0, FIRSTNAME_MAX_LENGTH);
        apdu.sendBytes((short) 0, FIRSTNAME_MAX_LENGTH);
    }

    private void getSurName(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short le = apdu.setOutgoing();

        if (le < SURNAME_MAX_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        apdu.setOutgoingLength(SURNAME_MAX_LENGTH);
        Util.arrayCopyNonAtomic(_surName, (short) 0, buffer, (short) 0, SURNAME_MAX_LENGTH);
        apdu.sendBytes((short) 0, SURNAME_MAX_LENGTH);
    }

    private void getEmail(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short le = apdu.setOutgoing();

        if (le < EMAIL_MAX_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        apdu.setOutgoingLength(EMAIL_MAX_LENGTH);
        Util.arrayCopyNonAtomic(_email, (short) 0, buffer, (short) 0, EMAIL_MAX_LENGTH);
        apdu.sendBytes((short) 0, EMAIL_MAX_LENGTH);
    }

    private void getPhone(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short le = apdu.setOutgoing();

        if (le < PHONE_MAX_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        apdu.setOutgoingLength(PHONE_MAX_LENGTH);
        Util.arrayCopyNonAtomic(_phone, (short) 0, buffer, (short) 0, PHONE_MAX_LENGTH);
        apdu.sendBytes((short) 0, PHONE_MAX_LENGTH);
    }

    private void getPhoto(APDU apdu) {
        ISOException.throwIt(ISO7816.SW_FUNC_NOT_SUPPORTED);
    }

    private void getResponse(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short le = apdu.setOutgoing();
        short length = (short) _response.length;

        if (le < length) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        apdu.setOutgoingLength(length);
        Util.arrayCopyNonAtomic(_response, (short) 0, buffer, (short) 0, length);
        apdu.sendBytes((short) 0, (short) _response.length);
    }

    private void seedTestData() {
        byte[] firstName = {'D', 'm', 'i', 't', 'r', 'y'};
        byte[] surName = {'K', 'a', 'l', 'i', 'n', 'i', 'n'};
        byte[] email = {'D', 'm', 'i', 't', 'r', 'y', 'K', 'a', 'l', 'i', 'n', 'i', 'n', '@', 'i', 'c', 'l', 'o', 'u', 'd', '.', 'c', 'o', 'm'};
        byte[] phone = {'+', '4', '7', '4', '1', '5', '1', '0', '5', '7', '9'};

        Util.arrayCopyNonAtomic(firstName, (short) 0, _firstName, (short) 0, (short) firstName.length);
        Util.arrayCopyNonAtomic(surName, (short) 0, _surName, (short) 0, (short) surName.length);
        Util.arrayCopyNonAtomic(email, (short) 0, _email, (short) 0, (short) email.length);
        Util.arrayCopyNonAtomic(phone, (short) 0, _phone, (short) 0, (short) phone.length);
    }
}
