package org.nuvola.mobile.prixpascher.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.nuvola.mobile.prixpascher.dto.OfferVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class OfferFetchTask extends AsyncTask<Void, Void, OfferVO> {
	private String url;
	private Handler mHandler;
	public String TAG = "OfferFetchTask";
	public OfferVO keyResponse;

	public OfferFetchTask(String url, Handler mHandler) {
		this.url = url;
		this.mHandler = mHandler;
        this.keyResponse = null;
	}

	public OfferFetchTask(String url, Handler mHandler, OfferVO keyResponse) {
		this.url = url;
		this.mHandler = mHandler;
		this.keyResponse = keyResponse;
	}

	@Override
	protected OfferVO doInBackground(Void... params) {
		HttpHeaders requestHeaders = new HttpHeaders();

		// Sending a JSON or XML object i.e. "application/json" or "application/xml"
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		// Populate the Message object to serialize and headers in an
		// HttpEntity object to use for the request
		HttpEntity<SearchFilterVO> requestEntity = new HttpEntity<>(requestHeaders);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		ResponseEntity<OfferVO> product = restTemplate.exchange(url,
				HttpMethod.GET,
				requestEntity, OfferVO.class);

        keyResponse = product.getBody();
		return keyResponse;
	}

	@Override
	protected void onPostExecute(OfferVO result) {
		// TODO Auto-generated method stub
		try {
			Bundle bundle = new Bundle();
            bundle.putSerializable(this.keyResponse.getOfferId(), result);
			Message msg = new Message();
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ERR", e.toString());
		}
	}
}
