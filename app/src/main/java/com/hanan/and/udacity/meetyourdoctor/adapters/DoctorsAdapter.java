package com.hanan.and.udacity.meetyourdoctor.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.activities.DoctorProfile;

import java.util.List;

/**
 * Created by Nono on 6/11/2018.
 */

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {
    Context mContext;
    List<String> mDoctorsList;

    public DoctorsAdapter(Context context, List<String> doctorsList) {
        mContext = context;
        mDoctorsList = doctorsList;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_list_item, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        holder.doctorName.setText(mDoctorsList.get(position));
        holder.ratingBar.setRating(3f);
    }

    @Override
    public int getItemCount() {
        return mDoctorsList.size();
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView doctorName, doctorSpecialist;
        RatingBar ratingBar;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctor_name);
            doctorSpecialist = itemView.findViewById(R.id.doctor_specialist);
            ratingBar = itemView.findViewById(R.id.rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DoctorProfile.class);
            mContext.startActivity(intent);
        }
    }
}
