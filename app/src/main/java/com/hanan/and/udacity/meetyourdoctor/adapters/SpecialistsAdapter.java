package com.hanan.and.udacity.meetyourdoctor.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.fragments.DoctorsFragment;

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
        if(position == 0){
            holder.specialistIcon.setImageResource(R.drawable.ic_dermatology);
        }else if(position == 1){
            holder.specialistIcon.setImageResource(R.drawable.ic_dentistry);
        }else if(position == 2){
            holder.specialistIcon.setImageResource(R.drawable.ic_psychiatry);
        }else if(position == 3){
            holder.specialistIcon.setImageResource(R.drawable.ic_newborn);
        }else if(position == 4){
            holder.specialistIcon.setImageResource(R.drawable.ic_neurology);
        }else if(position == 5){
            holder.specialistIcon.setImageResource(R.drawable.ic_orthopedics);
        }else if(position == 6){
            holder.specialistIcon.setImageResource(R.drawable.ic_gynaecology);
        }else if(position == 7){
            holder.specialistIcon.setImageResource(R.drawable.ic_earnose);
        }else if(position == 8){
            holder.specialistIcon.setImageResource(R.drawable.ic_cardiology);
        }else if(position == 9){
            holder.specialistIcon.setImageResource(R.drawable.ic_allergy);
        }



        holder.specialistName.setText(mSpecialistList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSpecialistList.size();
    }

    class SpecialistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            DoctorsFragment doctorsFragment = DoctorsFragment.newInstance();
            FragmentTransaction transaction = ((FragmentActivity)(mContext)).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, doctorsFragment).addToBackStack("search_fragment");
            transaction.commit();
        }
    }
}
