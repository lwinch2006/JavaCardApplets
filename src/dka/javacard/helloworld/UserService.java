package dka.javacard.helloworld;

import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;

public final class UserService {
    private static final byte FIRSTNAME_MAX_LENGTH = (byte) 25;
    private static final byte LASTNAME_MAX_LENGTH = (byte) 25;
    private static final byte EMAIL_MAX_LENGTH = (byte) 50;
    private static final byte PHONE_MAX_LENGTH = (byte) 25;
    private static final short PHOTO_MAX_LENGTH = (short) 2048; //1221; //16384; // 128x128

    private final byte[] _firstName = new byte[FIRSTNAME_MAX_LENGTH];
    private final byte[] _surName = new byte[LASTNAME_MAX_LENGTH];
    private final byte[] _email = new byte[EMAIL_MAX_LENGTH];
    private final byte[] _phone = new byte[PHONE_MAX_LENGTH];
    private final byte[] _photo = new byte[PHOTO_MAX_LENGTH];

    public UserService() {
        seedTestData();
    }

    public void getFirstName(APDU apdu) {
        ApduUtils.sendDataToCAD(apdu, _firstName, FIRSTNAME_MAX_LENGTH);
    }

    public void getLastName(APDU apdu) {
        ApduUtils.sendDataToCAD(apdu, _surName, LASTNAME_MAX_LENGTH);
    }

    public void getEmail(APDU apdu) {
        ApduUtils.sendDataToCAD(apdu, _email, EMAIL_MAX_LENGTH);
    }

    public void getPhone(APDU apdu) {
        ApduUtils.sendDataToCAD(apdu, _phone, PHONE_MAX_LENGTH);
    }

    public void getPhoto(APDU apdu) throws ISOException {
        ApduUtils._dataSource = _photo;
        ApduUtils._bytesLeft = PHOTO_MAX_LENGTH;
        ApduUtils._bytesOffset = 0;

        short dataLength = PHOTO_MAX_LENGTH > ApduContants.MAX_LE_VALUE ? Constants.S_FF : PHOTO_MAX_LENGTH;

        ISOException.throwIt((short)(ISO7816.SW_BYTES_REMAINING_00 | dataLength));
    }

    private void seedTestData() {
        byte[] firstName = {'D', 'm', 'i', 't', 'r', 'y'};
        byte[] surName = {'K', 'a', 'l', 'i', 'n', 'i', 'n'};
        byte[] email = {'D', 'm', 'i', 't', 'r', 'y', 'K', 'a', 'l', 'i', 'n', 'i', 'n', '@', 'i', 'c', 'l', 'o', 'u', 'd', '.', 'c', 'o', 'm'};
        byte[] phone = {'+', '4', '7', '4', '1', '5', '1', '0', '5', '7', '9'};

        Util.arrayCopyNonAtomic(firstName, (short) 0, _firstName, (short) 0, (short) firstName.length);
        Util.arrayCopyNonAtomic(surName, (short) 0, _surName, (short) 0, (short) surName.length);
        Util.arrayCopyNonAtomic(email, (short) 0, _email, (short) 0, (short) email.length);
        Util.arrayCopyNonAtomic(phone, (short) 0, _phone, (short) 0, (short) phone.length);

        Util.arrayFillNonAtomic(_photo, (short) 0, (short)256, (byte) 7);
        Util.arrayFillNonAtomic(_photo, (short) 256, (short)1, (byte) 2);
        Util.arrayFillNonAtomic(_photo, (short) 257, (short)(PHOTO_MAX_LENGTH - 257), (byte) 3);
    }
}
