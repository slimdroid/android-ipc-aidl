package com.slimdroid.ipcaidl.ui

data class MainUiState(
    val isServiceConnected: Boolean,
    val isProgress: Boolean,
    val response: String = ""
) {
    companion object {
        val Default = MainUiState(
            isServiceConnected = false,
            isProgress = false
        )
    }
}