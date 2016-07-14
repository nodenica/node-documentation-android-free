package com.mc.nad.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.nad.R;
import com.mc.nad.models.ModuleLocalModel;
import com.mc.nad.ui.activities.ModuleActivity;

import io.realm.RealmResults;

public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.ViewHolder> {
    private RealmResults<ModuleLocalModel> moduleLocalModels;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Context context;
        public TextView title;
        public LinearLayout itemContainer;
        public ViewHolder(Context context, View view) {
            super(view);
            this.context = context;
            this.title = (TextView) view.findViewById(R.id.title);
            this.itemContainer = (LinearLayout) view.findViewById(R.id.item_container);
        }
    }

    // Provide a suitable constructor
    public ApiAdapter(RealmResults<ModuleLocalModel> moduleLocalModels) {
        this.moduleLocalModels = moduleLocalModels;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ApiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_api, parent, false);
        return new ViewHolder(parent.getContext(), view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ModuleLocalModel moduleLocalModel = moduleLocalModels.get(position);
        holder.title.setText(moduleLocalModel.getTitle());

        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, ModuleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ModuleActivity.EXTRA_TITLE, moduleLocalModel.getTitle());
                intent.putExtra(ModuleActivity.EXTRA_NAME, moduleLocalModel.getName());
                holder.context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return moduleLocalModels.size();
    }
}