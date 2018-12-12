package com.javatpoint.user.swipedeleteinrecyclerview;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<String> dataList;
    private List<String> itemsPendingRemoval;

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnable, to cancel the removal


    public MyAdapter(List<String> dataList) {
        this.dataList = dataList;
        itemsPendingRemoval = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlayout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {

        final String data = dataList.get(position);

        if (itemsPendingRemoval.contains(data)) {
            /** show swipe layout and hide regular layout */
            itemViewHolder.regularLayout.setVisibility(View.GONE);
            itemViewHolder.swipeLayout.setVisibility(View.VISIBLE);
            itemViewHolder.undo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoOpt(data);
                }
            });
        } else {
            /** show regular layout and hide swipe layout*/
            itemViewHolder.regularLayout.setVisibility(View.VISIBLE);
            itemViewHolder.swipeLayout.setVisibility(View.GONE);
            itemViewHolder.listItem.setText(data);
        }
    }

    private void undoOpt(String customer) {
        Runnable pendingRemovalRunnable = pendingRunnables.get(customer);
        pendingRunnables.remove(customer);
        if (pendingRemovalRunnable != null)
            handler.removeCallbacks(pendingRemovalRunnable);
        itemsPendingRemoval.remove(customer);
        // this will rebind the row in "normal" state
        notifyItemChanged(dataList.indexOf(customer));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void pendingRemoval(int position) {

        final String data = dataList.get(position);
        if (!itemsPendingRemoval.contains(data)) {
            itemsPendingRemoval.add(data);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            //create, store and post a runnable to remove the data
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(dataList.indexOf(data));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(data, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        String data = dataList.get(position);
        if (itemsPendingRemoval.contains(data)) {
            itemsPendingRemoval.remove(data);
        }
        if (dataList.contains(data)) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        String data = dataList.get(position);
        return itemsPendingRemoval.contains(data);
    }
}