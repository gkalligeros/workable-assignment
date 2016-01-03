package task3;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
	public static final int PAGE_LIMIT = 50;

	public static void main(String[] args) throws IOException, SQLException {
		API_getter api = new API_getter();
		HtmlScraper scraper = new HtmlScraper();
		List<Movie> Movies = new ArrayList<Movie>();
		JSONObject response_json = null;
		JSONArray movies_json = null;
		Map<String, String> Actors_descriptions = new HashMap<String, String>();

		int movies_total = 0, pages = 0;

		System.out.println("Getting Movies");
		// get first page and total movies number
		String response = api.get_movies_in_theaters(PAGE_LIMIT, 1);
		try {
			response_json = new JSONObject(response);
			movies_total = (int) response_json.get("total");
			movies_json = response_json.getJSONArray("movies");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pages = (int) Math.ceil((((double) movies_total / PAGE_LIMIT)));

		// get the rest of the pages
		for (int i = 2; i <= pages; i++) {
			response = api.get_movies_in_theaters(PAGE_LIMIT, i);
			try {
				response_json = new JSONObject(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONArray temp = response_json.getJSONArray("movies");
				for (int i1 = 0; i1 < temp.length(); i1++) {

					movies_json.put(temp.get(i1));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// get the required attributes from json
		System.out.println("Searching Wikipedia");
		for (int i = 0; i < movies_json.length(); i++) {
			Movie temp_movie = null;
			try {
				 temp_movie = new Movie(Integer.parseInt(movies_json.getJSONObject(i).get("year").toString()),(String) movies_json.getJSONObject(i).get("synopsis"),movies_json.getJSONObject(i).get("title").toString());
				
				JSONArray temp_actors = movies_json.getJSONObject(i).getJSONArray("abridged_cast");
				
				for (int j = 0; j < temp_actors.length(); j++) {
					// must check if we already searched wikipedia for that
					// actor
					if (!Actors_descriptions.containsKey(temp_actors.getJSONObject(j).get("name").toString())) {
						Actors_descriptions.put(temp_actors.getJSONObject(j).get("name").toString(),
								scraper.get_first_paragraph(temp_actors.getJSONObject(j).get("name").toString()));
					}
					temp_movie.getActors().add(temp_actors.getJSONObject(j).get("name").toString());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Movies.add(temp_movie);
		}
		System.out.println("Updating DB");

		DB database = new DB();
		for (int i = 0; i < Movies.size(); i++) {
			database.save_film(Movies.get(i).getName(), Movies.get(i).getYear()+"" );
		}
		for (String key : Actors_descriptions.keySet()) {
			database.save_actor(key, Actors_descriptions.get(key));
		}
		for (int i = 0; i < Movies.size(); i++) {
			for (int j = 0; j < Movies.get(i).getActors().size(); j++) {
				database.add_film_actor_conection(Movies.get(i).getName(),
						Movies.get(i).getActors().get(j));
			}
		}
		System.out.println("Exiting");

	}

}
