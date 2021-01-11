package com.example.roombasicdemo.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.roombasicdemo.R;
import com.example.roombasicdemo.Word;
import com.example.roombasicdemo.WordAdapter;
import com.example.roombasicdemo.WordViewModel;
import com.example.roombasicdemo.adapter.WordAdapter2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private WordViewModel wordViewModel;
    //    private WordAdapter wordAdapter;
    private WordAdapter2 wordAdapter;
    private RecyclerView recycle;
    private FloatingActionButton floatingActionButton;
    private LiveData<List<Word>> filterWords;
    private List<Word> allWords;

    public WordsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFragment newInstance(String param1, String param2) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(requireActivity().getApplication(),
                this)).get(WordViewModel.class);
        recycle = requireActivity().findViewById(R.id.recycle);
        floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);

        if (wordAdapter == null) {
//            wordAdapter = new WordAdapter();
            wordAdapter = new WordAdapter2();
        }
        recycle.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recycle.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
                        RecyclerView.ViewHolder viewHolderForAdapterPosition = recycle.findViewHolderForAdapterPosition(i);
                        if (viewHolderForAdapterPosition != null) {
                            if (viewHolderForAdapterPosition instanceof WordAdapter2.MyViewHolder) {
                                ((WordAdapter2.MyViewHolder) viewHolderForAdapterPosition).textView.setText(String.valueOf(i + 1));
                            } else if (viewHolderForAdapterPosition instanceof WordAdapter2.MyViewHolder2) {
                                ((WordAdapter2.MyViewHolder2) viewHolderForAdapterPosition).textView.setText(String.valueOf(i + 1));
                            }
                        }
                    }
                }
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(requireContext()));
        wordAdapter.setWordViewModel(wordViewModel);
        wordAdapter.setUseCardView(true);
        recycle.setAdapter(wordAdapter);
        filterWords = wordViewModel.getListLiveData();
        filterWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int size = wordAdapter.getItemCount();
//                wordAdapter.setWordList(words);
                allWords = words;
                if (size != words.size()) {
                    wordAdapter.submitList(words);
                    if (size < words.size()) {
                        //滑动到顶部
                        recycle.smoothScrollToPosition(0);
                    }
//                    wordAdapter.notifyDataSetChanged();
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
//                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                Word wordFrom = allWords.get(viewHolder.getAdapterPosition());
//                Word wordTo = allWords.get(target.getAdapterPosition());
//                int idTemp = wordFrom.getId();
//                wordFrom.setId(wordTo.getId());
//                wordTo.setId(idTemp);
//                wordViewModel.updateWords(wordFrom,wordTo);
//                wordAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Word word = allWords.get(viewHolder.getAdapterPosition());
                wordViewModel.deleteWord(word);
                Snackbar.make(requireView().findViewById(R.id.wordsLayoutView), "删除了一个词汇", Snackbar.LENGTH_LONG)
                        .setAction("取消删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wordViewModel.insertWords(word);
                            }
                        })
                        .show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                Drawable icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_delete_24);
                Drawable background = new ColorDrawable(Color.LTGRAY);
                View itemView = viewHolder.itemView;
                if (icon != null) {
                    int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int iconLeft, iconRight, iconTop, iconBottom;
                    int backLeft, backRight, backTop, backBottom;
                    backTop = itemView.getTop();
                    backBottom = itemView.getBottom();
                    iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    iconBottom = iconTop + icon.getIntrinsicHeight();
                    if (dX > 0) {
                        backLeft = itemView.getLeft();
                        backRight = itemView.getLeft() + (int) dX;
                        background.setBounds(backLeft, backTop, backRight, backBottom);
                        iconLeft = itemView.getLeft() + iconMargin;
                        iconRight = iconLeft + icon.getIntrinsicWidth();
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    } else if (dX < 0) {
                        backLeft = itemView.getRight() + (int) dX;
                        backRight = itemView.getRight();
                        background.setBounds(backLeft, backTop, backRight, backBottom);
                        iconRight = itemView.getRight() - iconMargin;
                        iconLeft = iconRight - icon.getIntrinsicWidth();
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    } else {
                        background.setBounds(0, 0, 0, 0);
                        icon.setBounds(0, 0, 0, 0);
                    }
                    background.draw(c);
                    icon.draw(c);
                }
            }
        }).attachToRecyclerView(recycle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = ((SearchView) menu.findItem(R.id.app_bar_search).getActionView());
        DisplayMetrics outMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        searchView.setMaxWidth(widthPixels * 2 / 3);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //添加新的观察，现将之前的观察移除掉
                filterWords.removeObservers(getViewLifecycleOwner());
                filterWords = wordViewModel.findWordsWithPatten(newText.trim());
                filterWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int size = wordAdapter.getItemCount();
                        allWords = words;
//                        wordAdapter.setWordList(words);
                        wordAdapter.submitList(words);
                        if (size != words.size()) {
//                            wordAdapter.notifyDataSetChanged();
                            wordAdapter.submitList(words);
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearData:
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.clearDataTitle)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                wordViewModel.deleteAllWords();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.switchView:
                boolean useCardView = wordAdapter.isUseCardView();
                if (useCardView) {
                    wordAdapter.setUseCardView(false);
                } else {
                    wordAdapter.setUseCardView(true);
                }
                wordAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}