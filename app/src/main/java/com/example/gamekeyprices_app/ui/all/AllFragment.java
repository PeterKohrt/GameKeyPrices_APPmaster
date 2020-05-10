package com.example.gamekeyprices_app.ui.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.R;
import com.example.gamekeyprices_app.ListItem;

import java.util.ArrayList;
import java.util.List;

public class AllFragment extends Fragment {


    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

   /* // DOCUMENT SNAPSHOT for PAGE
    private DocumentSnapshot lastVisible;
    private boolean isFirstPageFirstLoad = true;
    private boolean queryEnable;
   */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SET VIEW
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.fragment_all);

        // INITIALIZE BlogRecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list);
        game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        game_list_view.setAdapter(allFragmentRecyclerAdapter);

        // Inflate the layout for this fragment
        return view;
                                                                             }
    // METHOD FOR LOADING QUERY AFTER onCreate
   /* private void loadFirstQuery() {
        if (queryEnable) {
            if (firebaseAuth.getCurrentUser() != null) {
                // FIREBASE QUERY for ORDER BLOG ENTRIES
                Query firstQuery = firebaseFirestore
                        .collection("Posts")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .limit(5);


                firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e == null) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);


                                // CHECK FOR DOCUMENT CHANGE
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                                    if (doc.getType() == DocumentChange.Type.ADDED) {

                                        String blogPostId = doc.getDocument().getId();
                                        BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);

                                        blog_list.add(blogPost);

                                        blogRecyclerAdapter.notifyDataSetChanged();

                                    }
                                }
                                isFirstPageFirstLoad = false;

                            }
                        }
                    }

                });

            }
        }
    }

    // PAGINATION for QUERIES
    public void loadMorePost() {
        if(queryEnable) {
            if(firebaseAuth.getCurrentUser() != null) {

                Query nextQuery = firebaseFirestore
                        .collection("Posts")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .startAfter(lastVisible)
                        .limit(5);

                nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e == null) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);


                                // CHECK FOR DOCUMENT CHANGE
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                                        blog_list.add(blogPost);
                                        blogRecyclerAdapter.notifyDataSetChanged();

                                    }
                                }
                            }
                        }
                    }

                });
            }
        }
    }
   */

    }
