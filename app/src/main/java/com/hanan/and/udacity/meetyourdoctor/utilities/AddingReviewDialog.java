package com.hanan.and.udacity.meetyourdoctor.utilities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.hanan.and.udacity.meetyourdoctor.R;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ANONYMOUS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;

public class AddingReviewDialog extends DialogFragment {

    public interface AddReviewDialogListener {
        void onFinishAddingReview(String reviewerName, String reviewContent, Float reviewRate);
    }

    public AddingReviewDialog() {
    }

    public static AddingReviewDialog newInstance(String username) {
        AddingReviewDialog dialog = new AddingReviewDialog();
        Bundle b = new Bundle();
        b.putString(USER, username);
        dialog.setArguments(b);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.review_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = getArguments().getString(USER, "");

        getDialog().setTitle(getString(R.string.add_review));
        final EditText reviewerEditText = view.findViewById(R.id.reviewer_name);
        final EditText reviewContentET = view.findViewById(R.id.review);
        final RatingBar ratingBar = view.findViewById(R.id.reviewer_rating);
        Button addButton = view.findViewById(R.id.add_review);

        reviewerEditText.setText(username);

        final String reviewerName = reviewerEditText.getText().toString().equals("") ? ANONYMOUS : reviewerEditText.getText().toString();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReviewDialogListener listener = (AddReviewDialogListener) getActivity();
                listener.onFinishAddingReview(reviewerName, reviewContentET.getText().toString(), ratingBar.getRating());
                dismiss();
            }
        });
    }
}
