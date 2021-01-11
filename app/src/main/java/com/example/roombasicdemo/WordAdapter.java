package com.example.roombasicdemo;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Word> wordList;

    private LayoutInflater inflater;
    private static final int ONE = 0;
    private static final int TWO = 1;
    private boolean isUseCardView;
    private WordViewModel wordViewModel;

    public WordViewModel getWordViewModel() {
        return wordViewModel;
    }

    public void setWordViewModel(WordViewModel wordViewModel) {
        this.wordViewModel = wordViewModel;
    }

    public void setUseCardView(boolean useCardView) {
        isUseCardView = useCardView;
    }

    public boolean isUseCardView() {
        return isUseCardView;
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder;
        if (ONE == viewType) {
            View inflate = inflater.inflate(R.layout.cell_normal_layout, parent, false);
            holder = new MyViewHolder(inflate);
        } else {
//            View inflate = inflater.inflate(R.layout.cell_card_layout, parent, false);
            View inflate = inflater.inflate(R.layout.cell_normal_2_layout, parent, false);
            holder = new MyViewHolder2(inflate);
            ((MyViewHolder2) holder).switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Word word = (Word) ((MyViewHolder2) holder).itemView.getTag(R.id.holder_ids);
                    if (word != null) {
                        if (isChecked) {
                            ((MyViewHolder2) holder).textView3.setVisibility(View.GONE);
                            word.setChinese_invisible(true);
                            wordViewModel.updateWords(word);
                        } else {
                            ((MyViewHolder2) holder).textView3.setVisibility(View.VISIBLE);
                            word.setChinese_invisible(false);
                            wordViewModel.updateWords(word);
                        }
                    }
                }
            });
        }
        (holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word word = (Word) holder.itemView.getTag(R.id.holder_ids);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (word != null) {
                    Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=" + word.getWord());
                    intent.setData(uri);
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (wordList != null && wordList.size() > 0) {
            Word word = wordList.get(position);
            if (holder instanceof MyViewHolder) {
                if (word != null) {
                    ((MyViewHolder) holder).textView.setText(String.valueOf(position + 1));
                    ((MyViewHolder) holder).textView2.setText(word.getWord());
                    ((MyViewHolder) holder).textView3.setText(word.getChineseMeaning());
                    ((MyViewHolder) holder).itemView.setTag(R.id.holder_ids, word);
                }
            } else {
                if (word != null) {
                    ((MyViewHolder2) holder).textView.setText(String.valueOf(position + 1));
                    ((MyViewHolder2) holder).textView2.setText(word.getWord());
                    ((MyViewHolder2) holder).textView3.setText(word.getChineseMeaning());
//                    ((MyViewHolder2) holder).switch2.setOnCheckedChangeListener(null);
                    ((MyViewHolder2) holder).itemView.setTag(R.id.holder_ids, word);
                    if (word.isChinese_invisible()) {
                        ((MyViewHolder2) holder).textView3.setVisibility(View.GONE);
                        ((MyViewHolder2) holder).switch2.setChecked(true);
                    } else {
                        ((MyViewHolder2) holder).textView3.setVisibility(View.VISIBLE);
                        ((MyViewHolder2) holder).switch2.setChecked(false);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return wordList == null ? 0 : wordList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!isUseCardView) {
            return ONE;
        } else {
            return TWO;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2, textView3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView4);
            textView2 = itemView.findViewById(R.id.textView5);
            textView3 = itemView.findViewById(R.id.textView6);
        }
    }

    static class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView textView, textView2, textView3;
        Switch switch2;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView4);
            textView2 = itemView.findViewById(R.id.textView5);
            textView3 = itemView.findViewById(R.id.textView6);
            switch2 = itemView.findViewById(R.id.switch2);
        }
    }
}
