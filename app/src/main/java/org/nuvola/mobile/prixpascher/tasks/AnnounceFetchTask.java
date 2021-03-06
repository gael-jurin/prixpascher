package org.nuvola.mobile.prixpascher.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class AnnounceFetchTask extends AsyncTask<Void, Void, ProductAnnonceVO> {
	private String url;
	private Handler mHandler;
	public String TAG = "AnnounceFetchTask";
	public ProductAnnonceVO keyResponse;

	public AnnounceFetchTask(String url, Handler mHandler) {
		this.url = url;
		this.mHandler = mHandler;
        this.keyResponse = null;
	}

	public AnnounceFetchTask(String url, Handler mHandler, ProductAnnonceVO keyResponse) {
		this.url = url;
		this.mHandler = mHandler;
		this.keyResponse = keyResponse;
	}

	@Override
	protected ProductAnnonceVO doInBackground(Void... params) {
		HttpHeaders requestHeaders = new HttpHeaders();

		// Sending a JSON or XML object i.e. "application/json" or "application/xml"
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		// Populate the Message object to serialize and headers in an
		// HttpEntity object to use for the request
		HttpEntity<SearchFilterVO> requestEntity = new HttpEntity<>(requestHeaders);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		ResponseEntity<ProductAnnonceVO> product = restTemplate.exchange(url,
				HttpMethod.GET,
				requestEntity, ProductAnnonceVO.class);

        keyResponse = product.getBody();
		return keyResponse;
	}

	@Override
	protected void onPostExecute(ProductAnnonceVO result) {
		// TODO Auto-generated method stub
		try {
			Bundle bundle = new Bundle();
            bundle.putSerializable(this.keyResponse.getId(), result);
			Message msg = new Message();
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ERR", e.toString());
		}
	}
}
