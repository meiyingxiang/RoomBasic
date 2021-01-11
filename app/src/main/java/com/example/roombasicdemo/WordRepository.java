package com.example.roombasicdemo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    private final LiveData<List<Word>> listLiveData;
    private final WordDao wordDao;

    public WordRepository(Context mContext) {
        WordDatabase wordDatabase = WordDatabase.getInstance(mContext.getApplicationContext());
        wordDao = wordDatabase.getWordDao();
        listLiveData = wordDao.getAllWords();
    }

    public LiveData<List<Word>> getListLiveData() {
        return listLiveData;
    }

    public LiveData<List<Word>> findWordsWithPatten(String patten) {
        return wordDao.findWordsWithPatten("%" + patten + "%");
    }

    void insertWords(Word... words) {
        new InsertAsyncTask(wordDao).execute(words);
    }

    void updateWords(Word... words) {
        new UpdateAsyncTask(wordDao).execute(words);
    }

    void deleteWord(Word... words) {
        new DeleteAsyncTask(wordDao).execute(words);
    }

    void deleteAllWords() {
        new DeleteAllAsyncTask(wordDao).execute();
    }

    /**
     * AsyncTask<Word,Void,Void>
     * 第一个参数：使用的参数
     * 第二个参数：进度
     * 第三个参数：返回结果
     */
    static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private final WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //执行完成
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //进度
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.update(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        public DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}
