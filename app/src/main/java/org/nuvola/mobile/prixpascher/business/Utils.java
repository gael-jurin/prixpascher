package org.nuvola.mobile.prixpascher.business;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.MarketApp;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.models.User;
import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver;
import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver.ConnectivityReceiverListener;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {
	private final static String TAG = "Utils.class";
    private static NetworkStateReceiver connectivityReceiver;

    public static boolean isConnectingToInternet(Context context) {
		final ConnectivityManager conMgr = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
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
        } catch (NoSuchMethodError ignore) { // Older device
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
		return "https://graph.facebook.com/" + fbId + "/picture?width=" + width
				+ "&height=" + height;
	}

	public static Boolean checkFacebookAvt(String path) {
		if (path.indexOf("https://graph.facebook.com") == -1) {
			return false;
		}
		return true;
	}

	public static class MyPicasso {
        private static Picasso instance;

        public static Picasso with(Context context) {
            if (instance == null) {
                instance =
                new Picasso.Builder(context)
                        .downloader(new OkHttp3Downloader(context))
                        .listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                exception.printStackTrace();
                            }})
                        .build();

            }
            return instance;
        }

        private MyPicasso() {
            throw new AssertionError("No instances.");
        }
	}

	public static class MyRestemplate {
        private static RestTemplate instance;

        public static RestTemplate getInstance(final Context context) {
            if (instance == null) {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                instance = restTemplate;
            }

            instance.setErrorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}

				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
                    Log.e(TAG, response.getStatusText());
				}
			});

            return instance;
        }
    }

	public static String buildImageUri(String imageId) {
        return "https://www.prixpascher.ma/api/bucket/picture/" + imageId;
    }

	public static String formatToYesterdayOrToday(Date dateTime) {
		String date = new SimpleDateFormat("dd/MM/yyyy").format(dateTime);
		String time = new SimpleDateFormat("HH:mm:ss").format(dateTime);
		try {
			if (DateUtils.isToday(dateTime.getTime())) {
				return "Aujourd'hui à " + time;
			} else {
                long sourceDuration = new Date().getTime() - dateTime.getTime();
                if (TimeUnit.DAYS.convert(sourceDuration, TimeUnit.MILLISECONDS) == 1) {
                    return "Hier à " + time;
                } else {
                    return "le " + date + " à " + time;
                }
            }
		} catch (Exception e) {
			return "le " + date + " à " + time;
		}
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
			Log.e(TAG, e.getMessage());
			return user;
		}
		return user;
	}

	public static void showCommingSoon(final Activity context) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder buidler = new AlertDialog.Builder(context);
                buidler.setMessage(context.getString(R.string.coming_soon_label));
                buidler.setPositiveButton(context.getResources().getString(R.string.ok_label),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method
                                // stub
                            }
                        });
                AlertDialog dialog = buidler.create();
                dialog.show();
            }
        });
    }

    public static void registerNetworkContext(Context context) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        connectivityReceiver = new NetworkStateReceiver();
        context.registerReceiver(connectivityReceiver, intentFilter);

        MarketApp.getInstance().setConnectivityListener((ConnectivityReceiverListener) context);
    }

    public static void unregisterNetworkContext(Context context) {
        context.unregisterReceiver(connectivityReceiver);
    }

    public static void showSnack(View cView, boolean isConnected) {
		String message;
		int color;
		if (!isConnected) {
			message = "No internet connection, try later";
			color = Color.RED;


			Snackbar snackbar = Snackbar
					.make(cView, message, Snackbar.LENGTH_LONG)
					.setAction("Dismiss", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
						}
					});

			View sbView = snackbar.getView();
			TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
			textView.setTextColor(color);
			snackbar.show();
		}
	}
}
