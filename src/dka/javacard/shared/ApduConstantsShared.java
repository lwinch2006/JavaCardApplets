package dka.javacard.shared;

public final class ApduConstantsShared {
    // CLA
    public static final byte HW_CLA = (byte) 0x80;
    public static final byte HW_CHAIN_CLA = (byte) 0x84;

    // Sample INS
    // Case 2
    public static final byte HW_GET_APDU_BUFFER_LENGTH_INS = (byte) 0x07;
    public static final byte HW_GET_INBLOCKSIZE_INS = (byte) 0x08;
    public static final byte HW_GET_OUTBLOCKSIZE_INS = (byte) 0x09;
    public static final byte HW_GET_AVAILABLEPERMANENTMEMORY_INS = (byte) 0x0A;
    public static final byte HW_GET_AVAILABLETRANSIENTMEMORY_ONRESET_INS = (byte) 0x0B;
    public static final byte HW_GET_AVAILABLETRANSIENTMEMORY_ONDESELECT_INS = (byte) 0x0C;
    public static final byte HW_GET_JCAPIVERSION_INS = (byte) 0x0D;
}
