package dka.javacard.helloworld;

public final class ApduConstants {
    private ApduConstants() {}

    public static final short MAX_LE_VALUE = 255;
    public static final short MAX_COMMAND_CHAINING_BUFFER_SIZE = 2048;

    // INS
    // Case 2
    public static final byte HW_GET_HELLOWORLD_INS = (byte) 0x01;
    public static final byte HW_GET_FIRSTNAME_INS = (byte) 0x02;
    public static final byte HW_GET_LASTNAME_INS = (byte) 0x03;
    public static final byte HW_GET_EMAIL_INS = (byte) 0x04;
    public static final byte HW_GET_PHONE_INS = (byte) 0x05;
    public static final byte HW_GET_PHOTO_INS = (byte) 0x06;
    public static final byte HW_GET_RESPONSE_INS = (byte) 0xC0;

    // Case 4
    public static final byte HW_GET_BUFFER_BYTES_SUM_INS = (byte) 0x30;
}
