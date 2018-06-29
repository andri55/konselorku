package com.konselorku.android.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.konselorku.android.R;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.supercharge.shimmerlayout.ShimmerLayout;

import static android.app.Activity.RESULT_OK;

public class LaporFragment extends Fragment {

    private ImageView imageView, pickImage, cancelImage;
    private EditText pesan;
    private TextView hasil;
    private ShimmerLayout progressPlaceholder;
    private Button submit;
    Fragment frag;
    Uri selectedImage;

    public LaporFragment() {
    }

    public static LaporFragment newInstance(String param1, String param2) {
        LaporFragment fragment = new LaporFragment();
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
        View v = inflater.inflate(R.layout.fragment_lapor, container, false);
        pickImage = v.findViewById(R.id.pick_foto_lapor);
        pickImage.setVisibility(View.VISIBLE);
        pickImage.requestFocus();
        frag = this;
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        imageView = v.findViewById(R.id.img_foto_lapor);
        int width = pickImage.getLayoutParams().width;
        pickImage.getLayoutParams().height = width;
        imageView.getLayoutParams().height = width;
        imageView.setImageResource(R.color.gray50);
        progressPlaceholder = v.findViewById(R.id.progress_lapor);

        cancelImage = v.findViewById(R.id.img_cancel_lapor);
        cancelImage.setVisibility(View.INVISIBLE);
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(false);
                submit.setAlpha(.5f);
                pesan.setEnabled(false);
                pesan.setAlpha(.5f);
                pesan.setFocusable(false);
                imageView.setImageResource(R.color.gray50);
                pickImage.setVisibility(View.VISIBLE);
                cancelImage.setVisibility(View.INVISIBLE);
            }
        });

        submit = v.findViewById(R.id.btn_submit_lapor);
        submit.setEnabled(false);
        submit.setAlpha(.5f);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog loading = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                loading.setTitleText("Loading")
                        .setCancelable(false);
                loading.getProgressHelper()
                        .setBarColor(getResources().getColor(R.color.blue));
                loading.show();
                pickImage.setVisibility(View.VISIBLE);
                pickImage.requestFocus();
                int width = pickImage.getLayoutParams().width;
                pickImage.getLayoutParams().height = width;
                imageView.getLayoutParams().height = width;
                imageView.setImageResource(R.color.gray50);
                cancelImage.setVisibility(View.INVISIBLE);
                pesan.setEnabled(false);
                pesan.setAlpha(.5f);
                pesan.setFocusable(false);
                pesan.setText("");
                hasil.setText("\n-----[ HASIL ]-----\nGambar: " + selectedImage + "\nPesan: " + pesan.getText().toString() + "\n");
                loading.setTitleText("Success!")
                        .setContentText("Your form has been submitted.")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                loading.show();
            }
        });

        pesan = v.findViewById(R.id.txt_lapor);
        pesan.setEnabled(false);
        pesan.setAlpha(.5f);
        pesan.setFocusable(false);
        pesan.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    submit.setEnabled(false);
                    submit.setAlpha(.5f);
                } else {
                    submit.setEnabled(true);
                    submit.setAlpha(1f);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        hasil = v.findViewById(R.id.hasil_lapor);
        return v;
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        final Drawable[] icon = {};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    RxImagePicker.with(getFragmentManager()).requestImage(Sources.CAMERA)
                            .flatMap(uri -> {
                                switch (converterRadioGroup.getCheckedRadioButtonId()) {
                                    case R.id.radio_file:
                                        return RxImageConverters.uriToFile(MainActivity.this, uri, createTempFile());
                                    case R.id.radio_bitmap:
                                        return RxImageConverters.uriToBitmap(MainActivity.this, uri);
                                    default:
                                        return Observable.just(uri);
                                }
                            })
                            .subscribe(this::onImagePicked, throwable -> Toast.makeText(MainActivity.this, String.format("Error: %s", throwable), Toast.LENGTH_LONG).show());
                } else if (options[item].equals("Choose from Gallery")) {

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

        /*final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        final Drawable[] icon = {};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    selectedImage = Uri.fromFile(getOutputMediaFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                    frag.startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();*/
    }

/*
    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        if (resultCode == RESULT_OK) {
            progressPlaceholder.startShimmerAnimation();
            pickImage.setVisibility(View.GONE);
            cancelImage.setVisibility(View.VISIBLE);
            if (requestCode == 1 || requestCode == 2) {
                if (requestCode == 2) {
                    selectedImage = data.getData();
                }
                Picasso.with(getActivity()).load(selectedImage).placeholder(R.color.gray50).noFade().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressPlaceholder.stopShimmerAnimation();
                        pesan.setEnabled(true);
                        pesan.setAlpha(1f);
                        pesan.setFocusableInTouchMode(true);
                        pesan.requestFocus();
                        pesan.setText(pesan.getText().toString() +"");
                    }

                    @Override
                    public void onError() {
                        progressPlaceholder.stopShimmerAnimation();
                        pickImage.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.color.gray50);
                        cancelImage.setVisibility(View.INVISIBLE);
                        pesan.setEnabled(false);
                        pesan.setAlpha(.5f);
                        pesan.setFocusable(false);
                    }
                });
            }
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Konselorku");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }*/
}
