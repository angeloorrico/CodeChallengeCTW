package br.com.angeloorrico.ctw.codechallengectw.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.angeloorrico.ctw.codechallengectw.R;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    public static final int TYPE_LIST     = 0;
    public static final int TYPE_FAVORITE = 1;

    private ArrayList<LocationModel> locations;
    private int type;

    int selectedPosition = -1;

    public LocationAdapter(ArrayList<LocationModel> locations, int type) {
        this.locations = locations;
        this.type = type;
    }

    private OnItemClickListener listener;

    @NonNull
    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new  LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder holder, int position) {
        if (type == TYPE_LIST) {
            holder.tvLabel.setText(Html.fromHtml(locations.get(position).getLabel()));
            holder.tvDistance.setText(locations.get(position).getDistance() + "m");
        } else {
            holder.tvLabel.setText(locations.get(position).getPlainLabel());
            holder.tvDistance.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }


    public class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView tvLabel, tvDistance;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLabel = itemView.findViewById(R.id.tv_label);
            tvDistance = itemView.findViewById(R.id.tv_distance);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();

                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(locations.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(LocationModel location);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}