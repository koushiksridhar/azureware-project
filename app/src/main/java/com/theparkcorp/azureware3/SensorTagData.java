package com.theparkcorp.azureware3;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.graphics.YuvImage;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT8;

/**
 * Created by allvac on 1/21/2017.
 */

public class SensorTagData {

    private static double scaledX;
    private static double scaledY;
    private static double scaledZ;

    private double[] AccelArray = new double[2];

    public static double extractAccelX(final BluetoothGattCharacteristic c){
        Integer x = c.getIntValue(FORMAT_SINT8, 5);

        scaledX = x / 64.0;


        double[] AccelArray = {scaledX, scaledY, scaledZ};

        return scaledX;
    }

    public static double extraAccelY(final BluetoothGattCharacteristic c){
        Integer y = c.getIntValue(FORMAT_SINT8, 4);


        scaledY = y / 64.0;


        return scaledY;
    }

    public static double extraAccelZ(final BluetoothGattCharacteristic c){
        Integer z = c.getIntValue(FORMAT_SINT8, 3) * -1;

        scaledZ = z / 64.0;

        return scaledZ;

    }

    public static Integer shortSignedAtOffset(BluetoothGattCharacteristic c, int offsett){
        Integer lowerByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offsett);
        Integer upperByte = c.getIntValue(FORMAT_SINT8, offsett + 1) ;

        return (upperByte << 8) + lowerByte;


    }

    public static Integer shortUnsignedAtOffset(BluetoothGattCharacteristic c, int offsett){
        Integer lowerByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offsett);
        Integer upperByte = c.getIntValue(FORMAT_SINT8, offsett + 1) ;

        return (upperByte << 8) + lowerByte;


    }
}
