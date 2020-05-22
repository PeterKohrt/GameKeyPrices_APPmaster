package com.example.gamekeyprices_app.ui.favorites;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.FavDB;
import com.example.gamekeyprices_app.ListItem;
import com.example.gamekeyprices_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritesFragment extends Fragment {

    private RecyclerView favourite_view;
    private FavDB favDB;
    private List<ListItem> favItemList = new ArrayList<>();
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    private Map<String,ListItem> plainMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        favDB = new FavDB(getActivity());
        favourite_view = root.findViewById(R.id.fav_recycler);
        favourite_view.setHasFixedSize(true);
        favourite_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        // add item touch helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(favourite_view); // set swipe to recyclerview

        loadData();

        return root;
    }

    private void loadData() {
        if (favItemList != null) {
            favItemList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID));
                String title = cursor.getString(cursor.getColumnIndex(FavDB.GAME_TITLE));
                String image = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGE));
                ListItem favItem = new ListItem(image, title,"","","","", id);

                String request_Plain = cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID));   //TODO 1 Request only + IF ZERO / ONE / TWO OR MORE ITEMS

                String JSON_URL = "https://api.isthereanydeal.com/v01/game/info/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&plains="+request_Plain; //TODO DEPENDS ON REGION SET
                Toast.makeText(getContext().getApplicationContext(), JSON_URL, Toast.LENGTH_LONG).show();

                favItemList.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(favItemList, getContext());

        favourite_view.setAdapter(allFragmentRecyclerAdapter);

    }

    // remove item after swipe
    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition(); // get position which is swipe
            final ListItem listItem = favItemList.get(position);
            if (direction == ItemTouchHelper.RIGHT | direction == ItemTouchHelper.LEFT) { //if swipe left/right
                allFragmentRecyclerAdapter.notifyItemRemoved(position); // item removed from recyclerview
                favItemList.remove(position); //then remove item
                favDB.remove_fav(listItem.getPlain()); // remove item from database
            }
        }
    };

}