package com.javatpoint.user.swipedeleteinrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout regularLayout;
    public LinearLayout swipeLayout;
    public TextView listItem;
    public TextView undo;

    public ItemViewHolder(View view) {
        super(view);

        regularLayout = view.findViewById(R.id.regularLayout);
        listItem =  view.findViewById(R.id.list_item);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        undo =  view.findViewById(R.id.undo);
    }
}
