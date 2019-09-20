package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductsActivity extends BaseActivity {

    ImageView imageView2;
    private ArrayList<Item> items;
    private ArrayList<Item> items2;
    private RecyclerView recyclerView;
    SnapRecyclerAdapter adapter;
    SnapRecyclerAdapter2 adapter2;
    SweetAlertDialog pDialog;
//    SwipeRefreshLayout mSwipeRefreshLayout;
    int products_intent;
    TextView textView1;
    private LabeledSwitch swToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        items=new ArrayList<>();
        items2=new ArrayList<>();

        textView1 = (TextView) findViewById(R.id.textView1234);

        products_intent = getIntent().getIntExtra("Products_Id" , 0);
        Log.d("Products_Value" , ""+products_intent);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swToggle = findViewById(R.id.iv_toggle);



        imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProductsActivity.this  ,Main_MenuScreen.class );
                startActivity(intent);
            }
        });


        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);





//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                updatemenu();
//            }
//        });


        swToggle.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                // Implement your switching logic here

                if (isOn)
                {
                    showdatainarabic();
                } else {

                    loadallimages();
                }
            }

        });

        loadallimages();


    }



    public void onBackPressed() {
//        Intent intent = new Intent(Product_Detail.this, ProductsActivity.class);
//        finish();
//        startActivity(intent);
    }


    private void showdatainarabic()
    {

        adapter2 = new SnapRecyclerAdapter2(this, items2);
        adapter2.setDataList(items2);
        recyclerView.setAdapter(adapter2);


        textView1.setText("شهي يقفز في الفم. نحن نخدم العاطفة");
        pDialog = Utilss.showSweetLoader(ProductsActivity.this, SweetAlertDialog.PROGRESS_TYPE, "Fetching Data...");

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(ProductsActivity.this);
        String url = "http://api.surveymenu.dwtdemo.com/api/ar/category/"+products_intent+"/products?pg=1";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Json-response1", response);

                        try {
                            JSONArray jsonArray =new JSONArray(response);

                            items2.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.get(i)));
                                Item dataObject = new Item();

                                dataObject.setID(jsonObject.getInt("ID"));
                                dataObject.setCategory(jsonObject.getString("Category"));
                                dataObject.setProduct(jsonObject.getString("Product"));
                                dataObject.setDescription(jsonObject.getString("Description"));
                                dataObject.setImage(jsonObject.getString("Image"));
                                dataObject.setPrice(jsonObject.getLong("Price"));


                                items2.add(dataObject);
                                adapter2.notifyDataSetChanged();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utilss.hideSweetLoader(pDialog);
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utilss.hideSweetLoader(pDialog);
                    }
                });
            }
        }) {
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer "+MainActivity.value2);
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }


//    private void updatemenu()
//    {
//        pDialog = Utilss.showSweetLoader(ProductsActivity.this, SweetAlertDialog.PROGRESS_TYPE, "Fetching Data...");
//        items2= new ArrayList<>();
//
//        com.android.volley.RequestQueue queue = Volley.newRequestQueue(ProductsActivity.this);
//        String url = "http://api.surveymenu.dwtdemo.com/api/en/category/"+products_intent+"/products?pg=1";
//        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
//                new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.d("Json-response1", response);
//
//                        try {
//                            JSONArray jsonArray =new JSONArray(response);
//
//                            items.clear();
//
//                            for (int i = 0; i < jsonArray.length(); i++)
//                            {
//                                JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.get(i)));
//                                Item dataObject = new Item();
//
//                                dataObject.setID(jsonObject.getInt("ID"));
//                                dataObject.setCategory(jsonObject.getString("Category"));
//                                dataObject.setProduct(jsonObject.getString("Product"));
//                                dataObject.setDescription(jsonObject.getString("Description"));
//                                dataObject.setImage(jsonObject.getString("Image"));
//                                dataObject.setPrice(jsonObject.getLong("Price"));
//
//
//                                items2.add(dataObject);
////                                adapter.notifyDataSetChanged();
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Utilss.hideSweetLoader(pDialog);
//                                    }
//                                });
//
//                            }
//
//
//                            adapter = new SnapRecyclerAdapter(ProductsActivity.this, items2);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this, LinearLayoutManager.HORIZONTAL, false));
//                            recyclerView.setAdapter(adapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Utilss.hideSweetLoader(pDialog);
//                    }
//                });
//            }
//        }) {
//            @Override
//            public Map getHeaders() {
//                HashMap headers = new HashMap();
//                headers.put("Authorization", "Bearer "+MainActivity.value2);
//                return headers;
//            }
//        };
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(stringRequest);
//        mSwipeRefreshLayout.setRefreshing(false);
//
//    }










    private void loadallimages()
    {

        adapter = new SnapRecyclerAdapter(this, items);
        adapter.setDataList(items);
        recyclerView.setAdapter(adapter);

        textView1.setText("Deliciousness jumping into the mouth. We serve passion");
        pDialog = Utilss.showSweetLoader(ProductsActivity.this, SweetAlertDialog.PROGRESS_TYPE, "Fetching Data...");

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(ProductsActivity.this);
        String url = "http://api.surveymenu.dwtdemo.com/api/en/category/"+products_intent+"/products?pg=1";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {







                        Log.d("Json-response1", response);

                        try {
                            JSONArray jsonArray =new JSONArray(response);

                            items.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.get(i)));
                                Item dataObject = new Item();

                                dataObject.setID(jsonObject.getInt("ID"));
                                dataObject.setCategory(jsonObject.getString("Category"));
                                dataObject.setProduct(jsonObject.getString("Product"));
                                dataObject.setDescription(jsonObject.getString("Description"));
                                dataObject.setImage(jsonObject.getString("Image"));
                                dataObject.setPrice(jsonObject.getLong("Price"));


                                items.add(dataObject);
                                adapter.notifyDataSetChanged();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utilss.hideSweetLoader(pDialog);
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utilss.hideSweetLoader(pDialog);
                    }
                });
            }
        }) {
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer "+MainActivity.value2);
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }
}
