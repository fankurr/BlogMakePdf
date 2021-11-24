package com.logicchip.blog_make_pdf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akhil Ashok
 * akhilashok123@gmail.com
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<ListItem> list;
    private Context context;
    public ListAdapter(Context context, ArrayList<ListItem> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView vwName,vwAmount,vwDate,vwMessage;
        public ViewHolder(View itemView) {
            super(itemView);
            vwName=itemView.findViewById(R.id.vwName);
            vwAmount=itemView.findViewById(R.id.vwAmount);
            vwDate=itemView.findViewById(R.id.vwDate);
            vwMessage=itemView.findViewById(R.id.vwMessage);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ListItem listItem=list.get(position);

        holder.vwName.setText(listItem.getName());
        holder.vwAmount.setText("$"+listItem.getAmount());
        holder.vwDate.setText(listItem.getDate());
        holder.vwMessage.setText(listItem.getCompany());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


