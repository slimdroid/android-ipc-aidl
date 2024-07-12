// IServiceResponseListener.aidl
package com.slimdroid.ipcaidl;

// Declare any non-default types here with import statements

oneway interface IServiceResponseListener {
    void onSuccess(in String success);
    void onFailure(in String failure);
}