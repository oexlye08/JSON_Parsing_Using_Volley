package id.web.jsonparsingusingvolley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_EMPLOYEE_ID = "employee_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOB = "dob";
    private static final String KEY_DESIGNATION = "designation";
    private static final String KEY_CONTACT_NUMBER = "contact_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_SALARY = "salary";
    private static final String NEW_LINE ="\n\n";

    private TextView mTxtDisplay;
    private ProgressDialog pDialog;

    private String obj_url= "http://api.androiddeft/volley/json_object.json";
    private String array_url = "http://api.androiddeft.com/volley/json_array.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtDisplay= (TextView) findViewById(R.id.txtDisplay);
        Button buttonObj = (Button) findViewById(R.id.btnObj);
        Button buttonArray = (Button) findViewById(R.id.btnArray);
        buttonObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayLoader();
                loadJsonObj();
            }
        });
        buttonArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayLoader();
                loadJsonArray();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading Data... Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void loadJsonObj(){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, obj_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    //PARSE THE JSON RESPONSE
                    Integer employeeID = response.getInt(KEY_EMPLOYEE_ID);
                    String name = response.getString(KEY_NAME);
                    String dob = response.getString(KEY_DOB);
                    String designation = response.getString(KEY_DESIGNATION);
                    String contactNumber = response.getString(KEY_CONTACT_NUMBER);
                    String email = response.getString(KEY_EMAIL);
                    String salary = response.getString(KEY_SALARY);

                    // CREATE STRING OUT OF THE PARSED JSON
                    StringBuilder textViewData = new StringBuilder().append("Employee Id : ")
                            .append(employeeID.toString()).append(NEW_LINE);
                    textViewData.append("Name : ").append(name).append(NEW_LINE);
                    textViewData.append("Date of Birth : ").append(dob).append(NEW_LINE);
                    textViewData.append("Designation : ").append(designation).append(NEW_LINE);
                    textViewData.append("Contact Number : ").append(contactNumber).append(NEW_LINE);
                    textViewData.append("Email : ").append(email).append(NEW_LINE);
                    textViewData.append("Salary").append(salary).append(NEW_LINE);

                    //POPULATE TEXTVIEW WITH THE RESPONSE
                    mTxtDisplay.setText(textViewData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                //DISPLAY ERROR MESSAGE WHENNEVER AN ERROR OCCURES
                Toast.makeText(getApplicationContext(),
                        error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //ACCESS THE REQUESTQUEUE THROUGHT YOUR  SINGLETON CLASS
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }
    private void loadJsonArray(){
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(
                Request.Method.GET, array_url, null, new Response.Listener<JSONArray>(){


            @Override
            public void onResponse(JSONArray responseArray) {
                pDialog.dismiss();
                try {
                    StringBuilder textViewData = new StringBuilder();

                    //PARSE THE JSON RESPONSE ARRAY BY ITERATING OVER IT
                    for (int i = 0; i<responseArray.length(); i++){
                        JSONObject response = responseArray.getJSONObject(i);
                        Integer employeeId = response.getInt(KEY_EMPLOYEE_ID);
                        String name = response.getString(KEY_NAME);
                        String dob = response.getString(KEY_DOB);
                        String designation = response.getString(KEY_DESIGNATION);
                        String contactNumber = response.getString(KEY_CONTACT_NUMBER);
                        String email = response.getString(KEY_EMAIL);
                        String salary = response.getString(KEY_SALARY);

                        //CREATER STRING OUT OF THE PARSed JSON

                        textViewData.append("Employee ID : ").append(employeeId.toString()).append(NEW_LINE);
                        textViewData.append("Name : ").append(name).append(NEW_LINE);
                        textViewData.append("Date of Birth : ").append(dob).append(NEW_LINE);
                        textViewData.append("Designation : ").append(designation).append(NEW_LINE);
                        textViewData.append("Contact Number : ").append(contactNumber).append(NEW_LINE);
                        textViewData.append("Email : ").append(email).append(NEW_LINE);
                        textViewData.append("Salary : ").append(salary).append(NEW_LINE);
                        textViewData.append(NEW_LINE);
                    }

                    //POPULATE TEXTVIEW WITH THE RESPONSE
                    mTxtDisplay.setText(textViewData.toString());
                }   catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                //Display Eror message whenever an error occurs
                Toast.makeText(getApplicationContext(),
                        error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        );

        //Access the RequestQueue throught your singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);

    }
}
