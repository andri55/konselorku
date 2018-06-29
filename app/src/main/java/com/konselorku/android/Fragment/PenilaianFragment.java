package com.konselorku.android.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.konselorku.android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PenilaianFragment extends Fragment {

    private TextInputLayout layNama, laySekolah, layLokasi, layKejadian, layKesan;
    private EditText nama, sekolah, lokasi, kejadian, kesan;
    private RadioGroup kelamin, status;
    private Button submit, btnTgl;
    private TextView tgl, hasil;
    private RadioButton kel, stat;
    private SimpleDateFormat df;
    private String dateNow;

    public PenilaianFragment() {
    }

    public static PenilaianFragment newInstance(String param1, String param2) {
        PenilaianFragment fragment = new PenilaianFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_penilaian, container, false);
        layNama = v.findViewById(R.id.layout_nama_penilaian);
        laySekolah = v.findViewById(R.id.layout_sekolah_penilaian);
        layLokasi = v.findViewById(R.id.layout_lokasi_penilaian);
        layKejadian = v.findViewById(R.id.layout_kejadian_penilaian);
        layKesan = v.findViewById(R.id.layout_kesan_penilaian);

        nama = v.findViewById(R.id.nama_penilaian);
        kelamin = v.findViewById(R.id.kelamin_penilaian);
        sekolah = v.findViewById(R.id.sekolah_penilaian);
        status = v.findViewById(R.id.status_penilaian);
        lokasi = v.findViewById(R.id.lokasi_penilaian);
        btnTgl = v.findViewById(R.id.btn_tgl_penilaian);
        tgl = v.findViewById(R.id.tgl_penilaian);
        kejadian = v.findViewById(R.id.kejadian_penilaian);
        kesan = v.findViewById(R.id.kesan_penilaian);
        submit = v.findViewById(R.id.btn_submit_penilaian);
        hasil = v.findViewById(R.id.hasil_penilaian);

        nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > layNama.getCounterMaxLength()) {
                    layNama.setError("Max character length is " + layNama.getCounterMaxLength());
                } else if (s.length() == 0) {
                    layNama.setError("Min character length is 0");
                } else {
                    layNama.setError(null);
                }
            }
        });
        sekolah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    laySekolah.setError("Min character length is 0");
                } else {
                    laySekolah.setError(null);
                }
            }
        });
        lokasi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    layLokasi.setError("Min character length is 0");
                } else {
                    layLokasi.setError(null);
                }
            }
        });
        kejadian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    layKejadian.setError("Min character length is 0");
                } else {
                    layKejadian.setError(null);
                }
            }
        });
        kesan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    layKesan.setError("Min character length is 0");
                } else {
                    layKesan.setError(null);
                }
            }
        });

        df = new SimpleDateFormat("dd-MM-yyyy");
        dateNow = df.format(Calendar.getInstance().getTime());
        tgl.setText(dateNow);

        nama.requestFocus();
        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear = getFromCalendar(tgl.getText().toString(), Calendar.YEAR);
                int mMonth = getFromCalendar(tgl.getText().toString(), Calendar.MONTH);
                int mDay = getFromCalendar(tgl.getText().toString(), Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                tgl.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog loading = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                loading.setTitleText("Loading")
                        .setCancelable(false);
                loading.getProgressHelper()
                        .setBarColor(getResources().getColor(R.color.blue));
                loading.show();
                if (nama.length() == 0) {
                    layNama.setError("Min character length is 0");
                    nama.requestFocus();
                    loading.dismiss();
                } else if (nama.length() > layNama.getCounterMaxLength()) {
                    layNama.setError("Max character length is " + layNama.getCounterMaxLength());
                    nama.requestFocus();
                    loading.dismiss();
                } else if (sekolah.length() == 0) {
                    laySekolah.setError("Min character length is 0");
                    sekolah.requestFocus();
                    loading.dismiss();
                } else if (lokasi.length() == 0) {
                    layLokasi.setError("Min character length is 0");
                    lokasi.requestFocus();
                    loading.dismiss();
                } else if (kejadian.length() == 0) {
                    layKejadian.setError("Min character length is 0");
                    kejadian.requestFocus();
                    loading.dismiss();
                } else if (kesan.length() == 0) {
                    layKesan.setError("Min character length is 0");
                    kesan.requestFocus();
                    loading.dismiss();
                } else {
                    int sKel = kelamin.getCheckedRadioButtonId();
                    kel = kelamin.findViewById(sKel);
                    int sStat = status.getCheckedRadioButtonId();
                    stat = status.findViewById(sStat);
                    hasil.setText("\n-----[ HASIL ]-----\nNama: " + nama.getText().toString() + "\nJenis Kelamin: " + kel.getText() + "\nAsal Sekolah: " + sekolah.getText().toString() + "\nStatus: " + stat.getText() + "\nLokasi: " + lokasi.getText().toString() + "\nTanggal: " + tgl.getText().toString() + "\nKejadian: " + kejadian.getText().toString() + "\nKesan: " + kesan.getText().toString());
                    loading.setTitleText("Success!")
                            .setContentText("Your form has been submitted.")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    loading.show();
                    nama.setText("");
                    sekolah.setText("");
                    lokasi.setText("");
                    kejadian.setText("");
                    kesan.setText("");
                    layNama.setError(null);
                    laySekolah.setError(null);
                    layLokasi.setError(null);
                    layKejadian.setError(null);
                    layKesan.setError(null);
                    kelamin.check(kelamin.getChildAt(0).getId());
                    status.check(status.getChildAt(0).getId());
                    tgl.setText(dateNow);
                    nama.requestFocus();
                }
            }
        });
        return v;
    }

    private int getFromCalendar(String strDate, int field) {
        int result = -1;
        try {
            java.util.Date date = df.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result = cal.get(field);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
