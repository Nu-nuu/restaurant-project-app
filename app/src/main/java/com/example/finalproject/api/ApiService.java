    package com.example.finalproject.api;
    import com.example.finalproject.models.Category;
    import com.example.finalproject.models.Food;
    import com.example.finalproject.models.Fooded;
    import com.example.finalproject.models.Reservate;
    import com.example.finalproject.models.Reservated;
    import com.example.finalproject.models.Reservates;
    import com.example.finalproject.models.Reservation;
    import com.example.finalproject.models.ReservationHistory;
    import com.example.finalproject.models.Table;
    import com.example.finalproject.models.User;
    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;
    import java.security.cert.CertificateException;
    import java.security.cert.X509Certificate;
    import java.util.List;
    import java.util.concurrent.TimeUnit;
    import javax.net.ssl.SSLContext;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509TrustManager;
    import okhttp3.OkHttpClient;
    import retrofit2.Call;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;
    import retrofit2.http.Body;
    import retrofit2.http.DELETE;
    import retrofit2.http.GET;
    import retrofit2.http.POST;
    import retrofit2.http.PUT;
    import retrofit2.http.Path;
    import retrofit2.http.Query;

    public interface ApiService {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS);

        static ApiService createApiService() {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
                okHttpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);

                OkHttpClient okHttpClient = okHttpClientBuilder.build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://restaurantbe.onrender.com/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(okHttpClient)
                        .build();

                return retrofit.create(ApiService.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // Users
        @GET("/users")
        Call<List<User>> getAllUsers();

        @GET("/users/{id}")
        Call<User> getUserByID(@Path("id") String id);

        @GET("/users/{id}/reservationHistory")
        Call<List<ReservationHistory>> getReservationHistoryByUserID(@Path("id") String id);

        @GET("/users/{id}/reservationHistory/{reservationId}")
        Call<Reservation> getOneReservationByUserID(@Path("id") String id, @Path("reservationId") String reservationId);

        @GET("/usersBySearch")
        Call<List<User>> getAllUsersBySearch(@Query("search") String searchQuery);

        @POST("/users")
        Call<User> addUser(@Body User user);

        @PUT("/users/{id}")
        Call<User> updateUser(@Path("id") String id, @Body User user);

        @DELETE("/users/{id}")
        Call<Void> deleteUser(@Path("id") String id);

        // Tables
        @GET("/tables")
        Call<List<Table>> getAllTables();

        @GET("/tables/{id}")
        Call<Table> getTableByID(@Path("id") String id);

        @GET("/tablesByCapacity")
        Call<List<Table>> getTableByCapacity(@Query("capacity") int capacity);

        @POST("/tables")
        Call<Table> addTable(@Body Table table);

        @PUT("/tables/{id}")
        Call<Table> updateTable(@Path("id") String id, @Body Table table);

        @DELETE("/tables/{id}")
        Call<Void> deleteTable(@Path("id") String id);

        // Foods
    //    @GET("/foods")
    //    Call<List<Food>> getAllFoods();



        @GET("/foodsBySearch")
        Call<List<Fooded>> getAllFoodsBySearch(@Query("search") String searchQuery);

        @GET("/foods/{id}")
        Call<Food> getFoodByID(@Path("id") String id);
        @GET("/foods")
        Call<List<Fooded>> getAllFoods();

        @POST("/foods")
        Call<Food> addFood(@Body Food food);

        @PUT("/foods/{id}")
        Call<Food> updateFood(@Path("id") String id, @Body Food food);

        @DELETE("/foods/{id}")
        Call<Void> deleteFood(@Path("id") String id);

        // Categories
        @GET("/categories")
        Call<List<Category>> getAllCategories();

        @GET("/categories/{id}")
        Call<Category> getCategoryByID(@Path("id") String id);

        @GET("/categoriesBySearch")
        Call<List<Category>> getAllCategoriesBySearch(@Query("search") String searchQuery);

        @POST("/categories")
        Call<Category> addCategory(@Body Category category);

        @PUT("/categories/{id}")
        Call<Category> updateCategory(@Path("id") String id, @Body Category category);

        @DELETE("/categories/{id}")
        Call<Void> deleteCategory(@Path("id") String id);

        // Reservations
        @GET("/reservations")
        Call<List<Reservated>> getAllReservations();

        @GET("/reservations/{id}")
        Call<Reservated> getReservationByID(@Path("id") String id);

        @POST("/reservations")
        Call<Reservation> addReservation(
                @Query("userId") String userId,
//                @Query("tableId") String tableId,
//                @Query("foodId") String foodIds,
                @Body Reservation reservation
        );


        @PUT("/reservations/{id}")
        Call<Reservate> updateReservation(@Path("id") String id, @Body Reservate reservate);

        @PUT("/reservations/{id}")
        Call<Reservates> updateReservations(@Path("id") String id, @Body Reservates reservates);


        @DELETE("/reservations/{id}")
        Call<Void> deleteReservation(@Path("id") String id);
    }
