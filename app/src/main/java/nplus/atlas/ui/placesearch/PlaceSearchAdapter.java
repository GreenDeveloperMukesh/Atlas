package nplus.atlas.ui.placesearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import nplus.atlas.retro.responsemodel.PredictionModel;

import java.util.List;

public class PlaceSearchAdapter extends RecyclerView.Adapter<PlaceSearchAdapter.MyViewHolder> {
    private List<PredictionModel.PredictedData> mPreditionList;
    private PlaceSearchScreen activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    public PlaceSearchAdapter(List<PredictionModel.PredictedData> placeList, PlaceSearchScreen placeSearchScreen) {
        mPreditionList = placeList;
        activity = placeSearchScreen;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaceSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mPreditionList.get(position).description);
        holder.textView.setTag(mPreditionList.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() != null && activity != null && view.getTag() instanceof PredictionModel.PredictedData) {
                    activity.onItemClicked((PredictionModel.PredictedData) view.getTag());
                }
            }
        });

    }

    public void addAllItems(List<PredictionModel.PredictedData> predictedDataList) {
        this.mPreditionList = predictedDataList;
        notifyDataSetChanged();
    }

    public void clearAllItems() {
        this.mPreditionList.clear();
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPreditionList.size();
    }
}
