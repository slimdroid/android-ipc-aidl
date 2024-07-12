// IServiceCommunication.aidl
package com.slimdroid.ipcaidl;

import com.slimdroid.ipcaidl.data.TransferObject;
import com.slimdroid.ipcaidl.IServiceResponseListener;

// Declare any non-default types here with import statements
interface IServiceCommunication {

    void sendObjectIn(in TransferObject object);

    void sendObjectOut(out TransferObject object);

    void sendObjectInOut(inout TransferObject object);

    TransferObject getObject();

    oneway void doAsyncWork(in IServiceResponseListener listener);

    oneway void throwException();

}