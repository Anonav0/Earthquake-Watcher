package app.maps.earthquakewatcher.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.maps.earthquakewatcher.Model.EarthQuake;
import app.maps.earthquakewatcher.R;
import app.maps.earthquakewatcher.Util.Constants;

public class QuakesListActivity extends AppCompatActivity {

    private ArrayList<String> arrayList;
    private ListView listView;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;

    private List<EarthQuake> earthQuakeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quakes_list);

        earthQuakeList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        queue = Volley.newRequestQueue(this);


        arrayList = new ArrayList<>();

        getAllQuakes(Constants.URL);
    }
    public void getAllQuakes(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                EarthQuake earthQuake = new EarthQuake();
                try {
                    JSONArray features = response.getJSONArray("features");

                    for ( int i=0; i< features.length(); i++) {

                        //getting the coordinates over here.
                        JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                        //getting coordinates
                        JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");

                        //get coordinate array
                        JSONArray coordinates = geometry.getJSONArray("coordinates");

                        double lon = coordinates.getDouble(0);
                        double lat = coordinates.getDouble(1);

                        //SetUp EarthQuake objects
                        earthQuake.setLon(lon);
                        earthQuake.setLat(lat);
                        earthQuake.setPlace(properties.getString("place"));
                        earthQuake.setTime(properties.getLong("time"));
                        earthQuake.setType(properties.getString("type"));

                        arrayList.add(earthQuake.getPlace());
                    }

                    arrayAdapter = new ArrayAdapter<>(QuakesListActivity.this, android.R.layout.simple_list_item_1,
                                android.R.id.text1, arrayList);
                    listView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
}
