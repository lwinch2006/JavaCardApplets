package dka.javacard.helloworld;

import javacard.framework.APDU;
import javacard.framework.Util;

public final class SampleService {

    private final byte[] _shortValueAsBytes = new byte[2];

    public SampleService() {}

    public void getBufferBytesSum(APDU apdu, SharedCommandChainingService commandChainingService) {
        short sum = 0;

        byte[] buffer = commandChainingService.getBuffer();

        for (short index = 0; index <= commandChainingService.getBufferOffset(); index++) {
            sum += buffer[index];
        }

        Util.setShort(_shortValueAsBytes, SharedConstants.S_0, sum);
        SharedApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
        commandChainingService.resetBuffer();
    }
}
