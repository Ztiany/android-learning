package me.ztiany.ble;

import me.ztiany.ble.BleServerListener;

interface BleServer {

    int startGattServer();

    int stopGattServer();

    int startAdvertising();

    int stopAdvertising();

    int getServerState();

    void registerBleServerListener(BleServerListener listener);

    void unregisterBleServerListener(BleServerListener listener);

}