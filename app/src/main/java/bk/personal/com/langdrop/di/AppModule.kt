package bk.personal.com.langdrop.di

import android.content.Context
import bk.personal.com.langdrop.R
import bk.personal.com.langdrop.game.repository.IWordRepository
import bk.personal.com.langdrop.game.repository.WordRepository
import bk.personal.com.langdrop.utils.IRandomNumberGenerator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideWordrepository(@ApplicationContext app: Context, gson: Gson, rng: IRandomNumberGenerator): IWordRepository {
        val fileContents = app.resources.openRawResource(R.raw.words_v2).bufferedReader().use { it.readText() }
        return WordRepository(fileContents, gson, rng)
    }
}