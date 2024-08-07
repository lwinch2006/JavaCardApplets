package dka.javacard.helloworld;

import javacard.framework.*;

public final class SharedCommandChainingService {
    private short _bufferOffset = SharedConstants.S_0;
    private byte _bufferINS = SharedConstants.B_FF;
    private short _bufferP1P2 = SharedConstants.S_FFFF;

    private final short _maxBufferSize;
    private final byte[] _buffer;

    public SharedCommandChainingService(short maxBufferSize) {
        _maxBufferSize = maxBufferSize;
        _buffer = JCSystem.makeTransientByteArray(_maxBufferSize, JCSystem.CLEAR_ON_DESELECT);
    }

    public short getBufferOffset() {
        return _bufferOffset;
    }

    public byte[] getBuffer() {
        return _buffer;
    }

    public void doCommandChaining(APDU apdu) throws ISOException {
        byte[] buffer = apdu.getBuffer();
        byte CLA = buffer[ISO7816.OFFSET_CLA];
        byte INS = buffer[ISO7816.OFFSET_INS];
        short P1P2 = Util.makeShort(buffer[ISO7816.OFFSET_P1], buffer[ISO7816.OFFSET_P2]);
        short offsetCData = apdu.getOffsetCdata();
        short readCount = apdu.setIncomingAndReceive();
        short lc = apdu.getIncomingLength();

        while (lc > 0) {
            if ((short)(_bufferOffset + readCount) > _maxBufferSize) {
                resetBuffer();
                ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
            }

            Util.arrayCopyNonAtomic(buffer, offsetCData, _buffer, _bufferOffset, readCount);
            _bufferOffset += readCount;

            lc -= readCount;
            readCount = apdu.receiveBytes(offsetCData);
        }

        if (_bufferINS == SharedConstants.B_FF) {
            _bufferINS = INS;
        }

        if (_bufferP1P2 == SharedConstants.S_FFFF) {
            _bufferP1P2 = P1P2;
        }

        if (_bufferINS != INS || _bufferP1P2 != P1P2) {
            resetBuffer();
            ISOException.throwIt(ISO7816.SW_LAST_COMMAND_EXPECTED);
        }

        if (CLA == SharedApduConstants.HW_CHAIN_CLA) {
            ISOException.throwIt(ISO7816.SW_NO_ERROR);
        }
    }

    public void resetBuffer() {
        _bufferOffset = SharedConstants.S_0;
        _bufferINS = SharedConstants.B_FF;
        _bufferP1P2 = SharedConstants.S_FFFF;

        Util.arrayFillNonAtomic(_buffer, SharedConstants.S_0, _maxBufferSize, SharedConstants.B_0);
    }
}
