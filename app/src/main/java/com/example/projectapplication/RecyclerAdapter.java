package com.example.projectapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<Calllogs> calllogsList;

    public RecyclerAdapter(Context context, List<Calllogs> calllogs) {
        this.context = context;
        calllogsList = calllogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.datalayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TextView contact, duration, time, type;

        contact = holder.contact;
        duration = holder.duration;
        time = holder.time;
        type = holder.type;

        contact.setText(calllogsList.get(position).getNumber());
        duration.setText(calllogsList.get(position).getDuration());
        time.setText(calllogsList.get(position).getTime());
        type.setText(calllogsList.get(position).getType());

        String timeStr = time.getText().toString();
        final String[] times = timeStr.split(" ");

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyXWSvct4NraanheJad2nrGp752R3GV8Rqk3QQHgKsTsVfG59rV/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Re.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action","addItem");
                params.put("date",times[0]);
                params.put("time",times[1]);
                params.put("phone",contact.getText().toString());
                params.put("status", type.getText().toString());
                params.put("duration", duration.getText().toString());
                return params;
            }
        };
        int socketTimeOut = 1000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return calllogsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contact, duration, time, type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contact = itemView.findViewById(R.id.number);
            duration = itemView.findViewById(R.id.time);
            time = itemView.findViewById(R.id.recording);
            type = itemView.findViewById(R.id.type);
        }
    }
}
