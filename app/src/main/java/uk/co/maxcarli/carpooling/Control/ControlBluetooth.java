package uk.co.maxcarli.carpooling.Control;

import android.bluetooth.BluetoothAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ControlBluetooth {


    /*
In questo metodo verifico che il dispositivo supporti il bluethoot
 */
    public static boolean verificaSupportoB(BluetoothAdapter mBluetoothAdapter){
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            return false;
        } else return true; //il dispositivo supporta il bluet.

    }
    /*
    verifico che il blue. sia acceso
     */
    public static boolean bluetoothIsActive(BluetoothAdapter mBluetoothAdapter) {
        if (!mBluetoothAdapter.isEnabled()) {
            return false; // ritorna falso se il dispositivo è spento
        } else return true;
    }


    public static String getBluetoothMacAddress() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {

            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        } else {
            bluetoothMacAddress = bluetoothAdapter.getAddress();
        }
        return bluetoothMacAddress;
    }




}
