package dka.javacard.helloworld;

import javacard.framework.*;
import javacardx.apdu.ExtendedLength;

public class HelloWorld extends Applet implements ExtendedLength {
    private final UserService _userService;
    private final SampleService _sampleService;

    private static final short MAX_BUFFER_SIZE = 2048;
    private final byte[] _buffer;
    private short _bufferLength = Constants.S_0;
    private short _bufferOffset = Constants.S_0;
    private byte _bufferINS = Constants.B_FF;
    private short _bufferP1P2 = Constants.S_FFFF;

    private final byte[] _shortValueAsBytes = new byte[2];

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new HelloWorld().register();
    }

    protected HelloWorld() {
        _userService = new UserService();
        _sampleService = new SampleService();

        _buffer = JCSystem.makeTransientByteArray(MAX_BUFFER_SIZE, JCSystem.CLEAR_ON_DESELECT);
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        byte CLA = buffer[ISO7816.OFFSET_CLA];
        byte INS = buffer[ISO7816.OFFSET_INS];

        if (CLA != ApduContants.HW_CLA && CLA != ApduContants.HW_CHAIN_CLA) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }

        if ((INS >> 4 & Constants.B_0F) == 0x03) {
            doCommandChaining(apdu);
        }

        switch (INS) {
            case ApduContants.HW_GET_FIRSTNAME_INS:
                _userService.getFirstName(apdu);
                return;

            case ApduContants.HW_GET_LASTNAME_INS:
                _userService.getLastName(apdu);
                return;

            case ApduContants.HW_GET_EMAIL_INS:
                _userService.getEmail(apdu);
                return;

            case ApduContants.HW_GET_PHONE_INS:
                _userService.getPhone(apdu);
                return;

            case ApduContants.HW_GET_PHOTO_INS:
                _userService.getPhoto(apdu);
                return;

            case ApduContants.HW_GET_RESPONSE_INS:
                ApduUtils.getResponse(apdu);
                return;

            case ApduContants.HW_GET_HELLOWORLD_INS:
                _sampleService.getHelloWorld(apdu);
                return;

            case ApduContants.HW_GET_APDU_BUFFER_LENGTH_INS:
                _sampleService.getAPDUBufferLength(apdu);
                return;

            case ApduContants.HW_GET_INBLOCKSIZE_INS:
                _sampleService.getInBlockSize(apdu);
                return;

            case ApduContants.HW_GET_OUTBLOCKSIZE_INS:
                _sampleService.getOutBlockSize(apdu);
                return;

            case ApduContants.HW_GET_BUFFER_BYTES_SUM:
                getBufferBytesSum(apdu);
                return;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    private void getBufferBytesSum(APDU apdu) {
        short sum = 0;

        for (short index = 0; index <= _bufferOffset; index++) {
            sum += _buffer[index];
        }

        Util.setShort(_shortValueAsBytes, Constants.S_0, sum);
        ApduUtils.sendDataToCAD(apdu, _shortValueAsBytes, (short) 2);
        ResetBuffer();
    }

    private void doCommandChaining(APDU apdu) throws ISOException {
        byte[] buffer = apdu.getBuffer();
        byte CLA = buffer[ISO7816.OFFSET_CLA];
        byte INS = buffer[ISO7816.OFFSET_INS];
        short P1P2 = Util.makeShort(buffer[ISO7816.OFFSET_P1], buffer[ISO7816.OFFSET_P2]);
        short offsetCData = apdu.getOffsetCdata();
        short readCount = apdu.setIncomingAndReceive();
        short lc = apdu.getIncomingLength();

        while (lc > 0) {
            if ((short)(_bufferOffset + readCount) > MAX_BUFFER_SIZE) {
                ResetBuffer();
                ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
            }

            Util.arrayCopyNonAtomic(buffer, offsetCData, _buffer, _bufferOffset, readCount);
            _bufferOffset += readCount;
            //_bufferLength = _bufferOffset;

            lc -= readCount;
            readCount = apdu.receiveBytes(offsetCData);
        }

        if (_bufferINS == Constants.B_FF) {
            _bufferINS = INS;
        }

        if (_bufferP1P2 == Constants.S_FFFF) {
            _bufferP1P2 = P1P2;
        }

        if (_bufferINS != INS || _bufferP1P2 != P1P2) {
            ResetBuffer();
            ISOException.throwIt(ISO7816.SW_LAST_COMMAND_EXPECTED);
        }

        if (CLA == ApduContants.HW_CHAIN_CLA) {
            ISOException.throwIt(ISO7816.SW_NO_ERROR);
        }
    }

    private void ResetBuffer() {
        //_bufferLength = Constants.S_0;
        _bufferOffset = Constants.S_0;
        _bufferINS = Constants.B_FF;
        _bufferP1P2 = Constants.S_FFFF;

        Util.arrayFillNonAtomic(_buffer, Constants.S_0, MAX_BUFFER_SIZE, Constants.B_0);
    }
}
