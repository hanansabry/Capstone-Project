package com.hanan.and.udacity.meetyourdoctor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanan.and.udacity.meetyourdoctor.R;

import java.util.List;

/**
 * Created by Nono on 6/11/2018.
 */

public class SpecialistsAdapter extends RecyclerView.Adapter<SpecialistsAdapter.SpecialistViewHolder> {
    Context mContext;
    List<String> mSpecialistList;

    public SpecialistsAdapter(Context context, List<String> specialistList) {
        mContext = context;
        mSpecialistList = specialistList;
    }

    @Override
    public SpecialistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialist_item, parent, false);
        return new SpecialistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpecialistViewHolder holder, int position) {
        holder.specialistIcon.setImageResource(R.drawable.dermatology);
        holder.specialistName.setText(mSpecialistList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSpecialistList.size();
    }

    class SpecialistViewHolder extends RecyclerView.ViewHolder {
        ImageView specialistIcon;
        TextView specialistName;

        public SpecialistViewHolder(View itemView) {
            super(itemView);
            specialistIcon = itemView.findViewById(R.id.specialist_icon);
            specialistName = itemView.findViewById(R.id.specialist_name);
        }
    }
}
