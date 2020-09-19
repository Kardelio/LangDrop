package bk.personal.com.langdrop.di

import bk.personal.com.langdrop.utils.IRandomNumberGenerator
import bk.personal.com.langdrop.utils.RandomNumberGenerator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface UtilsModule {

    @Binds
    fun provideRandomNumberGen(rand: RandomNumberGenerator): IRandomNumberGenerator
}