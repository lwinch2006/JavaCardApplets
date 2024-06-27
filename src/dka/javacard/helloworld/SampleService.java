package dka.javacard.helloworld;

import dka.javacard.shared.ApduUtils;
import dka.javacard.shared.CommandChainingService;
import dka.javacard.shared.Constants;
import javacard.framework.APDU;
import javacard.framework.Util;

public final class SampleService {

    private final byte[] _shortValueAsBytes = new byte[2];

    public SampleService() {}

    public void getBufferBytesSum(APDU apdu, CommandChainingService commandChainingService) {
        short sum = 0;

        byte[] buffer = commandChainingService.getBuffer();

        for (short index = 0; index <= commandChainingService.getBufferOffset(); index++) {
            sum += buffer[index];
        }

        Util.setShort(_shortValueAsBytes, Constants.S_0, sum);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
        commandChainingService.resetBuffer();
    }
}
