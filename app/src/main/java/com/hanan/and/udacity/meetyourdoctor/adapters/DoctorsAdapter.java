package com.hanan.and.udacity.meetyourdoctor.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.activities.DoctorProfile;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;

import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTOR;

/**
 * Created by Nono on 6/11/2018.
 */

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {
    Context mContext;
    List<Doctor> mDoctorsList;

    public DoctorsAdapter(Context context, List<Doctor> doctorsList) {
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
        Doctor doctor = mDoctorsList.get(position);
        holder.doctorName.setText(doctor.getName());
        holder.doctorStudy.setText(doctor.getStudy());
        holder.ratingBar.setRating(doctor.getRating());
        holder.fees.setText(doctor.getFees());
        holder.address.setText(doctor.getAddress());
        holder.days.setText(doctor.getDays());
        holder.times.setText(doctor.getTimes());
        holder.ratingBar.setRating(doctor.getRating());
    }

    @Override
    public int getItemCount() {
        return mDoctorsList.size();
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView doctorName, doctorStudy, fees, address, days, times;
        RatingBar ratingBar;
        Button callNow;

        DoctorViewHolder(View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctor_name);
            doctorStudy = itemView.findViewById(R.id.doctor_study);
            ratingBar = itemView.findViewById(R.id.rating);
            callNow = itemView.findViewById(R.id.call_button);
            fees = itemView.findViewById(R.id.fees_tv);
            address = itemView.findViewById(R.id.address_tv);
            days = itemView.findViewById(R.id.days_tv);
            times = itemView.findViewById(R.id.times_tv);
            itemView.setOnClickListener(this);

            callNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Call now on this number : 01034344829", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DoctorProfile.class);
            //pass doctor object to DoctorProfile Activity
            intent.putExtra(DOCTOR, mDoctorsList.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}
