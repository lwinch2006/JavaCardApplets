package dka.javacard.helloworld;

import javacard.framework.*;
import javacardx.apdu.ExtendedLength;

public class HelloWorld extends Applet implements ExtendedLength {
    private final UserService _userService;
    private final SampleService _sampleService;

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new HelloWorld();
    }

    protected HelloWorld() {
        _userService = new UserService();
        _sampleService = new SampleService();
        register();
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        byte CLA = buffer[ISO7816.OFFSET_CLA];
        byte INS = buffer[ISO7816.OFFSET_INS];

        if (CLA != ApduContants.HW_CLA) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
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

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
