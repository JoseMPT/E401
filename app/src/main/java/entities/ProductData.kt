package entities

import android.os.Parcel
import android.os.Parcelable

data class ProductData(
    var urlImage: String? = null,
    var smallName: String? = null,
    var name: String? = null,
    var price: Double = 0.0,
    var description: String? = null,
    var category: String? = null
):Parcelable {
    constructor() : this(null, null, null, 0.0, null, null)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(urlImage)
        parcel.writeString(smallName)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeString(description)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductData> {
        override fun createFromParcel(parcel: Parcel): ProductData {
            return ProductData(parcel)
        }

        override fun newArray(size: Int): Array<ProductData?> {
            return arrayOfNulls(size)
        }
    }
}
