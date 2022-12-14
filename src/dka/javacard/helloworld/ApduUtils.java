package dka.javacard.helloworld;

import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;

public final class ApduUtils {
    private ApduUtils() {}

    public static byte[] _dataSource = null;
    public static short _bytesLeft = 0;
    public static short _bytesOffset = 0;

    public static void sendDataToCAD(APDU apdu, byte[] data, short dataLength) throws ISOException {
        byte[] buffer = apdu.getBuffer();
        short le = apdu.setOutgoing();

        if (le < dataLength) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        apdu.setOutgoingLength(dataLength);
        Util.arrayCopyNonAtomic(data, Constants.S_0, buffer, Constants.S_0, dataLength);
        apdu.sendBytes(Constants.S_0, dataLength);
    }

    public static void getResponse(APDU apdu) throws ISOException {
        short le = apdu.setOutgoing();
        short dataLength = _bytesLeft > le ? le : _bytesLeft;

        apdu.setOutgoingLength(dataLength);
        apdu.sendBytesLong(_dataSource, _bytesOffset, dataLength);

        _bytesLeft -= dataLength;
        _bytesOffset += dataLength;

        if (_bytesLeft == Constants.S_0) {
            _bytesOffset = Constants.S_0;
            return;
        }

        if (_bytesLeft > ApduContants.MAX_LE_VALUE) {
            ISOException.throwIt((short)(ISO7816.SW_BYTES_REMAINING_00 | Constants.S_FF));
        }

        ISOException.throwIt((short)(ISO7816.SW_BYTES_REMAINING_00 | _bytesLeft));
    }
}
