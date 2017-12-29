package org.nuvola.mobile.prixpascher.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.HomeActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.dto.UserVO;
import org.nuvola.mobile.prixpascher.models.TypeAnnonceur;
import org.nuvola.mobile.prixpascher.models.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import io.cloudboost.CloudException;
import io.cloudboost.CloudNotification;
import io.cloudboost.CloudNotificationCallback;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudPush;
import io.cloudboost.CloudStringCallback;

@SuppressLint("NewApi")
public class AuthenticationFragment extends Fragment {
    LoginButton facebookLoginButton;
    TwitterLoginButton btnTwitterLogin;
	String TAG = "Fragment";
	ButtonFlat btnLogin, btnCreateAccount;
	ProgressDialog dialogPrg;
    private CallbackManager callbackManager;

    public static final AuthenticationFragment newInstance() {
		// TODO Auto-generated constructor stub
		AuthenticationFragment fragment = new AuthenticationFragment();
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.authentication_layout, container,
                false);
        facebookLoginButton = (LoginButton) view.findViewById(R.id.btnFacebookLogin);
        facebookLoginButton.setFragment(this);
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_about_me");
        facebookLoginButton.setReadPermissions(permissions);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        dialogPrg.setMessage(getActivity().getResources().getString(
                                R.string.loging));
                        dialogPrg.show();
                        facebookLoginButton.setEnabled(false);
                        insertUser(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });
		btnLogin = (ButtonFlat) view.findViewById(R.id.btn_login);

        // isTwitterInstalled();

        btnTwitterLogin = view.findViewById(R.id.btnTwitterLogin);
        btnTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.i(TAG, "logged In with twitter");

                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                if (session != null) {
                    TwitterAuthToken authToken = session.getAuthToken();

                    final UserVO userVO = new UserVO();
                    userVO.setId(String.valueOf(session.getUserId()));
                    userVO.setProviderUserId(String.valueOf(session.getUserId()));
                    userVO.setUserName(session.getUserName());
                    userVO.setAccessToken(authToken.token);

                    new TwitterUserRegister(userVO, authToken).start();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, exception.getMessage());
            }
        });

		// btnCreateAccount = (ButtonFlat) view.findViewById(R.id.btn_create_account);
		/*btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						CreateAccountActivity.class);
				startActivity(intent);
			}
		});*/
		dialogPrg = new ProgressDialog(getActivity());
		dialogPrg.setCanceledOnTouchOutside(false);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    private void insertUser(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        new FacebookUserRegister(user, accessToken).start();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,short_name,link,picture,email,website,address");
        request.setParameters(parameters);
        request.executeAsync();
	}

	private class FacebookUserRegister extends Thread {
		JSONObject user;
        AccessToken accessToken;

		public FacebookUserRegister(JSONObject user, AccessToken accessToken) {
			// TODO Auto-generated constructor stub
			this.user = user;
            this.accessToken = accessToken;
		}

        @Override
		public void run() {
			super.run();
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialogPrg.show();
				}
			});

			User userInfos = Utils.parseUser(this.user);
            final UserVO userVO = new UserVO();
            String email = userInfos.getEmail();
            getRegisteredUser(userVO, email, accessToken);
		}
    }

    private class TwitterUserRegister extends Thread {
        UserVO user;
        TwitterAuthToken accessToken;

        public TwitterUserRegister(UserVO user, TwitterAuthToken accessToken) {
            // TODO Auto-generated constructor stub
            this.user = user;
            this.accessToken = accessToken;
        }

        @Override
        public void run() {
            super.run();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogPrg.show();
                }
            });

            getRegisteredUser(user, "", null);
        }
    }

    private void getRegisteredUser(UserVO userVO, String email, AccessToken accessToken) {
        if (accessToken != null) {
            userVO.setId(accessToken.getUserId());
            userVO.setProviderUserId(accessToken.getUserId());
            userVO.setEmail(email);
        }

        try {
            org.springframework.http.HttpEntity<UserVO> requestEntity =
                    new org.springframework.http.HttpEntity<>(userVO);

            ResponseEntity<UserVO> mobileUser = Utils.MyRestemplate.getInstance().exchange(
                    getResources().getString(R.string.users_json_url) + userVO.getProviderUserId(),
                    HttpMethod.GET,
                    null, UserVO.class);

            if (mobileUser.getStatusCode().equals(HttpStatus.OK)) {
                Log.i(TAG, mobileUser.getBody().getUserName() + "got connected");
                try {
                    dialogPrg.dismiss();
                    UserSessionManager userSession = new UserSessionManager(
                            getActivity());
                    userSession.storeUserSession(mobileUser.getBody());
                    registerForNotification(mobileUser.getBody());
                    Intent intent = new Intent(
                            getActivity(),
                            HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    showDialog(getActivity().getResources()
                            .getString(R.string.login_failed));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	public void showDialog(String message) {
		Utils.logout(getActivity());
		AlertDialog.Builder buidler = new AlertDialog.Builder(getActivity());
		buidler.setMessage(message);
		buidler.setPositiveButton(
				getActivity().getResources().getString(R.string.ok_label),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method
						// stub
						facebookLoginButton.setEnabled(true);
					}
				});
		AlertDialog dialog = buidler.create();
		dialog.show();
	}

    private void registerForNotification(UserVO mobileUser) {
        String[] channels = {"global"};
        String[] channels2 = {"global", "affiliates"};

        if (mobileUser.getPrimaryShopClasse() != null && mobileUser.getTypeAnnonceur().equals(TypeAnnonceur.PROFESSIONAL)) {
            channels = channels2;
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            try {
                new CloudPush().addDevice(refreshedToken, TimeZone.getDefault().getDisplayName(),
                        channels, "prixpascher",
                        new CloudObjectCallback() {
                            @Override
                            public void done(CloudObject x, CloudException t) throws CloudException {
                                subscribeForNotification();
                            }
                        });
            } catch (CloudException e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            unsubscribeForNotification();
        }
    }

    private void unsubscribeForNotification() {
        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            CloudNotification.off("affiliates", new CloudStringCallback() {
                @Override
                public void done(String x, CloudException e) throws CloudException {
                    Log.i(TAG, "Unregistered as affiliated");
                }
            });
        } catch (CloudException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void subscribeForNotification() {
        try {
            CloudNotification.on("affiliates", new CloudNotificationCallback() {
                @Override
                public void done(Object x, CloudException t) throws CloudException {
                    Log.i(TAG, "Registered as affiliated");
                }
            });
        } catch (CloudException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isTwitterInstalled() {
        boolean twitterInstalled = false;
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
            String getPackageName = packageInfo.toString();
            if (getPackageName.equals("com.twitter.android")) {
                Toast.makeText(getActivity(), "Twitter App is installed on device!", Toast.LENGTH_LONG).show();
                twitterInstalled = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Twitter App not found on device!", Toast.LENGTH_LONG).show();
        }
        return twitterInstalled;
    }

    /*private class facebookUserCheck extends Thread {
		JSONObject user;

		public facebookUserCheck(JSONObject user) {
			// TODO Auto-generated constructor stub
			this.user = user;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			// TODO Auto-generated method stub
			String handleInsertUser = getActivity().getResources().getString(
					R.string.users_json_url);
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInsertUser);
				MultipartEntity reqEntity = new MultipartEntity();
				// reqEntity.addPart("fb_id", new StringBody(user.getId()));
				post.setEntity(reqEntity);
				HttpResponse res = client.execute(post);
				HttpEntity resEntity = res.getEntity();
				final String response_str = EntityUtils.toString(resEntity);
				if (resEntity != null) {
					Log.i(TAG, response_str);
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							try {
								dialogPrg.dismiss();
								JSONArray jsonArray = new JSONArray(
										response_str);
								if (jsonArray.length() == 1) {
									JSONObject obj = jsonArray.getJSONObject(0);
									User user = Utils.parseUser(obj);
									UserSessionManager userSession = new UserSessionManager(
											getActivity());
									userSession.storeUserSession(user);

									Intent intent = new Intent(getActivity(),
											HomeActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
											| Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							} catch (Exception e) {
								LayoutInflater inflater = LayoutInflater
										.from(getActivity());
								View promptsView = inflater.inflate(
										R.layout.username_promtps_layout, null);

								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										getActivity());

								// set prompts.xml to alertdialog builder
								alertDialogBuilder.setView(promptsView);

								final EditText message = (EditText) promptsView
										.findViewById(R.id.editTextDialogUserInput);
								alertDialogBuilder
										.setMessage(getResources().getString(
												R.string.choose_your_user_name));
								// set dialog message
								alertDialogBuilder
										.setCancelable(false)
										.setPositiveButton(
												getResources().getString(
														R.string.ok_label),
												new DialogInterface.OnClickListener() {
													public void onClick(

													DialogInterface dialog,
															int id) {
														if (!Validator
																.validUserName(message
																		.getText()
																		.toString())) {
															showDialog(getResources()
																	.getString(
																			R.string.invalid_user_name));
															return;
														}
														userName = message
																.getText()
																.toString();
														new FacebookUserRegister(
																user).start();
													}
												});

								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder
										.create();

								// show it
								alertDialog.show();
							}
						}
					});
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}*/

}
