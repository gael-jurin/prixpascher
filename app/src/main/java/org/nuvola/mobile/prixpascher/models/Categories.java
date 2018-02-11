package org.nuvola.mobile.prixpascher.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Categories implements Parcelable {
	public static final String TAG_ID = "id";
	public static final String TAG_NAME = "name";
	int id;
	String name;

	public Categories() {
		// TODO Auto-generated constructor stub
	}

	public Categories(Parcel in) {
		id = in.readInt();
		name = in.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(name);
	}

	public static final Creator<Categories> CREATOR = new Creator<Categories>() {
		@Override
		public Categories createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Categories(in);
		}

		@Override
		public Categories[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Categories[size];
		}
	};
}
