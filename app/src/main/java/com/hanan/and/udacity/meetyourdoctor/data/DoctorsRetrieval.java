package com.hanan.and.udacity.meetyourdoctor.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;

import java.util.ArrayList;
import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ARABIC;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.AR_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EN_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;

public class DoctorsRetrieval {
    private Context context;
    private static final String DOCTORS_NODE = "doctors";
    private ArrayList<Doctor> doctors;

    public DoctorsRetrieval(){}

    public DoctorsRetrieval(final Context context){
        this.context = context;
    }

    public List<Doctor> getDoctorsBySpecialist(String specialistName){
        return getDoctors();
    }

    public List<Doctor> getFavouriteDoctors(){
        List<Doctor> doctors = new ArrayList<>();

        Doctor doc1 = new Doctor();
        doc1.setName("Amina Gaber");
        doc1.setSpecialist(new Specialist(null, "New Born", null, null));
//        doc1.setSpecialist("New Born");
        doc1.setStudy("PHD at Dermatology");
        doc1.setFees("50 EGP");
        doc1.setTimes("From 5 pm to 10 pm except Friday");
        doc1.setDays("Every day except Friday");
        doc1.setAddress("Ashmon, El-Menoufia");
        doc1.setServices("Service1, Service2");
        doc1.setCity("Menouf");
        doc1.setRating(2f);

        Doctor doc2 = new Doctor();
        doc2.setName("Amany Sabry");
        doc2.setSpecialist(new Specialist(null, "New Born", null, null));
//        doc2.setSpecialist("New Born");
        doc2.setStudy("PHD at Pediatrics");
        doc2.setFees("50 EGP");
        doc2.setTimes("From 5 pm to 10 pm except Friday");
        doc2.setDays("Every day except Friday");
        doc2.setAddress("Sirs-Ellayan, El-Menoufia");
        doc2.setServices("Service1, Service2");
        doc2.setCity("Sirs El-Layan");
        doc2.setRating(4f);

        doctors.add(doc1);
        doctors.add(doc2);

        return doctors;
    }

    //some dummy data
    public List<Doctor> getDoctors(){
        List<Doctor> doctors = new ArrayList<>();

        Doctor doc1 = new Doctor();
        doc1.setName("Amina Gaber");
        doc1.setSpecialist(new Specialist(null, "New Born", null, null));
//        doc1.setSpecialist("New Born");
        doc1.setStudy("PHD at Dermatology");
        doc1.setFees("50 EGP");
        doc1.setTimes("From 5 pm to 10 pm");
        doc1.setDays("Every day except Friday");
        doc1.setAddress("Ashmon, El-Menoufia");
        doc1.setServices("Service1, Service2");
        doc1.setCity("Menouf");
        doc1.setRating(3.5f);

        Doctor doc2 = new Doctor();
        doc2.setName("Amany Sabry");
        doc2.setSpecialist(new Specialist(null, "New Born", null, null));
//        doc2.setSpecialist("New Born");
        doc2.setStudy("PHD at Pediatrics");
        doc2.setFees("50 EGP");
        doc2.setTimes("From 5 pm to 10 pm except Friday");
        doc2.setDays("Every day except Friday");
        doc2.setAddress("Sirs-Ellayan, El-Menoufia");
        doc2.setServices("Service1, Service2");
        doc2.setCity("Sirs El-Layan");
        doc2.setRating(2.5f);

        doctors.add(doc1);
        doctors.add(doc2);
        doctors.add(doc1);
        doctors.add(doc2);
        doctors.add(doc1);
        doctors.add(doc2);

        return doctors;
    }
}
