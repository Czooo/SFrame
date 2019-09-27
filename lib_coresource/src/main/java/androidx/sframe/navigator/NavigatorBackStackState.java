package androidx.sframe.navigator;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author create by Zoran on 2019-09-26
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavigatorBackStackState implements Parcelable {

	private final Bundle mArguments;
	private final int mDestinationId;

	public NavigatorBackStackState(@NonNull NavigatorBackStackEntry navigatorBackStackEntry) {
		this.mDestinationId = navigatorBackStackEntry.getDestination().getDestinationId();
		this.mArguments = navigatorBackStackEntry.getArguments();
	}

	private NavigatorBackStackState(Parcel in) {
		this.mDestinationId = in.readInt();
		this.mArguments = in.readBundle(this.getClass().getClassLoader());
	}

	public int getDestinationId() {
		return this.mDestinationId;
	}

	@Nullable
	public Bundle getArguments() {
		return this.mArguments;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(this.mDestinationId);
		parcel.writeBundle(this.mArguments);
	}

	public static final Creator<NavigatorBackStackState> CREATOR = new Creator<NavigatorBackStackState>() {
		@Override
		public NavigatorBackStackState createFromParcel(Parcel in) {
			return new NavigatorBackStackState(in);
		}

		@Override
		public NavigatorBackStackState[] newArray(int size) {
			return new NavigatorBackStackState[size];
		}
	};
}
