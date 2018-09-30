package com.example.vibhu.assignment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    ArrayList<Details> arrayList;
    Context context;
    private DatabaseReference mDatabase;

    public CustomAdapter(ArrayList<Details> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        mDatabase= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Details getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(R.layout.rowlayout, viewGroup, false);
            ViewHolder holder=new ViewHolder();
            holder.titleTextView=view1.findViewById(R.id.locname);
            holder.titlebutton=view1.findViewById(R.id.titlebutton);
            view1.setTag(holder);
        }
        ViewHolder holder1=(ViewHolder) view1.getTag();
        final Details item=getItem(i);
        holder1.titleTextView.setText(item.getName());
        holder1.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Details of the location:\n\n  Address: "+item.getArea()+"\n\n  Latitude: "+item.getLatitude()+"\n\n  Longitude: "+item.getLongitude()+"\n");
                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });
        holder1.titlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MapsActivity.class);
                intent.putExtra("lat",item.getLatitude());
                intent.putExtra("lon",item.getLongitude());
                intent.putExtra("name",item.getName());
                intent.putExtra("type","1");
                intent.putExtra("area",item.getArea());
                context.startActivity(intent);
            }
        });
        return view1;
    }
    public class ViewHolder{
        TextView titleTextView;
        Button titlebutton;

    }
}
