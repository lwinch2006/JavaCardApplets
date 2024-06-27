package dka.javacard.helloworld;

import dka.javacard.shared.*;
import javacard.framework.*;
import javacardx.apdu.ExtendedLength;

import static dka.javacard.helloworld.ApduConstants.MAX_COMMAND_CHAINING_BUFFER_SIZE;

public class HelloWorld extends Applet implements ExtendedLength {
    private final UserService _userService;
    private final CardService _cardService;
    private final SampleService _sampleService;
    private final CommandChainingService _commandChainingService;

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new HelloWorld().register();
    }

    protected HelloWorld() {
        _userService = new UserService();
        _cardService = new CardService();
        _sampleService = new SampleService();
        _commandChainingService = new CommandChainingService(MAX_COMMAND_CHAINING_BUFFER_SIZE);
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        byte CLA = buffer[ISO7816.OFFSET_CLA];
        byte INS = buffer[ISO7816.OFFSET_INS];

        if (CLA != ApduConstantsShared.HW_CLA && CLA != ApduConstantsShared.HW_CHAIN_CLA) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }

        if (CLA == ApduConstantsShared.HW_CHAIN_CLA) {
            _commandChainingService.doCommandChaining(apdu);
        }

        switch (INS) {
            case ApduConstants.HW_GET_FIRSTNAME_INS:
                _userService.getFirstName(apdu);
                return;

            case ApduConstants.HW_GET_LASTNAME_INS:
                _userService.getLastName(apdu);
                return;

            case ApduConstants.HW_GET_EMAIL_INS:
                _userService.getEmail(apdu);
                return;

            case ApduConstants.HW_GET_PHONE_INS:
                _userService.getPhone(apdu);
                return;

            case ApduConstants.HW_GET_PHOTO_INS:
                _userService.getPhoto(apdu);
                return;

            case ApduConstants.HW_GET_RESPONSE_INS:
                ApduUtils.getResponse(apdu);
                return;

            case ApduConstants.HW_GET_HELLOWORLD_INS:
                _cardService.getHelloWorld(apdu);
                return;

            case ApduConstantsShared.HW_GET_APDU_BUFFER_LENGTH_INS:
                _cardService.getAPDUBufferLength(apdu);
                return;

            case ApduConstantsShared.HW_GET_INBLOCKSIZE_INS:
                _cardService.getInBlockSize(apdu);
                return;

            case ApduConstantsShared.HW_GET_OUTBLOCKSIZE_INS:
                _cardService.getOutBlockSize(apdu);
                return;

            case ApduConstantsShared.HW_GET_AVAILABLEPERMANENTMEMORY_INS:
                _cardService.getAvailablePermanentMemory(apdu);
                return;

            case ApduConstantsShared.HW_GET_AVAILABLETRANSIENTMEMORY_ONRESET_INS:
                _cardService.getAvailableTransientMemoryOnReset(apdu);
                return;

            case ApduConstantsShared.HW_GET_AVAILABLETRANSIENTMEMORY_ONDESELECT_INS:
                _cardService.getAvailableTransientMemoryOnDeselect(apdu);
                return;

            case ApduConstantsShared.HW_GET_JCAPIVERSION_INS:
                _cardService.getJcApiVersion(apdu);
                return;

            case ApduConstants.HW_GET_BUFFER_BYTES_SUM_INS:
                _sampleService.getBufferBytesSum(apdu, _commandChainingService);
                return;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
