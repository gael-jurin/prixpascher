package org.nuvola.mobile.prixpascher.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonFlat;

import org.nuvola.mobile.prixpascher.AnnouncesActivity;
import org.nuvola.mobile.prixpascher.ChangePassActivity;
import org.nuvola.mobile.prixpascher.OffersActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.UpdateProfileActivity;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ContactMailVO;
import org.nuvola.mobile.prixpascher.interfaces.ProfileComunicator;
import org.nuvola.mobile.prixpascher.models.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    @BindView(R.id.btn_update) Button btnEdit;
    @BindView(R.id.btn_logout) Button btnLogout;
    @BindView(R.id.btn_change_pass) ButtonFlat btnChangePass;
    @BindView(R.id.btn_update_products) ButtonFlat btnUpdateProduct;
    @BindView(R.id.btn_show_my_products) ButtonFlat btnShowMyProducts;
    @BindView(R.id.btn_show_my_announces) ButtonFlat btnShowMyAnnounces;
	@BindView(R.id.user_name) EditText userName;
	@BindView(R.id.product_feed) EditText feed;
	@BindView(R.id.address) EditText address;
	@BindView(R.id.websites) EditText website;
	@BindView(R.id.display_name) EditText displayName;
	@BindView(R.id.email) EditText email;
	@BindView(R.id.avt) ImageView avt;

	ProfileComunicator listener;
	ProgressDialog loadingDialog;
	User userProfile;
	public static final String TAG = "ProfileFragment";
	ProgressDialog prgDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
            feed.setText(userProfile.getWebsite());
			address.setText(userProfile.getAddress());
			email.setText(userProfile.getEmail());
			userName.setText(userProfile.getUserName());
			if (userProfile.getAvt() != null
					&& !userProfile.getAvt().equalsIgnoreCase("")) {
				String avtString = userProfile.getAvt();
				Log.i(TAG, avtString);

				Utils.MyPicasso.with(getContext())
						.load(avtString)
						.resize(200, 200).centerCrop()
						.placeholder(R.drawable.ic_avatar)
						.into(avt);
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
                            OffersActivity.class);
                    intent.putExtra(constants.USER_ID_KEY, userProfile.getId());
                    startActivity(intent);
				}
			});

            btnShowMyAnnounces.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(),
                            AnnouncesActivity.class);
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

                    MaterialDialog.Builder alertDialogBuilder = new MaterialDialog.Builder(getActivity());
                    alertDialogBuilder.customView(R.layout.renew_prompts_layout, true);

                    final MaterialDialog alertDialog = alertDialogBuilder.build();
                    alertDialog.show();

                    ButtonFlat sendAlert = (ButtonFlat) alertDialog.findViewById(R.id.sendRequest);

                    sendAlert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Utils
                                    .isConnectingToInternet(getActivity())) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    new Upload()
                                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    new Upload().execute();
                                    alertDialog.dismiss();
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
                    alertDialog.show();
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
			ContactMailVO contactVO = new ContactMailVO();
			contactVO.setSubject("PrixPasCher - Store");
			contactVO.setEmail(userProfile.getEmail());
			contactVO.setMessage("Demande de Boost Produits de la boutique : " +
                    userProfile.getUserName() + " - " + userProfile.getWebsite());

			try {
				org.springframework.http.HttpEntity<ContactMailVO> requestEntity =
						new org.springframework.http.HttpEntity<>(contactVO);
				ResponseEntity<String> resEntity = Utils.MyRestemplate.getInstance(getContext()).exchange(
						getResources().getString(R.string.msg_send_url),
						HttpMethod.POST,
						requestEntity, String.class);

				if (resEntity != null) {
					Log.i("RESPONSE", resEntity.getBody());
					if (!resEntity.getStatusCode().equals(HttpStatus.OK)) {
						prgDialog.dismiss();
                        // showDialog(getResources().getString(
								// R.string.spam_msg));
					}
				}
				return true;
			} catch (Exception ex) {
				Log.e("Debug", "error: " + ex.getMessage(), ex);
				return false;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_fragment_layout, null);
		ButterKnife.bind(this, view);
		// btnShowMarkProducts = (Button) view
		// .findViewById(R.id.btn_show_mark_products);

		return view;
	}
}
