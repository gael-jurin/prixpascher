package org.nuvola.mobile.prixpascher.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.dto.SToken;
import org.nuvola.mobile.prixpascher.dto.ShopInfoVO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ShopGeoFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap googleMap;

    private List<ShopInfoVO> shopList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.maps_container_layout, null);

        mapView = rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);

        new LoadShopLocationDataTask().execute();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                LatLng(33.572625, -7.614413), 10));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private class LoadShopLocationDataTask extends AsyncTask<Void, Void, List<ShopInfoVO>> {

        @Override
        protected List<ShopInfoVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return loadLocalisation();
        }

        @Override
        protected void onPostExecute(List<ShopInfoVO> result) {
            parseAndAppend();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
        }
    }

    private void parseAndAppend() {
        for (ShopInfoVO shop : shopList) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(shop.getStoreContacts().get(0).getLoc().getLatitude(),
                            shop.getStoreContacts().get(0).getLoc().getLongitude()))
                    .title(shop.getName()));
        }
    }

    private List<ShopInfoVO> loadLocalisation() {
        try {
            SToken token = new SToken();
            token.setSerialToken(getString(R.string.serial));
            HttpEntity<SToken> requestEntity = new HttpEntity<>(token);
            ResponseEntity<ShopInfoVO[]> resEntity = Utils.MyRestemplate.getInstance(getActivity()).exchange(
                    getContext().getResources().getString(R.string.shop_json_url),
                    HttpMethod.POST,
                    requestEntity, ShopInfoVO[].class);

            if (resEntity != null && resEntity.getStatusCode().equals(HttpStatus.OK)) {
                for (ShopInfoVO shop : resEntity.getBody()) {
                    if(!shop.getStoreContacts().isEmpty()) {
                        shopList.add(shop);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("ShopGeoFragment", "error: " + ex.getMessage(), ex);
        }

        return shopList;
    }
}

