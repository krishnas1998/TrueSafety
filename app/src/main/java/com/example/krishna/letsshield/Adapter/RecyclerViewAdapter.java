package com.example.krishna.letsshield.Adapter;

/**
 * Created by krishna on 31/8/17.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.krishna.letsshield.R;

import java.util.List;

/**
 * Created by Juned on 3/27/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyView> {

    private List<String> list,helperList;
    private  int i =0;
    private Context mContext;

    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView textview1;
        public TextView textView3;
        public TextView textview4

        public MyView(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.textview1);
            cardView = (CardView)view.findViewById(R.id.cardview);

        }
    }


    public RecyclerViewAdapter(Context context, List<String> horizontalList, List<String> otherList) {
        this.list = horizontalList;
        helperList = otherList;
        mContext = context;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_layout, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        holder.textView.setText(list.get(position));
        if (helperList ==null||helperList.size()==0)
            return;
        else
        {
            //Toast.makeText(mContext,helperList.get(i),Toast.LENGTH_SHORT).show();
           for (int j = 0; j<helperList.size();j++)
            if (list.get(position).equals(helperList.get(j)))
            {
                //i++;
                //holder.textView.setText("Match");
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext,android.R.color.background_dark));
                //break;
               // holder.textView.setBackgroundColor(ContextCompat.getColor(mContext,android.R.color.background_light));
            }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}