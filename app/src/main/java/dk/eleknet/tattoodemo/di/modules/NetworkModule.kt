package dk.eleknet.tattoodemo.di.modules

import android.app.Application
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dk.eleknet.tattoodemo.BuildConfig
import dk.eleknet.tattoodemo.di.scopes.ApplicationScope
import dk.eleknet.tattoodemo.network.DateTimeAdapter
import dk.eleknet.tattoodemo.network.TattoodoApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * Provides network dependencies
 */
@Module(includes = [])
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideCache(application: Application): Cache {
        val cacheSize: Long = 10 * 1024 * 1024 // 10MB
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @ApplicationScope
    fun provideLogger(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        when (BuildConfig.HTTP_LOGLEVEL) {
            0 -> interceptor.level = HttpLoggingInterceptor.Level.NONE
            1 -> interceptor.level = HttpLoggingInterceptor.Level.BASIC
            2 -> interceptor.level = HttpLoggingInterceptor.Level.BODY
            else -> interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }

    @Provides
    @ApplicationScope
    fun provideMoshi(): MoshiConverterFactory {
        val moshi = Moshi.Builder()
            .add(DateTimeAdapter())
            .build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @ApplicationScope
    @Named("tattoodoHttpClient")
    fun provideTattodoApiClient(httpLoggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(1, TimeUnit.MINUTES)
            .cache(cache)
            .build()
    }

    @Provides
    @ApplicationScope
    @Named("tattoodoApiRetrofit")
    fun provideTattoodoApiRetrofit(moshiConverterFactory: MoshiConverterFactory, @Named("tattoodoHttpClient")client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.TATTOODO_API_ENDPOINT)
            .addConverterFactory(moshiConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideTattoodoApi(@Named("tattoodoApiRetrofit")retrofit: Retrofit): TattoodoApi {
        return retrofit.create(TattoodoApi::class.java)
    }
}