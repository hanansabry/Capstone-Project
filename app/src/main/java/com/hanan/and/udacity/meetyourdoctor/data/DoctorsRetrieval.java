package com.hanan.and.udacity.meetyourdoctor.data;

import android.content.Context;

import com.hanan.and.udacity.meetyourdoctor.model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorsRetrieval {
    private Context context;

    public DoctorsRetrieval(){}

    public DoctorsRetrieval(Context context){
        this.context = context;
    }

    public List<Doctor> getDoctorsBySpecialist(String specialistName){
        return getDoctors();
    }

    public List<Doctor> getFavouriteDoctors(){
        List<Doctor> doctors = new ArrayList<>();

        Doctor doc1 = new Doctor();
        doc1.setDoctorName("Amina Gaber");
        doc1.setSpecialist("Dermatology");
        doc1.setStudy("PHD at Dermatology");
        doc1.setFees("50 EGP");
        doc1.setClinicTimes("From 5 pm to 10 pm except Friday");
        doc1.setClinicDays("Every day except Friday");
        doc1.setAddress("Ashmon, El-Menoufia");
        doc1.setClinicServices(new String[]{"service1", "service2"});
        doc1.setCity("Menouf");
        doc1.setRating(2f);

        Doctor doc2 = new Doctor();
        doc2.setDoctorName("Amany Sabry");
        doc2.setSpecialist("Pediatrics and New Born");
        doc2.setStudy("PHD at Pediatrics");
        doc2.setFees("50 EGP");
        doc2.setClinicTimes("From 5 pm to 10 pm except Friday");
        doc2.setClinicDays("Every day except Friday");
        doc2.setAddress("Sirs-Ellayan, El-Menoufia");
        doc2.setClinicServices(new String[]{"service1", "service2"});
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
        doc1.setDoctorName("Amina Gaber");
        doc1.setSpecialist("Dermatology");
        doc1.setStudy("PHD at Dermatology");
        doc1.setFees("50 EGP");
        doc1.setClinicTimes("From 5 pm to 10 pm");
        doc1.setClinicDays("Every day except Friday");
        doc1.setAddress("Ashmon, El-Menoufia");
        doc1.setClinicServices(new String[]{"service1", "service2"});
        doc1.setCity("Menouf");
        doc1.setRating(3.5f);

        Doctor doc2 = new Doctor();
        doc2.setDoctorName("Amany Sabry");
        doc2.setSpecialist("Pediatrics and New Born");
        doc2.setStudy("PHD at Pediatrics");
        doc2.setFees("50 EGP");
        doc2.setClinicTimes("From 5 pm to 10 pm except Friday");
        doc2.setClinicDays("Every day except Friday");
        doc2.setAddress("Sirs-Ellayan, El-Menoufia");
        doc2.setClinicServices(new String[]{"service1", "service2"});
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
