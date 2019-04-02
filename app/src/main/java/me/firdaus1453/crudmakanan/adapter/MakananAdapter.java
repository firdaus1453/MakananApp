package me.firdaus1453.crudmakanan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.firdaus1453.crudmakanan.R;
import me.firdaus1453.crudmakanan.model.makanan.MakananData;
import me.firdaus1453.crudmakanan.ui.detailmakanan.DetailMakananActivity;
import me.firdaus1453.crudmakanan.ui.detailmakananbyuser.DetailMakananByUserActivity;
import me.firdaus1453.crudmakanan.ui.makananbycategory.MakananByCategoryActivity;
import me.firdaus1453.crudmakanan.ui.uploadmakanan.UploadMakananActivity;
import me.firdaus1453.crudmakanan.utils.Constants;

/**
 * Created by firdaus1453 on 3/26/2019.
 */
public class MakananAdapter extends RecyclerView.Adapter<MakananAdapter.ViewHolder> {
    // TYPE 1 untuk makanan baru
    public static final int TYPE_1 = 1;
    // TYPE 2 untuk makanan populer
    public static final int TYPE_2 = 2;
    // TYPE 3 untuk category
    public static final int TYPE_3 = 3;
    // TYPE 4 untuk makanan by category
    public static final int TYPE_4 = 4;
    // TYPE 5 untuk makanan by user
    public static final int TYPE_5 = 5;

    Integer viewType;
    private final Context context;
    private final List<MakananData> makananDataList;


    public MakananAdapter(Integer viewType, Context context, List<MakananData> makananDataList) {
        this.viewType = viewType;
        this.context = context;
        this.makananDataList = makananDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_news, null);
                return new FoodNewsViewHolder(view);
            case TYPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_populer, null);
                return new FoodPopulerViewHolder(view);
            case TYPE_3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_kategory, null);
                return new FoodKategoryViewHolder(view);
            case TYPE_4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_by_category, null);
                return new FoodNewsViewHolder(view);
            case TYPE_5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_by_category, null);
                return new FoodByUserViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Mengambil data lalu memasukkan ke dalam model
        final MakananData makananData = makananDataList.get(position);

        // Mengambil view type dari user atau constractor
        int mViewType = viewType;
        switch (mViewType) {
            case TYPE_1:
                // Membuat holder untuk dapat mengakses widget
                FoodNewsViewHolder foodNewsViewHolder = (FoodNewsViewHolder) holder;

                // Requestoption untuk error dan placeholder gambar
                RequestOptions options = new RequestOptions().error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_broken_image);
                Glide.with(context).load(makananData.getUrlMakanan()).apply(options).into(foodNewsViewHolder.imgMakanan);

                // Menampilkan tittle dan jumlah view
                foodNewsViewHolder.txtTitle.setText(makananData.getNamaMakanan());
                foodNewsViewHolder.txtView.setText(makananData.getView());

                // Menampilkan waktu upload
                foodNewsViewHolder.txtTime.setText(newDate(makananData.getInsertTime()));

                // Membuat onclick
                foodNewsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Berpindah halaman ke detail makanan
                        context.startActivity(new Intent(context, DetailMakananActivity.class).putExtra(Constants.KEY_EXTRA_ID_MAKANAN, makananData.getIdMakanan()));
                    }
                });

                break;
            case TYPE_2:
                FoodPopulerViewHolder foodPopulerViewHolder = (FoodPopulerViewHolder) holder;

                // Requestoption untuk error dan placeholder gambar
                RequestOptions options2 = new RequestOptions().error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_broken_image);
                Glide.with(context).load(makananData.getUrlMakanan()).apply(options2).into(foodPopulerViewHolder.imgMakanan);
                // Menampilkan tittle dan jumlah view
                foodPopulerViewHolder.txtTitle.setText(makananData.getNamaMakanan());
                foodPopulerViewHolder.txtView.setText(makananData.getView());

                // Menampilkan waktu upload
                foodPopulerViewHolder.txtTime.setText(newDate(makananData.getInsertTime()));

                foodPopulerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Berpindah halaman ke detail makanan
                        context.startActivity(new Intent(context, DetailMakananActivity.class).putExtra(Constants.KEY_EXTRA_ID_MAKANAN, makananData.getIdMakanan()));

                    }
                });
                break;
            case TYPE_3:
                FoodKategoryViewHolder foodKategoryViewHolder = (FoodKategoryViewHolder) holder;

                // Requestoption untuk error dan placeholder gambar
                RequestOptions options3 = new RequestOptions().error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_broken_image);
                Glide.with(context).load(makananData.getUrlMakanan()).apply(options3).into(foodKategoryViewHolder.image);

                foodKategoryViewHolder.txtNamaKategory.setText(makananData.getNamaKategori());
                foodKategoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("cek idcategory adapter", "onClick: " + makananData.getIdKategori());
                        // Berpindah halaman ke detail makanan
                        context.startActivity(new Intent(context, MakananByCategoryActivity.class).putExtra(Constants.KEY_EXTRA_ID_CATEGORY, makananData.getIdKategori()));
                    }
                });
                break;
            case TYPE_4:
                // Membuat holder untuk dapat mengakses widget
                FoodNewsViewHolder foodNewsViewHolder2 = (FoodNewsViewHolder) holder;

                // Requestoption untuk error dan placeholder gambar
                RequestOptions options4 = new RequestOptions().error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_broken_image);
                Glide.with(context).load(makananData.getUrlMakanan()).apply(options4).into(foodNewsViewHolder2.imgMakanan);

                // Menampilkan tittle dan jumlah view
                foodNewsViewHolder2.txtTitle.setText(makananData.getNamaMakanan());
                foodNewsViewHolder2.txtView.setText(makananData.getView());

                // Menampilkan waktu upload
                foodNewsViewHolder2.txtTime.setText(newDate(makananData.getInsertTime()));

                // Membuat onclick
                foodNewsViewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Berpindah halaman ke detail makanan
                        context.startActivity(new Intent(context, DetailMakananActivity.class).putExtra(Constants.KEY_EXTRA_ID_MAKANAN, makananData.getIdMakanan()));
                    }
                });
                break;
            case TYPE_5:
                // Membuat holder untuk dapat mengakses widget
                FoodByUserViewHolder foodByUserViewHolder = (FoodByUserViewHolder) holder;

                // Requestoption untuk error dan placeholder gambar
                RequestOptions options5 = new RequestOptions().error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_broken_image);
                Glide.with(context).load(makananData.getUrlMakanan()).apply(options5).into(foodByUserViewHolder.imgMakanan);

                // Menampilkan tittle dan jumlah view
                foodByUserViewHolder.txtTitle.setText(makananData.getNamaMakanan());
                foodByUserViewHolder.txtView.setText(makananData.getView());

                // Menampilkan waktu upload
                foodByUserViewHolder.txtTime.setText(newDate(makananData.getInsertTime()));

                // Membuat onclick
                foodByUserViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Berpindah halaman ke detail makanan
                        context.startActivity(new Intent(context, DetailMakananByUserActivity.class).putExtra(Constants.KEY_EXTRA_ID_MAKANAN, makananData.getIdMakanan()));
                    }
                });
                break;
        }
    }

    private String newDate(String insertTime) {
        // Membuat variable penampung tanggal
        Date date = null;
        // Membuat penampung date untuk format yang baru
        String newDate = insertTime;

        // Membuat date dengan format sesuai dengan tanggal yang sudah dimiliki
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // mengubah tanggal yang dimiliki menjadi tipe date
        try {
            date = sdf.parse(insertTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Kita cek format date yang kita miliki sesuai dengan yang kita inginkan
        if (date != null) {
            // Mengubah date yang dimiliki menjadi format date yang baru
            newDate = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(date);
        }
        return newDate;
    }

    @Override
    public int getItemCount() {
        return makananDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FoodNewsViewHolder extends ViewHolder {
        @BindView(R.id.img_makanan)
        ImageView imgMakanan;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.img_view)
        ImageView imgView;
        @BindView(R.id.txt_view)
        TextView txtView;
        @BindView(R.id.txt_time)
        TextView txtTime;

        public FoodNewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class FoodPopulerViewHolder extends ViewHolder {
        @BindView(R.id.img_makanan)
        ImageView imgMakanan;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.img_view)
        ImageView imgView;
        @BindView(R.id.txt_view)
        TextView txtView;
        @BindView(R.id.txt_time)
        TextView txtTime;

        public FoodPopulerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class FoodKategoryViewHolder extends ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.txt_nama_kategory)
        TextView txtNamaKategory;

        public FoodKategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class FoodByUserViewHolder extends ViewHolder {
        @BindView(R.id.img_makanan)
        ImageView imgMakanan;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.img_view)
        ImageView imgView;
        @BindView(R.id.txt_view)
        TextView txtView;

        public FoodByUserViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
