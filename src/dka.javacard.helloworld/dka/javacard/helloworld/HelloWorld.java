package dka.javacard.helloworld;

import javacard.framework.*;

public class HelloWorld extends Applet {

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new HelloWorld();
    }

    protected HelloWorld() {
        register();
    }

    public void process(APDU apdu) {
        //Insert your code here
    }
}
