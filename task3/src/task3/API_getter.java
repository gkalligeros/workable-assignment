package task3;
import java.io.IOException;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class API_getter {
	OkHttpClient client = new OkHttpClient();
	String url ="http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=qtqep7qydngcc7grk4r4hyd9&page_limit=50&page=1";
	
	public String get_movies_in_theaters(int page_limit, int page) throws IOException {
		String url ="http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=qtqep7qydngcc7grk4r4hyd9&page_limit="+page_limit+"&page="+page;
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

}
