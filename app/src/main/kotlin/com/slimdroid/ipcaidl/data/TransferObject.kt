package com.slimdroid.ipcaidl.data

import android.os.Parcel
import android.os.Parcelable

class TransferObject() : Parcelable {

    var value: String = "DEFAULT VALUE"

    companion object CREATOR : Parcelable.Creator<TransferObject> {
        override fun createFromParcel(parcel: Parcel): TransferObject {
            return TransferObject(parcel)
        }

        override fun newArray(size: Int): Array<TransferObject?> {
            return arrayOfNulls(size)
        }
    }

    constructor(inParcel: Parcel) : this() {
        readFromParcel(inParcel)
    }

    override fun writeToParcel(outParcel: Parcel, flags: Int) {
        outParcel.writeString(value)
    }

    fun readFromParcel(inParcel: Parcel) {
        value = inParcel.readString() ?: ""
    }

    override fun describeContents(): Int = 0

    override fun toString(): String {
        return "Object(value=${value})"
    }

}