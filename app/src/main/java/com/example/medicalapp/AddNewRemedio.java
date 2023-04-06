package com.example.medicalapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.medicalapp.database.RemedioDAO;
import com.example.medicalapp.model.RemedioModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewRemedio extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private RemedioDAO dao = new RemedioDAO();
    private EditText newRemedioText;
    private Button newRemedioSaveButton;

    public static AddNewRemedio newInstance(){
        return new AddNewRemedio();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_remedio, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newRemedioText = requireView().findViewById(R.id.newRemedioText);
        newRemedioSaveButton = getView().findViewById(R.id.newRemedioButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("remedio");
            newRemedioText.setText(task);
            assert task != null;
            if(task.length()>0)
                newRemedioText.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
        }


        newRemedioText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newRemedioSaveButton.setEnabled(false);
                    newRemedioSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newRemedioSaveButton.setEnabled(true);
                    newRemedioSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newRemedioSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newRemedioText.getText().toString();
                if(finalIsUpdate){
                   // db.updateTask(bundle.getInt("id"), text);
                }
                else {
                    RemedioModel task = new RemedioModel();
                    task.setRemedio(text);
                    task.setStatus(0);
                    dao.create(task);
                    //db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
