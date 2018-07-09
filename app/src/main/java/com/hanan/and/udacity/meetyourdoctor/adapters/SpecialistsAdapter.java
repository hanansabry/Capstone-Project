package com.hanan.and.udacity.meetyourdoctor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;

import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ARABIC;

/**
 * Created by Nono on 6/11/2018.
 */

public class SpecialistsAdapter extends RecyclerView.Adapter<SpecialistsAdapter.SpecialistViewHolder> {
    private Context mContext;
    private List<Specialist> mSpecialistList;
    private SpecialistAdapterCallback mCallback;

    public interface SpecialistAdapterCallback {
        void onSpecialistClick(int position);
    }

    public SpecialistsAdapter(Context context, List<Specialist> specialistList, SpecialistAdapterCallback callback) {
        mContext = context;
        mSpecialistList = specialistList;
        mCallback = callback;
    }

    @Override
    public SpecialistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialist_item, parent, false);
        return new SpecialistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpecialistViewHolder holder, int position) {
        Specialist specialist = mSpecialistList.get(position);
        holder.specialistName.setText(specialist.getName());
        Glide.with(mContext).load(specialist.getIconUrl())
                .placeholder(R.drawable.ic_specialist_placeholder)
                .into(holder.specialistIcon);
    }

    @Override
    public int getItemCount() {
        return mSpecialistList.size();
    }

    class SpecialistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView specialistIcon;
        TextView specialistName;

        public SpecialistViewHolder(View itemView) {
            super(itemView);
            specialistIcon = itemView.findViewById(R.id.specialist_icon);
            specialistName = itemView.findViewById(R.id.specialist_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCallback.onSpecialistClick(getAdapterPosition());
        }
    }
}
