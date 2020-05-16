package com.xeniac.parkingmoderator.database.vehiclesTable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.xeniac.parkingmoderator.ExitActivity;
import com.xeniac.parkingmoderator.R;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ViewHolder> {

    private Context mContext;
    private List<VehiclesDataItem> mItems;
    private ParkingDataItem mParking;

    public VehiclesAdapter(Context context, List<VehiclesDataItem> items, ParkingDataItem parking) {
        this.mContext = context;
        this.mItems = items;
        this.mParking = parking;
    }

    @NonNull
    @Override
    public VehiclesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_item_exit, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final VehiclesDataItem item = mItems.get(position);

        holder.exitCV.setClipToOutline(false);
        holder.exitIV.setClipToOutline(true);

        if (item.getPhoto() != null) {
            String photoPath = item.getPhoto();
            File imageFile = new File(photoPath);
            if (imageFile.exists()) {
                try {
                    Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    ExifInterface exifInterface = new ExifInterface(photoPath);
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            holder.exitIV.setImageBitmap(rotateImage(imageBitmap, 90));
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            holder.exitIV.setImageBitmap(rotateImage(imageBitmap, 180));
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            holder.exitIV.setImageBitmap(rotateImage(imageBitmap, 270));
                            break;
                        default:
                            holder.exitIV.setImageBitmap(imageBitmap);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        holder.plateNumber2ET.setText(String.valueOf(item.getPlateNumber2()));
        holder.plateLetterET.setText(item.getPlateLetter());
        holder.plateNumber3ET.setText(String.valueOf(item.getPlateNumber3()));
        holder.plateIranET.setText(String.valueOf(item.getPlateIran()));
        holder.modelET.setText(item.getModel());
        holder.nameET.setText(item.getName());
        holder.phoneNumberET.setText(item.getPhoneNumber());

        holder.exitLL.setOnClickListener(v -> {
            if (mContext instanceof ExitActivity) {
                ((ExitActivity) mContext).popUpExit(item, mParking.getChargeBase(),
                        mParking.getChargeExtra(), holder.exitLL);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        MaterialCardView exitCV;
        ImageView exitIV;
        TextInputEditText plateNumber2ET;
        TextInputEditText plateLetterET;
        TextInputEditText plateNumber3ET;
        TextInputEditText plateIranET;
        TextInputEditText modelET;
        TextInputEditText nameET;
        TextInputEditText phoneNumberET;
        LinearLayout exitLL;

        ViewHolder(View itemView) {
            super(itemView);

            exitCV = itemView.findViewById(R.id.cv_list_exit);
            exitIV = itemView.findViewById(R.id.iv_list_exit);
            plateNumber2ET = itemView.findViewById(R.id.ti_list_exit_edit_plate_number_2);
            plateLetterET = itemView.findViewById(R.id.ti_list_exit_edit_plate_letter);
            plateNumber3ET = itemView.findViewById(R.id.ti_list_exit_edit_plate_number_3);
            plateIranET = itemView.findViewById(R.id.ti_exit_edit_plate_iran);
            modelET = itemView.findViewById(R.id.ti_list_exit_edit_model);
            nameET = itemView.findViewById(R.id.ti_list_exit_edit_name);
            phoneNumberET = itemView.findViewById(R.id.ti_list_exit_edit_phone);
            exitLL = itemView.findViewById(R.id.ll_list_exit_exit);

            mView = itemView;
        }
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}