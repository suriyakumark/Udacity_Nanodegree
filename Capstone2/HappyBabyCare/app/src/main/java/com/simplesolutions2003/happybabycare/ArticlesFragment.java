package com.simplesolutions2003.happybabycare;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.simplesolutions2003.happybabycare.data.AppContract;

/**
 * Created by SuriyaKumar on 8/20/2016.
 */
public class ArticlesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public final static boolean KEEP_IN_STACK = true;
    public final static String TAG = ArticlesFragment.class.getSimpleName();

    private final static int ARTICLES_LOADER = 0;
    private int dPosition;
    private ArticlesAdapter articlesAdapter;
    public static String ARTICLE_TYPE = "stories";
    RecyclerView articlesRecyclerView;

    private static final String[] ARTICLE_COLUMNS = {
            AppContract.ArticleEntry.TABLE_NAME + "." + AppContract.ArticleEntry._ID,
            AppContract.ArticleEntry.TABLE_NAME + "." + AppContract.ArticleEntry.COLUMN_TYPE,
            AppContract.ArticleEntry.TABLE_NAME + "." + AppContract.ArticleEntry.COLUMN_CATEGORY,
            AppContract.ArticleEntry.TABLE_NAME + "." + AppContract.ArticleEntry.COLUMN_TITLE,
            AppContract.ArticleEntry.TABLE_NAME + "." + AppContract.ArticleEntry.COLUMN_COVER_PIC
    };


    static final int COL_ARTICLE_ID = 0;
    static final int COL_ARTICLE_TYPE = 1;
    static final int COL_ARTICLE_CATEGORY = 2;
    static final int COL_ARTICLE_TITLE = 3;
    static final int COL_ARTICLE_COVER_PIC = 4;


    public interface Callback {
    }

    public ArticlesFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.articles, container, false);
        articlesRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }


    public void onResume()
    {
        super.onResume();
        switch(ARTICLE_TYPE){
            case "stories":
                ((MainActivity) getActivity()).updateToolbarTitle("Stories");
                break;
            case "rhymes":
                ((MainActivity) getActivity()).updateToolbarTitle("Rhymes");
                break;
            default:
                break;
        }
        ArticleDetailFragment.ARTICLE_TITLE = "";
        getLoaderManager().initLoader(ARTICLES_LOADER, null, this);
    }

    //check which loader is initiated and get appropriate cursor using content provider
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(TAG, "onCreateLoader - " + i + " loader");

        Uri buildArticle = AppContract.ArticleEntry.buildArticleByTypeUri(ARTICLE_TYPE);

        return new CursorLoader(getActivity(),
                buildArticle,
                ARTICLE_COLUMNS,
                null,
                null,
                null);

    }

    //check which loader has completed and use the data accordingly
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(TAG, "onLoadFinished - " + loader.getId() + " loader - " + cursor.getCount() + " rows retrieved");

        articlesAdapter = new ArticlesAdapter(getActivity(),cursor,0);
        articlesAdapter.setHasStableIds(true);
        articlesRecyclerView.setAdapter(articlesAdapter);
        int columnCount = 2;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        articlesRecyclerView.setLayoutManager(sglm);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        articlesRecyclerView.setAdapter(null);
    }


}
