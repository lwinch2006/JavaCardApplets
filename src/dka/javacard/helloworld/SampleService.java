package dka.javacard.helloworld;

import javacard.framework.APDU;
import javacard.framework.Util;

public final class SampleService {
    private static final byte[] _response = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'D', 'm', 'i', 't', 'r', 'y'};

    private final byte[] _shortValueAsBytes = new byte[2];

    public SampleService() {}

    public void getHelloWorld(APDU apdu) {
        ApduUtils.sendDataToCAD(apdu, _response, (short) _response.length);
    }

    public void getAPDUBufferLength(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short bufferLength = (short) buffer.length;
        Util.setShort(_shortValueAsBytes, (short) 0, bufferLength);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
    }

    public void getInBlockSize(APDU apdu) {
        short inBlockSize = APDU.getInBlockSize();
        Util.setShort(_shortValueAsBytes, (short) 0, inBlockSize);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
    }

    public void getOutBlockSize(APDU apdu) {
        short outBlockSize = APDU.getOutBlockSize();
        Util.setShort(_shortValueAsBytes, (short) 0, outBlockSize);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
    }
}
