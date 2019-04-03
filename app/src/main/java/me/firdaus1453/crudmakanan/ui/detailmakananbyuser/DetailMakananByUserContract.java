package me.firdaus1453.crudmakanan.ui.detailmakananbyuser;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import me.firdaus1453.crudmakanan.model.makanan.MakananData;

/**
 * Created by firdaus1453 on 4/3/2019.
 */
public interface DetailMakananByUserContract {
    interface View{
        void showProgress();
        void hideProgress();
        void showDetailMakanan(MakananData makananData);
        void showMessage(String msg);
        void successDelete();
        void showSpinnerCategory(List<MakananData> categoryDataList);
    }

    interface Presenter{
        void getCategory();
        void getDetailMakanan(String idMakanan);
        void updateDataMakanan(Context context,
                               Uri filePath,
                               String namaMakanan,
                               String descMakanan,
                               String idCategory,
                               String namaFotoMakanan);
        void deleteMakanan(String idMakanan, String namaFotoMakanan);
    }
}
