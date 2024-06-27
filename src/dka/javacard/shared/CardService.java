package dka.javacard.shared;

import javacard.framework.APDU;
import javacard.framework.JCSystem;
import javacard.framework.Util;

public final class CardService {
    private static final byte[] _response = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'D', 'm', 'i', 't', 'r', 'y'};

    private final byte[] _shortValueAsBytes = new byte[2];

    public CardService() {}

    public void getHelloWorld(APDU apdu) {
        ApduUtils.sendDataToCAD(apdu, _response, (short) _response.length);
    }

    public void getAPDUBufferLength(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short bufferLength = (short) buffer.length;
        Util.setShort(_shortValueAsBytes, Constants.S_0, bufferLength);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) _shortValueAsBytes.length);
    }

    public void getInBlockSize(APDU apdu) {
        short inBlockSize = APDU.getInBlockSize();
        Util.setShort(_shortValueAsBytes, Constants.S_0, inBlockSize);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) _shortValueAsBytes.length);
    }

    public void getOutBlockSize(APDU apdu) {
        short outBlockSize = APDU.getOutBlockSize();
        Util.setShort(_shortValueAsBytes, Constants.S_0, outBlockSize);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) _shortValueAsBytes.length);
    }

    public void getJcApiVersion(APDU apdu) {
        short version = JCSystem.getVersion();
        Util.setShort(_shortValueAsBytes, Constants.S_0, version);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) _shortValueAsBytes.length);
    }

    public void getAvailablePermanentMemory(APDU apdu) {
        short[] shortValues = new short[2];
        JCSystem.getAvailableMemory(shortValues, Constants.S_0, JCSystem.MEMORY_TYPE_PERSISTENT);

        byte[] data = new byte[4];
        Util.setShort(data, Constants.S_0, shortValues[0]);
        Util.setShort(data, (short) 2, shortValues[1]);

        ApduUtils.sendDataToCAD(apdu, data, (short) data.length);
    }

    public void getAvailableTransientMemoryOnReset(APDU apdu) {
        short[] shortValues = new short[2];
        JCSystem.getAvailableMemory(shortValues, Constants.S_0, JCSystem.MEMORY_TYPE_TRANSIENT_RESET);

        byte[] data = new byte[4];
        Util.setShort(data, Constants.S_0, shortValues[0]);
        Util.setShort(data, (short) 2, shortValues[1]);

        ApduUtils.sendDataToCAD(apdu, data, (short) data.length);
    }

    public void getAvailableTransientMemoryOnDeselect(APDU apdu) {
        short[] shortValues = new short[2];
        JCSystem.getAvailableMemory(shortValues, Constants.S_0, JCSystem.MEMORY_TYPE_TRANSIENT_DESELECT);

        byte[] data = new byte[4];
        Util.setShort(data, Constants.S_0, shortValues[0]);
        Util.setShort(data, (short) 2, shortValues[1]);

        ApduUtils.sendDataToCAD(apdu, data, (short) data.length);
    }
}
