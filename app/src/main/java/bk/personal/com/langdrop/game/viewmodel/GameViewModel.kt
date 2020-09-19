package bk.personal.com.langdrop.game.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import bk.personal.com.langdrop.game.repository.IWordRepository

class GameViewModel @ViewModelInject constructor(private val repo: IWordRepository): ViewModel(){

    init {
        Log.d("BENK","Loaded vm")
    }

    fun test(){
        Log.d("BENK","test")

    }

}