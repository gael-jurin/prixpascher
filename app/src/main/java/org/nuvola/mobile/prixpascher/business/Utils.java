package org.nuvola.mobile.prixpascher.business;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import com.facebook.AccessToken;

import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.models.User;

import java.io.File;
import java.net.URL;

public class Utils {
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	public static File createDirOnSDCard(String dirName) {
		File folder = new File(Environment.getExternalStorageDirectory() + "/"
				+ dirName);
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		return folder;
	}

    @SuppressLint("NewApi")
    public static int getScreenWidth(Context context) {
        int columnWidth;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

	public static String getFileName(URL extUrl) {
		String filename = "";
		String path = extUrl.getPath();
		String[] pathContents = path.split("[\\\\/]");
		if (pathContents != null) {
			int pathContentsLength = pathContents.length;
			System.out.println("Path Contents Length: " + pathContentsLength);
			for (int i = 0; i < pathContents.length; i++) {
				System.out.println("Path " + i + ": " + pathContents[i]);
			}
			String lastPart = pathContents[pathContentsLength - 1];
			String[] lastPartContents = lastPart.split("\\.");
			if (lastPartContents != null && lastPartContents.length > 1) {
				int lastPartContentLength = lastPartContents.length;
				System.out
						.println("Last Part Length: " + lastPartContentLength);
				String name = "";
				for (int i = 0; i < lastPartContentLength; i++) {
					System.out.println("Last Part " + i + ": "
							+ lastPartContents[i]);
					if (i < (lastPartContents.length - 1)) {
						name += lastPartContents[i];
						if (i < (lastPartContentLength - 2)) {
							name += ".";
						}
					}
				}
				String extension = lastPartContents[lastPartContentLength - 1];
				filename = name + "." + extension;
				System.out.println("Name: " + name);
				System.out.println("Extension: " + extension);
				System.out.println("Filename: " + filename);
			}
		}
		return filename;
	}

	public static Bitmap getRoundedShape(Bitmap bitmap, int width, int height) {
		Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(1);
		paint.setDither(true);
		paint.setShader(shader);
		Canvas c = new Canvas(circleBitmap);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		return circleBitmap;
	}

	public static void logout(Context context) {
		UserSessionManager sessionManager = new UserSessionManager(context);
		sessionManager.clearAll();
		if (AccessToken.getCurrentAccessToken() != null) {
            AccessToken.setCurrentAccessToken(null);
		} else {
			AccessToken.refreshCurrentAccessTokenAsync();
		}
	}

	public static String getFacebookAvt(String fbId, int width, int height) {
		return "http://graph.facebook.com/" + fbId + "/picture?width=" + width
				+ "&height=" + height;
	}

	public static Boolean checkFacebookAvt(String path) {
		if (path.indexOf("http://graph.facebook.com") == -1) {
			return false;
		}
		return true;
	}

	public static User parseUser(JSONObject jsonObj) {
		User user = new User();
		try {
			String id = jsonObj.getString(User.TAG_ID);
			user.setId(id);
			String email = jsonObj.getString(User.TAG_EMAIL);
			user.setEmail(email);
			String fullName = jsonObj.getString(User.TAG_FULL_NAME);
			user.setFullName(fullName);
			String userName = jsonObj.getString(User.TAG_USER_NAME);
			user.setUserName(userName);
			/*String address = jsonObj.getString(User.TAG_ADDRESS);
			user.setAddress(address);*/
			String avt = ((JSONObject)((JSONObject)jsonObj.get("picture")).get("data")).getString(User.TAG_AVT);
			user.setAvt(avt);
			/*String fbId = jsonObj.getString(User.TAG_FB_ID);
			user.setFbId(fbId);*/
			String website = jsonObj.getString(User.TAG_WEBSITES);
			user.setWebsite(website);
			/*String phone = jsonObj.getString(User.TAG_PHONE);
			user.setPhone(phone);*/
		} catch (Exception e) {
			e.printStackTrace();
			return user;
		}
		return user;
	}

}
