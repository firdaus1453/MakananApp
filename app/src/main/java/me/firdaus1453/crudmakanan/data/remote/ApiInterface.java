package me.firdaus1453.crudmakanan.data.remote;

import me.firdaus1453.crudmakanan.model.detailmakanan.DetailMakananResponse;
import me.firdaus1453.crudmakanan.model.login.LoginResponse;
import me.firdaus1453.crudmakanan.model.makanan.MakananResponse;
import me.firdaus1453.crudmakanan.model.uploadmakanan.UploadMakananResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by firdaus1453 on 2/28/2019.
 */
public interface ApiInterface {

    // Membuat endpoint login
    @FormUrlEncoded
    @POST("loginuser.php")
    Call<LoginResponse> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    // Membuat endpoint register
    @FormUrlEncoded
    @POST("registeruser.php")
    Call<LoginResponse> registerUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("namauser") String namauser,
            @Field("alamat") String alamat,
            @Field("jenkel") String jenkel,
            @Field("notelp") String notelp,
            @Field("level") String level
    );

    // Membuat update
    @FormUrlEncoded
    @POST("updateuser.php")
    Call<LoginResponse> updateUser(
            @Field("iduser") int iduser,
            @Field("namauser") String namauser,
            @Field("alamat") String alamat,
            @Field("jenkel") String jenkel,
            @Field("notelp") String notelp
    );

    // Mengambil data kategori
    @GET("getkategori.php")
    Call<MakananResponse> getKategoriMakanan();

    // Mengambil data makanan baru
    @GET("getmakananbaru.php")
    Call<MakananResponse> getMakananBaru();

    // Mengambil data makanan populer
    @GET("getmakananpopuler.php")
    Call<MakananResponse> getMakananPopuler();

    // Mengupload makanan
    @Multipart
    @POST("uploadmakanan.php")
    Call<UploadMakananResponse> uploadMakanan(
            @Part("iduser") int iduser,
            @Part("idkategori") int idkategori,
            @Part("namamakanan") RequestBody namamakanan,
            @Part("descmakanan") RequestBody descmakanan,
            @Part("timeinsert") RequestBody timeinsert,
            @Part MultipartBody.Part image
    );

    // Mengambil data detail makanan
    @GET("getdetailmakanan.php")
    Call<DetailMakananResponse> getDetailMakanan(@Query("idmakanan") int idMakanan);

    // Mengambil data makanan berdasarkan id category
    @GET("getmakananbykategori.php")
    Call<MakananResponse> getMakananByCategory(@Query("idkategori") int idCategory);

    // Mengambil data makanan berdasarkan id user
    @GET("getmakananbyuser.php")
    Call<MakananResponse> getMakananByUser(@Query("iduser") int idUser);

    @FormUrlEncoded
    @POST("deletemakanan.php")
    Call<MakananResponse> deleteMakanan(
            @Field("idmakanan") int idMakanan,
            @Field("fotomakanan") String namaFotoMakanan
    );

    // Update makanan
    @Multipart
    @POST("updatemakanan.php")
    Call<MakananResponse> updateMakanan(
            @Part("idmakanan") int idMakanan,
            @Part("idkategori") int idCategory,
            @Part("namamakanan") RequestBody namaMakanan,
            @Part("descmakanan") RequestBody descMakanan,
            @Part("fotomakanan") RequestBody namaFotoMakanan,
            @Part("inserttime") RequestBody insertTime,
            @Part MultipartBody.Part image
    );
}
