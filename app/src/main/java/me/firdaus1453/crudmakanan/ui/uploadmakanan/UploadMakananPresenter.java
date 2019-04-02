package me.firdaus1453.crudmakanan.ui.uploadmakanan;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.firdaus1453.crudmakanan.data.remote.ApiClient;
import me.firdaus1453.crudmakanan.data.remote.ApiInterface;
import me.firdaus1453.crudmakanan.model.makanan.MakananResponse;
import me.firdaus1453.crudmakanan.model.uploadmakanan.UploadMakananResponse;
import me.firdaus1453.crudmakanan.utils.Constants;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by firdaus1453 on 3/28/2019.
 */
public class UploadMakananPresenter implements UploadMakananContract.Presenter {

    // TODO 1 menyiapkan variable yang dibuthkan
    private final UploadMakananContract.View view;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    public UploadMakananPresenter(UploadMakananContract.View view) {
        this.view = view;
    }

    @Override
    public void getCategory() {
        view.showProgress();
        Call<MakananResponse> call = apiInterface.getKategoriMakanan();
        call.enqueue(new Callback<MakananResponse>() {
            @Override
            public void onResponse(Call<MakananResponse> call, Response<MakananResponse> response) {
                view.hideProgress();
                if (response.body() != null) {
                    if (response.body().getResult() == 1) {
                        view.showSpinnerCategory(response.body().getMakananDataList());
                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                } else {
                    view.showMessage("Data kosong");
                }
            }

            @Override
            public void onFailure(Call<MakananResponse> call, Throwable t) {
                view.hideProgress();
                view.showMessage(t.getMessage());
                Log.i("Cek failure", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void uploadMakanan(Context context, Uri filePath, String namaMakanan, String descMakanan, String idCategory) {
        view.showProgress();

        if (namaMakanan.isEmpty()) {
            view.showMessage("Nama makanan tidak boleh kosong");
            view.hideProgress();
            return;
        }

        if (descMakanan.isEmpty()) {
            view.showMessage("Desc makanan tidak boleh kosong");
            view.hideProgress();
            return;
        }

        if (filePath == null) {
            view.showMessage("Silahkan memilih gambar");
            view.hideProgress();
            return;
        }

        Log.i("isi filepath", "filepath: " + String.valueOf(filePath));
        // Mengambil alamat file image
        File myFile = new File(filePath.getPath());
        Log.i("isi myFile", "myFile: " + String.valueOf(myFile));

        Uri selectedImage = getImageContentUri(context, myFile, filePath);
        Log.i("isi selectedimage", "selectedimg: " + String.valueOf(selectedImage));

        File imageFile = null;
        if (selectedImage != null) {
            String partImage = getPath(context, selectedImage);
            Log.i("isi partImage", "partImage: " + imageFile);

            imageFile = new File(partImage);
            Log.i("isi imageFile if", "imgfile: " + String.valueOf(imageFile));
        }else {
            String partImage = getPath(context, filePath);
            imageFile = new File(partImage);
            Log.i("isi imageFile else", "imgfile: " + String.valueOf(imageFile));
        }
        // Mengambil id user di dalam shared pref
        SharedPreferences pref = context.getSharedPreferences(Constants.pref_name, 0);
        String idUser = pref.getString(Constants.KEY_USER_ID,"");

        // Mengambil date sekarang untuk waktu upload makanan
        String dateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Memasukkan data yang diperlukan ke dalam request body dengan tipe form-data
        // Memasukkan imagefile ke dalam requestbody.part
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part mPartImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

        // Memasukkan nama,desc dan inserttime ke dalam requestbody
        RequestBody mNamaMakanan = RequestBody.create(MediaType.parse("multipart/form-data"), namaMakanan);
        RequestBody mDescMakanan = RequestBody.create(MediaType.parse("multipart/form-data"), descMakanan);
        RequestBody mInsertTime = RequestBody.create(MediaType.parse("multipart/form-data"), dateNow);

        // Mengirim data ke API
        Call<UploadMakananResponse> call = apiInterface.uploadMakanan(
                Integer.valueOf(idUser),
                Integer.valueOf(idCategory),
                mNamaMakanan,
                mDescMakanan,
                mInsertTime,
                mPartImage);
        call.enqueue(new Callback<UploadMakananResponse>() {
            @Override
            public void onResponse(Call<UploadMakananResponse> call, Response<UploadMakananResponse> response) {
                view.hideProgress();
                if (response.body() != null){
                    if (response.body().getResult() == 1){
                        view.showMessage(response.body().getMessage());
                        view.successUpload();
                    }else {
                        view.showMessage(response.body().getMessage());
                    }
                }else {
                    view.showMessage("Data kosong");
                }
            }

            @Override
            public void onFailure(Call<UploadMakananResponse> call, Throwable t) {
                view.hideProgress();
                view.showMessage(t.getMessage());
                Log.i("Cek failure", "onFailure: " + t.getMessage());
            }
        });
    }

    private String getPath(Context context, Uri filepath) {
        Cursor cursor = context.getContentResolver().query(filepath, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private Uri getImageContentUri(Context context, File imageFile, Uri filePath) {
        String fileAbsolutePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{fileAbsolutePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Apabila file gambar sudah pernah diapakai namun ada kondisi lain yang belum diketahui
            // Apabila file gambar sudah pernah dipakai pengambilan bukan di galery

            Log.i("Isi Selected if", "Masuk cursor ada");
            return filePath;

        } else {
            Log.i("Isi Selected else", "cursor tidak ada");
            if (imageFile.exists()) {
                // Apabila file gambar baru belum pernah di pakai
                Log.i("Isi Selected else", "imagefile exists");
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, fileAbsolutePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                // Apabila file gambar sudah pernah dipakai
                // Apabila file gambar sudah pernah dipakai di galery
                Log.i("Isi Selected else", "imagefile tidak exists");
                return null;
            }
        }
    }
}

