package com.slimdroid.ipcaidl.ui

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slimdroid.ipcaidl.IServiceCommunication
import com.slimdroid.ipcaidl.IServiceResponseListener
import com.slimdroid.ipcaidl.TAG
import com.slimdroid.ipcaidl.data.TransferObject
import com.slimdroid.ipcaidl.service.IpcService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val _uiState = MutableStateFlow(MainUiState.Default)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var resolver: IServiceCommunication? = null
        get() = field ?: run {
            Log.e(TAG, "VM: service is not bound, thread: ${Thread.currentThread().name}")
            null
        }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            resolver = IServiceCommunication.Stub.asInterface(service)
            _uiState.update { state ->
                state.copy(
                    isServiceConnected = true,
                    isProgress = false
                )
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            resolver = null
            _uiState.update { state ->
                state.copy(
                    isServiceConnected = false,
                    isProgress = false
                )
            }
        }
    }

    init {
        context.bindService(
            Intent(context, IpcService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private val transferObject = TransferObject().apply {
        value = "object for sending"
    }

    fun sendObjectIn() {
        Log.d(TAG, "---IN---")
        Log.d(TAG, "VM: send: ${transferObject}, thread: ${Thread.currentThread().name}")
        try {
            resolver?.sendObjectIn(transferObject)
            Log.d(
                TAG,
                "VM: after sending: ${transferObject}, thread: ${Thread.currentThread().name}"
            )
        } catch (e: RemoteException) {
            Log.e(TAG, "VM: service is not bound, thread: ${Thread.currentThread().name}")
        }
    }

    fun sendObjectOut() {
        Log.d(TAG, "---OUT---")
        Log.d(TAG, "VM: send: ${transferObject}, thread: ${Thread.currentThread().name}")
        try {
            resolver?.sendObjectOut(transferObject)
            Log.d(
                TAG,
                "VM: after sending: ${transferObject}, thread: ${Thread.currentThread().name}"
            )
        } catch (e: RemoteException) {
            Log.e(TAG, "VM: service is not bound, thread: ${Thread.currentThread().name}")
        }
    }

    fun sendObjectInOut() {
        Log.d(TAG, "---INOUT---")
        Log.d(TAG, "VM: send: ${transferObject}, thread: ${Thread.currentThread().name}")
        try {
            resolver?.sendObjectInOut(transferObject)
            Log.d(
                TAG,
                "VM: after sending: ${transferObject}, thread: ${Thread.currentThread().name}"
            )
        } catch (e: RemoteException) {
            Log.e(TAG, "VM: service is not bound, thread: ${Thread.currentThread().name}")
        }
    }

    fun getObject() {
        Log.d(TAG, "---GET---")
        try {
            Log.d(TAG, "VM: wait object from service, thread: ${Thread.currentThread().name}")
            val transferObject = resolver?.getObject()
            Log.d(
                TAG,
                "S: receive object: ${transferObject}, thread: ${Thread.currentThread().name}"
            )
        } catch (e: RemoteException) {
            Log.e(TAG, "VM: service is not bound, thread: ${Thread.currentThread().name}")
        }
    }

    fun doAsyncWork() {
        Log.d(TAG, "---ONEWAY---")
        _uiState.update { state -> state.copy(isProgress = true) }
        try {
            Log.d(TAG, "VM: do async work, thread: ${Thread.currentThread().name}")
            resolver?.doAsyncWork(object : IServiceResponseListener.Stub() {
                override fun onSuccess(success: String) {
                    Log.d(
                        TAG,
                        "VM: response from async work, thread: ${Thread.currentThread().name}"
                    )
                    viewModelScope.launch {
                        Log.d(
                            TAG,
                            "VM: response from async work in coroutine, thread: ${Thread.currentThread().name}"
                        )
                        _uiState.update { state ->
                            state.copy(
                                isProgress = false,
                                response = "thread: ${Thread.currentThread().name} $success"
                            )
                        }
                    }
                }

                override fun onFailure(failure: String) {
                    Log.d(
                        TAG,
                        "VM: response from async work, thread: ${Thread.currentThread().name}"
                    )
                    viewModelScope.launch {
                        Log.d(
                            TAG,
                            "VM: response from async work in coroutine, thread: ${Thread.currentThread().name}"
                        )
                        _uiState.update { state ->
                            state.copy(
                                isProgress = false,
                                response = "thread: ${Thread.currentThread().name} $failure"
                            )
                        }
                    }
                }
            })
        } catch (e: RemoteException) {
            Log.e(TAG, "VM: service is not bound, thread: ${Thread.currentThread().name}")
        }
    }

    fun killProcess() {
        try {
            resolver?.throwException()
        } catch (e: RemoteException) {
            Log.e(TAG, "VM: service is not bound, thread: ${Thread.currentThread().name}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        resolver?.let {
            context.unbindService(serviceConnection)
        }
    }

}