package com.xeniac.parkingmoderator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.exifinterface.media.ExifInterface;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataSource;
import com.xeniac.parkingmoderator.database.vehiclesTable.VehiclesDataItem;
import com.xeniac.parkingmoderator.database.vehiclesTable.VehiclesDataSource;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class EnterActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ParkingDataSource parkingDataSource;
    private VehiclesDataSource vehiclesDataSource;
    private ParkingDataItem parking;

    private String photoPath = null;

    private ScrollView scrollView;
    private MaterialCardView popUpCV;
    private TextView popUpTV;
    private FrameLayout shadowFL;
    private RelativeLayout photoRL;
    private ImageView photoIV;
    private TextInputEditText plateNumber2ET;
    private TextInputEditText plateLetterET;
    private TextInputEditText plateNumber3ET;
    private TextInputEditText plateIranET;
    private TextInputEditText modelET;
    private TextInputEditText nameET;
    private TextInputEditText phoneNumberET;
    private LinearLayout enterLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        Toolbar toolbar = findViewById(R.id.toolbar_enter);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        enterInitializer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parkingDataSource.open();
        vehiclesDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        parkingDataSource.close();
        vehiclesDataSource.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onSupportNavigateUp();
    }

    private void enterInitializer() {
        parkingDataSource = new ParkingDataSource(this);
        vehiclesDataSource = new VehiclesDataSource(this);
        SharedPreferences preferences =
                getSharedPreferences(LoginActivity.LOGIN_CHECK, MODE_PRIVATE);
        String parkingEmail = preferences.getString(LoginActivity.PARKING_EMAIL_KEY, null);
        parking = parkingDataSource.getParking(parkingEmail);
        enterLayouts();
        plateHints();
        enterActionDone();
    }

    private void enterLayouts() {
        scrollView = findViewById(R.id.sv_enter);
        popUpCV = findViewById(R.id.cv_enter_popup_panel);
        popUpTV = findViewById(R.id.tv_enter_popup);
        shadowFL = findViewById(R.id.fl_enter_shadow);
        photoRL = findViewById(R.id.rl_enter_photo);
        photoIV = findViewById(R.id.iv_enter);
        plateNumber2ET = findViewById(R.id.ti_enter_edit_plate_number_2);
        plateLetterET = findViewById(R.id.ti_enter_edit_plate_letter);
        plateNumber3ET = findViewById(R.id.ti_enter_edit_plate_number_3);
        plateIranET = findViewById(R.id.ti_enter_edit_plate_iran);
        modelET = findViewById(R.id.ti_enter_edit_model);
        nameET = findViewById(R.id.ti_enter_edit_name);
        phoneNumberET = findViewById(R.id.ti_enter_edit_phone);
        enterLL = findViewById(R.id.ll_enter_enter);

        MaterialCardView enterCV = findViewById(R.id.cv_enter);
        enterCV.setClipToOutline(false);
        photoIV.setClipToOutline(true);
    }

    private void plateHints() {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));
        numberFormat.setGroupingUsed(false);

        plateNumber2ET.setHint(numberFormat.format(79));
        plateLetterET.setHint("ب");
        plateNumber3ET.setHint(numberFormat.format(426));

        plateNumber2ET.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                plateNumber2ET.setHint("");
            } else {
                plateNumber2ET.setHint(numberFormat.format(79));
            }
        });

        plateLetterET.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                plateLetterET.setHint("");
            } else {
                plateLetterET.setHint("ب");
            }
        });

        plateNumber3ET.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                plateNumber3ET.setHint("");
            } else {
                plateNumber3ET.setHint(numberFormat.format(426));
            }
        });
    }

    public void photoOnClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                //Error occurred while creating the File
                e.printStackTrace();
            }
            //Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.xeniac.parkingmoderator.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        File imageFile = new File(photoPath);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (imageFile.exists()) {
                try {
                    Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    ExifInterface exifInterface = new ExifInterface(photoPath);
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            photoIV.setImageBitmap(rotateImage(imageBitmap, 90));
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            photoIV.setImageBitmap(rotateImage(imageBitmap, 180));
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            photoIV.setImageBitmap(rotateImage(imageBitmap, 270));
                            break;
                        default:
                            photoIV.setImageBitmap(imageBitmap);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //noinspection ResultOfMethodCallIgnored
            imageFile.delete();
            photoPath = null;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File createImageFile() throws IOException {
        //Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_kkmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",       /* suffix */
                storageDir          /* directory */
        );

        //Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void enterActionDone() {
        phoneNumberET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                enterOnClick(v);
            }
            return false;
        });
    }

    public void enterOnClick(View view) {
        String plateNumber2Input = Objects.requireNonNull(plateNumber2ET.getText()).toString();
        String plateLetterInput = Objects.requireNonNull(plateLetterET.getText()).toString();
        String plateNumber3Input = Objects.requireNonNull(plateNumber3ET.getText()).toString();
        String plateIranInput = Objects.requireNonNull(plateIranET.getText()).toString();
        String modelInput = Objects.requireNonNull(modelET.getText()).toString();
        String nameInput = Objects.requireNonNull(nameET.getText()).toString();
        String phoneNumberInput = Objects.requireNonNull(phoneNumberET.getText()).toString();

        if (TextUtils.isEmpty(plateNumber2Input)) {
            plateNumber2ET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(plateLetterInput)) {
            plateLetterET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(plateNumber3Input)) {
            plateNumber3ET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(plateIranInput)) {
            plateIranET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(nameInput)) {
            nameET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (!TextUtils.isEmpty(phoneNumberInput) && !isPhoneNumberValid(phoneNumberInput)) {
            phoneNumberET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else {
            plateNumber2ET.clearFocus();
            plateLetterET.clearFocus();
            plateNumber3ET.clearFocus();
            plateIranET.clearFocus();
            modelET.clearFocus();
            nameET.clearFocus();
            phoneNumberET.clearFocus();

            if (TextUtils.isEmpty(modelInput)) {
                modelInput = null;
            }

            if (TextUtils.isEmpty(phoneNumberInput)) {
                phoneNumberInput = null;
            }

            PersianDate currentDate = new PersianDate();
            PersianDateFormat dateFormat = new PersianDateFormat("YmdHis");
            String enterDate = dateFormat.format(currentDate);

            VehiclesDataItem vehicle = new VehiclesDataItem();
            vehicle.setPlateNumber2(Integer.parseInt(plateNumber2Input));
            vehicle.setPlateLetter(plateLetterInput);
            vehicle.setPlateNumber3(Integer.parseInt(plateNumber3Input));
            vehicle.setPlateIran(Integer.parseInt(plateIranInput));
            vehicle.setModel(modelInput);
            vehicle.setName(nameInput);
            vehicle.setPhoneNumber(phoneNumberInput);
            vehicle.setDateEnter(enterDate);
            vehicle.setPhoto(photoPath);
            vehicle.setParkingId(parking.getId());

            enterVehicle(vehicle);
        }
    }

    private boolean isPhoneNumberValid(String number) {
        return ((TextUtils.isDigitsOnly(number) && (number.startsWith("9") ||
                number.startsWith("09")) && number.length() >= 10));
    }

    private void enterVehicle(VehiclesDataItem item) {
        parkingDataSource.open();
        vehiclesDataSource.open();

        if (!vehiclesDataSource.vehicleExist(item.getPlateNumber2(), item.getPlateLetter(),
                item.getPlateNumber3(), item.getPlateIran())) {
            vehiclesDataSource.createVehicle(item);
            parking.setOccupiedSpace(parking.getOccupiedSpace() + 1);
            parkingDataSource.updateParking(parking);
            popUpTV.setText(R.string.string_enter_tv_popup_question);
        } else {
            popUpTV.setText(R.string.string_enter_tv_popup_question_exists);
        }
        popUpMessage();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popUpMessage() {
        scrollView.setOnTouchListener((view1, motionEvent) -> true);
        shadowFL.setVisibility(View.VISIBLE);
        photoRL.setClickable(false);
        plateNumber2ET.setEnabled(false);
        plateLetterET.setEnabled(false);
        plateNumber3ET.setEnabled(false);
        plateIranET.setEnabled(false);
        modelET.setEnabled(false);
        nameET.setEnabled(false);
        phoneNumberET.setEnabled(false);
        enterLL.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", distance);
        animator.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void yesOnClick(View view) {
        photoPath = null;
        photoIV.setImageResource(R.drawable.ic_user);
        plateNumber2ET.setText(null);
        plateLetterET.setText(null);
        plateNumber3ET.setText(null);
        plateIranET.setText(null);
        modelET.setText(null);
        nameET.setText(null);
        phoneNumberET.setText(null);

        scrollView.setOnTouchListener((view1, motionEvent) -> false);
        shadowFL.setVisibility(View.GONE);
        photoRL.setClickable(true);
        plateNumber2ET.setEnabled(true);
        plateLetterET.setEnabled(true);
        plateNumber3ET.setEnabled(true);
        plateIranET.setEnabled(true);
        modelET.setEnabled(true);
        nameET.setEnabled(true);
        phoneNumberET.setEnabled(true);
        enterLL.setClickable(true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", 0);
        animator.start();
    }

    public void noOnClick(View view) {
        super.onNavigateUp();
    }
}