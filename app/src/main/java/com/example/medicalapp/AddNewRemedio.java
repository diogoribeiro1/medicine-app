package com.example.medicalapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.medicalapp.database.RemedioDAO;
import com.example.medicalapp.model.RemedioModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;
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
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public static AddNewRemedio newInstance(){
        return new AddNewRemedio();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            String alarme = bundle.getString("alarme");

            newRemedioText.setText(nome);
            newDoseText.setText(dose);
            newFrequenciaText.setText(frequencia);
            newHorarioText.setText(horario);
            timeButton.setText(alarme);

            alarmeFormatado = alarme;

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
        newRemedioSaveButton.setOnClickListener(v -> {
            String nome = newRemedioText.getText().toString();
            String dose = newDoseText.getText().toString();
            String frequencia = newFrequenciaText.getText().toString();
            String horarios = newHorarioText.getText().toString();

            RemedioModel model = new RemedioModel();
            model.setRemedio(nome);
            model.setDose(dose);
            model.setFrequencia(frequencia);
            model.setHorarios(horarios);
            model.setAlarme(alarmeFormatado);
            model.setStatus(0);

            if(finalIsUpdate){

                String[] partes = model.getAlarme().split(":");
                int horas = Integer.parseInt(partes[0]);
                int minutos = Integer.parseInt(partes[1]);

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, horas);
                calendar.set(Calendar.MINUTE, minutos);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

               dao.update(model);
            }
            else {
                dao.create(model);
            }
            scheduleNotification();
            dismiss();
        });

        timeButton.setOnClickListener(view1 -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
                hour = selectedHour;
                minute = selectedMinute;
                alarmeFormatado = String.format(Locale.getDefault(), "%02d:%02d",hour, minute);
                timeButton.setText(alarmeFormatado);

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

            timePickerDialog.setTitle("Selecione o horario");
            timePickerDialog.show();
        });
    }

    private void scheduleNotification() {
        Intent intent = new Intent(getContext(), Notification.class);
        intent.putExtra("titleExtra", "Alarme");
        intent.putExtra("messageExtra", "Hora de tomar o remedio");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                1,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
        Toast.makeText(getContext(), "Alarme pronto", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
