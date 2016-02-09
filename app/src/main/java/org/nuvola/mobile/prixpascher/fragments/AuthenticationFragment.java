package org.nuvola.mobile.prixpascher.fragments;

import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.nuvola.mobile.prixpascher.CreateAccountActivity;
import org.nuvola.mobile.prixpascher.HomeActivity;
import org.nuvola.mobile.prixpascher.LoginActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Validator;
import org.nuvola.mobile.prixpascher.models.User;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.gc.materialdesign.views.ButtonRectangle;

@SuppressLint("NewApi")
public class AuthenticationFragment extends Fragment {
	LoginButton facebookLoginButton;
	private UiLifecycleHelper uiHelper;
	String TAG = "Fragment";
	ButtonRectangle btnLogin, btnCreateAccount;
	ProgressDialog dialogPrg;
	String userName = null;

	public static final AuthenticationFragment newInstance() {
		// TODO Auto-generated constructor stub
		AuthenticationFragment fragment = new AuthenticationFragment();
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.authentication_layout, container,
				false);
		facebookLoginButton = (LoginButton) view
				.findViewById(R.id.btnFacebookLogin);
		facebookLoginButton.setFragment(this);
		facebookLoginButton.setReadPermissions(Arrays.asList("email"));
		btnLogin = (ButtonRectangle) view.findViewById(R.id.btn_login);
		btnCreateAccount = (ButtonRectangle) view.findViewById(R.id.btn_create_account);
		btnLogin.setOnClickListener(new View.OnClickListener() {
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
		});
		dialogPrg = new ProgressDialog(getActivity());
		dialogPrg.setCanceledOnTouchOutside(false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i("FB AUT FRAGMENT", "Logged in...");
		} else if (state.isClosed()) {
			Log.i("FB AUT FRAGMENT", "Logged out...");
		}
	}

	private void insertUser(Session session) {
		Request.newMeRequest(session, new GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				// TODO Auto-generated method stub
				new facebookUserCheck(user).start();
			}
		}).executeAsync();
	}

	private class facebookUserCheck extends Thread {
		GraphUser user;

		public facebookUserCheck(GraphUser user) {
			// TODO Auto-generated constructor stub
			this.user = user;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			// TODO Auto-generated method stub
			String handleInsertUser = getActivity().getResources().getString(
					R.string.users_json_url)
					+ "facebook_user_check";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInsertUser);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("fb_id", new StringBody(user.getId()));
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
														new facebookUserRegister(
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
	}

	private class facebookUserRegister extends Thread {
		GraphUser user;

		public facebookUserRegister(GraphUser user) {
			// TODO Auto-generated constructor stub
			this.user = user;
		}

		@Override
		public void run() {
			super.run();
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					dialogPrg.show();
				}
			});
			String handleInsertUser = getActivity().getResources().getString(
					R.string.users_json_url)
					+ "facebook_user_register";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInsertUser);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("fb_id", new StringBody(user.getId()));
				reqEntity.addPart("fullname", new StringBody(user.getName()));
				reqEntity.addPart("email",
						new StringBody(user.asMap().get("email").toString()));
				reqEntity.addPart("username", new StringBody(userName));
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
								JSONObject jsonObj = new JSONObject(
										response_str);
								if (jsonObj.getString("ok").equals("0")) {
									// show error email;
									showDialog(getActivity().getResources()
											.getString(R.string.email_exist));
									return;
								}

								if (jsonObj.getString("ok").equals("1")) {
									// show error username
									showDialog(getActivity()
											.getResources()
											.getString(R.string.user_name_exist));
									return;
								}

								if (jsonObj.getString("ok").equals("2")) {
									// show unknow username
									showDialog(getActivity().getResources()
											.getString(R.string.login_failed));
									return;
								}

							} catch (Exception e) {
								JSONArray jsonArray;
								try {
									jsonArray = new JSONArray(response_str);
									if (jsonArray.length() == 1) {
										JSONObject obj = jsonArray
												.getJSONObject(0);
										User user = Utils.parseUser(obj);
										UserSessionManager userSession = new UserSessionManager(
												getActivity());
										userSession.storeUserSession(user);
										Intent intent = new Intent(
												getActivity(),
												HomeActivity.class);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
												| Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(intent);
									}
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									showDialog(getActivity().getResources()
											.getString(R.string.login_failed));
								}

							}
						}
					});
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		if (Session.getActiveSession() != null
				&& Session.getActiveSession().isOpened()) {
			dialogPrg.setMessage(getActivity().getResources().getString(
					R.string.loging));
			dialogPrg.show();
			facebookLoginButton.setEnabled(false);
			insertUser(Session.getActiveSession());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
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

}
