package org.nuvola.mobile.prixpascher.fragments;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;

import org.nuvola.mobile.prixpascher.ChangePassActivity;
import org.nuvola.mobile.prixpascher.UpdateProfileActivity;
import org.nuvola.mobile.prixpascher.ProductsActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.RoundedAvatarDrawable;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.interfaces.ProfileComunicator;
import org.nuvola.mobile.prixpascher.models.User;
import com.gc.materialdesign.views.ButtonRectangle;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProfileFragment extends Fragment {

    ButtonRectangle btnEdit,btnLogout, btnShowMyProducts,
            btnChangePass, btnUpdateProduct;
	EditText displayName, email, website, address, phone, userName;
	ImageView avt;
	ProfileComunicator listener;
	ProgressDialog loadingDialog;
	User userProfile;
	public static final String TAG = "ProfileFragment";
	ProgressDialog prgDialog;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		listener = (ProfileComunicator) getActivity();
	}

	public static final ProfileFragment newInstance() {
		// TODO Auto-generated constructor stub
		ProfileFragment fragment = new ProfileFragment();
		return fragment;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		UserSessionManager sessionManager = new UserSessionManager(
				getActivity());
		// userProfile = new User();
		userProfile = sessionManager.getUserSession();
		if (userProfile != null) {
			displayName.setText(userProfile.getFullName());
			website.setText(userProfile.getWebsite());
			phone.setText(userProfile.getPhone());
			address.setText(userProfile.getAddress());
			email.setText(userProfile.getEmail());
			userName.setText(userProfile.getUserName());
			if (userProfile.getAvt() != null
					&& !userProfile.getAvt().equalsIgnoreCase("")) {
				Log.i(TAG, "Khac null");
				String avtString = "";
				if (Utils.checkFacebookAvt(userProfile.getAvt())) {
					avtString = userProfile.getAvt();
				} else {
					avtString = getResources().getString(R.string.domain_url)
							+ userProfile.getAvt();
				}
				Log.i(TAG, avtString);
				Ion.with(getActivity(), avtString).withBitmap()
						.resize(200, 200).centerCrop()
						.placeholder(R.drawable.ic_avatar)
						.error(R.drawable.ic_avatar).asBitmap()
						.setCallback(new FutureCallback<Bitmap>() {

							@Override
							public void onCompleted(Exception arg0,
									Bitmap bitmap) {
								// TODO Auto-generated method stub
								if (bitmap != null) {
									RoundedAvatarDrawable avtDrawable = new RoundedAvatarDrawable(
											bitmap);
									avt.setImageDrawable(avtDrawable);
								}
							}

						});

			}

			btnEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),
							UpdateProfileActivity.class);
					intent.putExtra(constants.COMMON_KEY, userProfile);
					startActivity(intent);
				}
			});

			btnChangePass.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),
							ChangePassActivity.class);
					intent.putExtra(constants.COMMON_KEY, userProfile);
					startActivity(intent);
				}
			});

			btnLogout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Utils.logout(getActivity());
					listener.logout();
				}
			});

			btnShowMyProducts.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(),
                            ProductsActivity.class);
                    intent.putExtra(constants.USER_ID_KEY, userProfile.getId());
                    startActivity(intent);
				}
			});

			btnUpdateProduct.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	/*				AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage(getActivity().getResources().getString(
							R.string.update_products_msg));
					builder.setNegativeButton(getActivity().getResources()
							.getString(R.string.cancel_label),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					builder.setPositiveButton(getActivity().getResources()
							.getString(R.string.ok_label),
							new DialogInterface.OnClickListener() {

								@SuppressLint("NewApi")
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (Utils
											.isConnectingToInternet(getActivity())) {
										if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
											new Upload()
													.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
										} else {
											new Upload().execute();
										}
									} else {
										Toast ts = Toast.makeText(
												getActivity(),
												getResources().getString(
														R.string.open_network),
												Toast.LENGTH_LONG);
										ts.show();
									}
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();*/

                    final Dialog dialog = new Dialog(getActivity(),getResources().getString(R.string.alert),getActivity().getResources().getString(
                            R.string.update_products_msg));
                    dialog.addCancelButton(getResources().getString(R.string.cancel_label));

                    dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Utils
                                    .isConnectingToInternet(getActivity())) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    new Upload()
                                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    new Upload().execute();
                                }
                            } else {
                                Toast ts = Toast.makeText(
                                        getActivity(),
                                        getResources().getString(
                                                R.string.open_network),
                                        Toast.LENGTH_LONG);
                                ts.show();
                            }
                        }
                    });

                    dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();


				}
			});

		}
	}

	private class Upload extends AsyncTask<Void, Void, Boolean> {
		String fb_id = null;
		int user_id = 0;

		public Upload() {
			// TODO Auto-generated constructor stub
		}

		public Upload(String fb_id, int user_id) {
			this.fb_id = fb_id;
			this.user_id = user_id;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			prgDialog.dismiss();
		}

		protected void onPostExecute() {
			prgDialog.dismiss();
		};

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			prgDialog = new ProgressDialog(getActivity());
			prgDialog.setMessage(getActivity().getResources().getString(
					R.string.please_wait_msg));
			prgDialog.setCanceledOnTouchOutside(false);
			prgDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.products_json_url)
					+ "update";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("user_id", new StringBody(userProfile.getId()
						+ ""));
				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				HttpEntity resEntity = response.getEntity();
				final String response_str = EntityUtils.toString(resEntity);
				if (resEntity != null) {
					Log.i("RESPONSE", response_str);
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							try {
								prgDialog.dismiss();
								Toast ts = Toast.makeText(
										getActivity(),
										getActivity().getResources().getString(
												R.string.success_action), Toast.LENGTH_LONG);
								ts.show();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Exception ex) {
				Log.e("Debug", "error: " + ex.getMessage(), ex);
			}
			return null;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.profile_fragment_layout, null);
		btnLogout = (ButtonRectangle) view.findViewById(R.id.btn_logout);
		btnEdit = (ButtonRectangle) view.findViewById(R.id.btn_update);
		// btnShowMarkProducts = (Button) view
		// .findViewById(R.id.btn_show_mark_products);
		btnShowMyProducts = (ButtonRectangle) view
				.findViewById(R.id.btn_show_my_products);
		displayName = (EditText) view.findViewById(R.id.display_name);
		email = (EditText) view.findViewById(R.id.email);
		address = (EditText) view.findViewById(R.id.address);
		phone = (EditText) view.findViewById(R.id.phone);
		website = (EditText) view.findViewById(R.id.websites);
		avt = (ImageView) view.findViewById(R.id.avt);
		userName = (EditText) view.findViewById(R.id.user_name);
		btnChangePass = (ButtonRectangle) view.findViewById(R.id.btn_change_pass);
		btnUpdateProduct = (ButtonRectangle) view.findViewById(R.id.btn_update_products);
		return view;
	}
}
