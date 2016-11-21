package org.itri.ccma.tarsan.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RetorfitController {
	Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.create();
	
}
