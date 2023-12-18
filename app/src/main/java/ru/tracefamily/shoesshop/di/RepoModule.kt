package ru.tracefamily.shoesshop.di

import android.app.Application
import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.tracefamily.shoesshop.domain.repo.ApiInfoServiceRepo
import ru.tracefamily.shoesshop.domain.repo.BarcodeScannerRepo
import ru.tracefamily.shoesshop.repo.ApiInfoServiceRepoImpl
import ru.tracefamily.shoesshop.repo.BarcodeScannerRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideApiInfoServiceRepo() : ApiInfoServiceRepo = ApiInfoServiceRepoImpl()

    @Provides
    @Singleton
    fun provideScannerRepo(scanner: GmsBarcodeScanner): BarcodeScannerRepo = BarcodeScannerRepoImpl(scanner)

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideBarcodeOptions(): GmsBarcodeScannerOptions = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    @Singleton
    @Provides
    fun provideBarcodeScanner(
        context: Context,
        scannerOptions: GmsBarcodeScannerOptions
    ): GmsBarcodeScanner =
        GmsBarcodeScanning.getClient(context, scannerOptions)
}