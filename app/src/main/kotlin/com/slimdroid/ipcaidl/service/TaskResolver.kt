package com.slimdroid.ipcaidl.service

import android.os.RemoteException
import android.util.Log
import com.slimdroid.ipcaidl.IServiceCommunication
import com.slimdroid.ipcaidl.IServiceResponseListener
import com.slimdroid.ipcaidl.TAG
import com.slimdroid.ipcaidl.data.TransferObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class TaskResolver : IServiceCommunication.Stub() {

    private val scope = CoroutineScope(Dispatchers.Default)

    // in
    override fun sendObjectIn(transferObject: TransferObject) {
        Log.d(TAG, "S: receive object: ${transferObject}, thread: ${Thread.currentThread().name}")
        transferObject.value = "KEYWORD IN"

        Log.d(TAG, "S: change object: ${transferObject}, thread: ${Thread.currentThread().name}")
    }

    // out
    override fun sendObjectOut(transferObject: TransferObject) {
        Log.d(TAG, "S: receive object: ${transferObject}, thread: ${Thread.currentThread().name}")
        transferObject.value = "KEYWORD OUT"

        Log.d(TAG, "S: change object: ${transferObject}, thread: ${Thread.currentThread().name}")
    }

    // inout
    override fun sendObjectInOut(transferObject: TransferObject) {
        Log.d(TAG, "S: receive object: ${transferObject}, thread: ${Thread.currentThread().name}")
        transferObject.value = "KEYWORD INOUT"

        Log.d(TAG, "S: change object: ${transferObject}, thread: ${Thread.currentThread().name}")
    }

    override fun getObject(): TransferObject {
        val transferObject = TransferObject().apply {
            value = "OBJECT FROM SERVICE"
        }
        Log.d(TAG, "S: return object: ${transferObject}, thread: ${Thread.currentThread().name}")
        return transferObject
    }

    // oneway
    override fun doAsyncWork(listener: IServiceResponseListener) {
        Log.d(TAG, "S: async work, thread: ${Thread.currentThread().name}")
        scope.launch {
            Log.d(TAG, "S: launch coroutine, thread: ${Thread.currentThread().name}")
            delay(2000)
            try {
                Log.d(TAG, "S: send result, thread: ${Thread.currentThread().name}")
                if (Random.nextBoolean()) listener.onSuccess("Success")
                else listener.onFailure("Failure")
            } catch (e: RemoteException) {
                Log.e(TAG, "S: activity is not available, thread: ${Thread.currentThread().name}")
            }
        }
    }

    override fun throwException() {
        Log.d(TAG, "Killing in progress, thread: ${Thread.currentThread().name}")
        android.os.Process.killProcess(android.os.Process.myPid())
    }

}