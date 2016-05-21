package net.montoyo.mcef.client;

import org.cef.callback.CefQueryCallback;

import net.montoyo.mcef.api.IJSQueryCallback;

public class QueryCallback implements IJSQueryCallback {

	private CefQueryCallback cb;

	public QueryCallback(CefQueryCallback cb) {
		this.cb = cb;
	}

	@Override
	public void failure(int errId, String errMsg) {
		cb.failure(errId, errMsg);
	}

	@Override
	public void success(String response) {
		cb.success(response);
	}

}
