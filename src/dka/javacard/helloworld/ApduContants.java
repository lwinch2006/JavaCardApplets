package dka.javacard.helloworld;

public final class ApduContants {
    private ApduContants() {}

    public static final short MAX_APDU_DATA_LENGTH = 256;
    public static final short MAX_TEMP_BUFFER_LENGTH = 2048;

    // CLA
    public static final byte HW_CLA = (byte) 0x80;
    public static final byte HW_CHAIN_CLA = (byte) 0x84;


    // INS
    // Case 2
    public static final byte HW_GET_HELLOWORLD_INS = (byte) 0x01;
    public static final byte HW_GET_FIRSTNAME_INS = (byte) 0x02;
    public static final byte HW_GET_LASTNAME_INS = (byte) 0x03;
    public static final byte HW_GET_EMAIL_INS = (byte) 0x04;
    public static final byte HW_GET_PHONE_INS = (byte) 0x05;
    public static final byte HW_GET_PHOTO_INS = (byte) 0x06;

    // Case 3
    public static final byte HW_GET_RESPONSE_INS = (byte) 0xC0;

    // Sample INS
    // Case 2
    public static final byte HW_GET_APDU_BUFFER_LENGTH_INS = (byte) 0x07;
    public static final byte HW_GET_INBLOCKSIZE_INS = (byte) 0x08;
    public static final byte HW_GET_OUTBLOCKSIZE_INS = (byte) 0x09;

    // Case 4
    public static final byte HW_GET_BUFFER_BYTES_SUM = (byte) 0x4A;
}
