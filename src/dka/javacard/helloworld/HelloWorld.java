package dka.javacard.helloworld;

import javacard.framework.*;
import javacardx.apdu.ExtendedLength;

public class HelloWorld extends Applet implements ExtendedLength {
    public static final byte HW_CLA = (byte) 0x80;

    public static final byte HW_GET_HELLOWORLD_INS = (byte) 0x01;
    public static final byte HW_GET_FIRSTNAME_INS = (byte) 0x02;
    public static final byte HW_GET_LASTNAME_INS = (byte) 0x03;
    public static final byte HW_GET_EMAIL_INS = (byte) 0x04;
    public static final byte HW_GET_PHONE_INS = (byte) 0x05;
    public static final byte HW_GET_PHOTO_INS = (byte) 0x06;

    public static final byte HW_GET_RESPONSE_INS = (byte) 0xC0;

    public static byte FIRSTNAME_MAX_LENGTH = (byte) 25;
    public static byte LASTNAME_MAX_LENGTH = (byte) 25;
    public static byte EMAIL_MAX_LENGTH = (byte) 50;
    public static byte PHONE_MAX_LENGTH = (byte) 25;
    public static short PHOTO_MAX_LENGTH = (short) 1221; //16384; // 128x128

    public static final byte[] _response = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'D', 'm', 'i', 't', 'r', 'y'};
    public byte[] _firstName = new byte[FIRSTNAME_MAX_LENGTH];
    public byte[] _surName = new byte[LASTNAME_MAX_LENGTH];
    public byte[] _email = new byte[EMAIL_MAX_LENGTH];
    public byte[] _phone = new byte[PHONE_MAX_LENGTH];

    public short _photo_bytes_left = 0;
    public short _photo_offset = 0;
    public byte[] _photo = new byte[PHOTO_MAX_LENGTH];
    public byte[] _shortValueAsBytes = new byte[2];


    // Test data
    public static final byte HW_GET_APDU_BUFFER_LENGTH_INS = (byte) 0x07;
    public static final byte HW_GET_INBLOCKSIZE_INS = (byte) 0x08;
    public static final byte HW_GET_OUTBLOCKSIZE_INS = (byte) 0x09;

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
        byte CLA = buffer[ISO7816.OFFSET_CLA];
        byte INS = buffer[ISO7816.OFFSET_INS];

        if (CLA != HW_CLA) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }

        switch (INS) {
            case HW_GET_FIRSTNAME_INS:
                getFirstName(apdu);
                return;

            case HW_GET_LASTNAME_INS:
                getLastName(apdu);
                return;

            case HW_GET_EMAIL_INS:
                getEmail(apdu);
                return;

            case HW_GET_PHONE_INS:
                getPhone(apdu);
                return;

            case HW_GET_PHOTO_INS:
                getPhoto(apdu);
                return;

            case HW_GET_RESPONSE_INS:
                getPhotoContinued(apdu);
                return;

            case HW_GET_HELLOWORLD_INS:
                getResponse(apdu);
                return;

            case HW_GET_APDU_BUFFER_LENGTH_INS:
                getAPDUBufferLength(apdu);
                return;

            case HW_GET_INBLOCKSIZE_INS:
                getInBlockSize(apdu);
                return;

            case HW_GET_OUTBLOCKSIZE_INS:
                getOutBlockSize(apdu);
                return;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    private void getFirstName(APDU apdu) {
        sendDataToCAD(apdu, _firstName, FIRSTNAME_MAX_LENGTH);
    }

    private void getLastName(APDU apdu) {
        sendDataToCAD(apdu, _surName, LASTNAME_MAX_LENGTH);
    }

    private void getEmail(APDU apdu) {
        sendDataToCAD(apdu, _email, EMAIL_MAX_LENGTH);
    }

    private void getPhone(APDU apdu) {
        sendDataToCAD(apdu, _phone, PHONE_MAX_LENGTH);
    }

    private void getPhoto(APDU apdu) {
        _photo_bytes_left = PHOTO_MAX_LENGTH;
        _photo_offset = 0;

        if (PHOTO_MAX_LENGTH > 255) {
            ISOException.throwIt((short)(ISO7816.SW_BYTES_REMAINING_00 | 0xFF));
        }

        ISOException.throwIt((short)(ISO7816.SW_BYTES_REMAINING_00 | PHOTO_MAX_LENGTH));
    }

    private void getPhotoContinued(APDU apdu) {
        short le = apdu.setOutgoing();
        short dataLength = _photo_bytes_left > le ? le : _photo_bytes_left;

        apdu.setOutgoingLength(dataLength);
        apdu.sendBytesLong(_photo, _photo_offset, dataLength);

        _photo_bytes_left -= dataLength;
        _photo_offset += dataLength;

        if (_photo_bytes_left == 0) {
            _photo_offset = 0;
            return;
        }

        if (_photo_bytes_left > 255) {
            ISOException.throwIt((short)(ISO7816.SW_BYTES_REMAINING_00 | 0xFF));
        }

        ISOException.throwIt((short)(ISO7816.SW_BYTES_REMAINING_00 | _photo_bytes_left));
    }


    private void getResponse(APDU apdu) {
        sendDataToCAD(apdu, _response, (short) _response.length);
    }

    private void getAPDUBufferLength(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short bufferLength = (short) buffer.length;
        Util.setShort(_shortValueAsBytes, (short) 0, bufferLength);
        sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
    }

    private void getInBlockSize(APDU apdu) {
        short inBlockSize = APDU.getInBlockSize();
        Util.setShort(_shortValueAsBytes, (short) 0, inBlockSize);
        sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
    }

    private void getOutBlockSize(APDU apdu) {
        short outBlockSize = APDU.getOutBlockSize();
        Util.setShort(_shortValueAsBytes, (short) 0, outBlockSize);
        sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
    }

    private void sendDataToCAD(APDU apdu, byte[] data, short dataLength) {
        byte[] buffer = apdu.getBuffer();
        short le = apdu.setOutgoing();

        if (le < dataLength) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        apdu.setOutgoingLength(dataLength);
        Util.arrayCopyNonAtomic(data, (short) 0, buffer, (short) 0, dataLength);
        apdu.sendBytes((short) 0, dataLength);
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

        Util.arrayFillNonAtomic(_photo, (short) 0, (short)256, (byte) 7);
        Util.arrayFillNonAtomic(_photo, (short) 256, (short)1, (byte) 2);
        Util.arrayFillNonAtomic(_photo, (short) 257, (short)(PHOTO_MAX_LENGTH - 257), (byte) 3);
    }
}
