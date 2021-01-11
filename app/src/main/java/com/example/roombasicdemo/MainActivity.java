package com.example.roombasicdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.savedstate.SavedStateRegistryOwner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button insert, update, clear, delete;
    private RecyclerView recycle;
    private Switch switch1;

    private WordViewModel wordViewModel;
    private WordAdapter wordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建RoomDatabase数据库
        /*
         * 第一个参数：上下文对象'
         * 第二个参数：RoomDatabase的class
         * 第三个参数：数据库的文件名
         */
        wordViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(WordViewModel.class);

        initView();
        onClickView();
        if (wordAdapter == null) {
            wordAdapter = new WordAdapter();
            recycle.setLayoutManager(new LinearLayoutManager(this));
            wordAdapter.setWordViewModel(wordViewModel);
            recycle.setAdapter(wordAdapter);
        }
        wordViewModel.getListLiveData().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int size = wordAdapter.getItemCount();
                wordAdapter.setWordList(words);
                if (size != words.size()) {
                    wordAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void onClickView() {
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Word word = new Word("Hello", "你好\n");
//                Word word2 = new Word("World", "世界\n");
                String[] word1 = {"Hello", "Java", "Kotlin", "Python", "C#", "C", "C++", "Flutter", "React Native"};
                String[] word2 = {"你好", "佳娃", "卡特琳", "派森", "C#", "C", "C++", "弗鲁特", "混合"};
                for (int i = 0; i < word1.length; i++) {
                    wordViewModel.insertWords(new Word(word1[i], word2[i]));
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新
                wordViewModel.updateWords();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wordDao.deleteAllWords();
                wordViewModel.deleteAllWords();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word word = new Word("Hello", "你好");
                word.setId(1);
//                wordDao.deleteWords();
                wordViewModel.deleteWord(word);
            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
                wordAdapter.setUseCardView(isChecked);
                wordAdapter.notifyDataSetChanged();
//                }
            }
        });
    }

    private void initView() {
        insert = findViewById(R.id.insert);
        update = findViewById(R.id.update);
        clear = findViewById(R.id.clear);
        delete = findViewById(R.id.delete);
        recycle = findViewById(R.id.recycle);
        switch1 = findViewById(R.id.switch1);
    }


}