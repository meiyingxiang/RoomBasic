package com.example.roombasicdemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private final WordRepository wordRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<List<Word>> getListLiveData() {
        return wordRepository.getListLiveData();
    }

    public LiveData<List<Word>> findWordsWithPatten(String patten) {
        return wordRepository.findWordsWithPatten(patten);
    }


    public void insertWords(Word... words) {
        wordRepository.insertWords(words);
    }

    public void updateWords(Word... words) {
        wordRepository.updateWords(words);
    }

    public void deleteWord(Word... words) {
        wordRepository.deleteWord(words);
    }

    public void deleteAllWords() {
        wordRepository.deleteAllWords();
    }
}
