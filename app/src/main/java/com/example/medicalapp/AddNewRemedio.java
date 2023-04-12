package com.example.medicalapp;

import android.app.Activity;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.medicalapp.database.RemedioDAO;
import com.example.medicalapp.model.RemedioModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;
import java.util.Objects;

public class AddNewRemedio extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private RemedioDAO dao = new RemedioDAO();
    private EditText newRemedioText, newDoseText, newFrequenciaText, newHorarioText;
    private Button newRemedioSaveButton;

    String alarmeFormatado;

    int hour, minute;

    private Button timeButton;

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
        newDoseText = requireView().findViewById(R.id.newDoseText);
        newFrequenciaText = requireView().findViewById(R.id.newFrequenciaText);
        newHorarioText = requireView().findViewById(R.id.newHorarioText);


        newRemedioSaveButton = getView().findViewById(R.id.newRemedioButton);
        timeButton = getView().findViewById(R.id.timePickerButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String nome = bundle.getString("remedio");
            String dose = bundle.getString("dose");
            String frequencia = bundle.getString("frequencia");
            String horario = bundle.getString("horario");

            newRemedioText.setText(nome);
            newDoseText.setText(dose);
            newFrequenciaText.setText(frequencia);
            newHorarioText.setText(horario);

            assert nome != null;
            if(nome.length()>0)
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
                String nome = newRemedioText.getText().toString();
                String dose = newDoseText.getText().toString();
                String frequencia = newFrequenciaText.getText().toString();
                String horarios = newHorarioText.getText().toString();

                if(finalIsUpdate){
                   // db.updateTask(bundle.getInt("id"), text);
                }
                else {
                    RemedioModel model = new RemedioModel();
                    model.setRemedio(nome);
                    model.setDose(dose);
                    model.setFrequencia(frequencia);
                    model.setHorarios(horarios);
                    model.setAlarme(alarmeFormatado);
                    model.setStatus(0);
                    dao.create(model);
                    //db.insertTask(task);
                }
                dismiss();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                    {
                        hour = selectedHour;
                        minute = selectedMinute;
                        alarmeFormatado = String.format(Locale.getDefault(), "%02d:%02d",hour, minute);
                        timeButton.setText(alarmeFormatado);
                    }
                };

                // int style = AlertDialog.THEME_HOLO_DARK;

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

                timePickerDialog.setTitle("Selecione o horario");
                timePickerDialog.show();
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