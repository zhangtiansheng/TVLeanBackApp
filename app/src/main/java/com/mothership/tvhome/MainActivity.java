package com.mothership.tvhome;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.mothership.tvhome.app.MainFragment;
import com.mothership.tvhome.view.AdView;
import com.mothership.tvhome.view.EmptyLoadingView;
import com.mothership.tvhome.widget.BlockHorizontalPresenter;
import com.mothership.tvhome.widget.CardPresenter;
import com.mothership.tvhome.widget.DisplayItemSelector;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.video.TabsGsonLoader;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<GenericBlock<DisplayItem>>, AdView.AdListener {

    private static final String TAG = "MainActivity";
    protected DisplayItem item;
    protected EmptyLoadingView mLoadingView;
    protected BaseGsonLoader mLoader;
    DisplayItemSelector mDiSel = new DisplayItemSelector();
    ArrayObjectAdapter mAdapter = new ArrayObjectAdapter(new CardPresenter());
    Handler mHandler = new Handler();
    private long mPreKeytime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        convert2Adapter(null);
        setContentView(R.layout.activity_main);
        MainFragment mf = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_browse_fragment);
        mf.setAdapter(mAdapter);

        item = (DisplayItem) this.getIntent().getSerializableExtra("item");

        getLoaderManager().initLoader(TabsGsonLoader.LOADER_ID, null, MainActivity.this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            long time = System.currentTimeMillis();
            if (time - mPreKeytime < 100) {
                return true;
            }
            mPreKeytime = System.currentTimeMillis();
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public Loader<GenericBlock<DisplayItem>> onCreateLoader(int loaderId, Bundle args) {
        if (loaderId == TabsGsonLoader.LOADER_ID) {
            createTabsLoader();
            mLoader.forceLoad();
            return mLoader;
        } else {
            return null;
        }
    }

    //please override this fun
    protected void createTabsLoader() {
        mLoader = new TabsGsonLoader(this, item);
    }


    @Override
    public void onLoadFinished(Loader<GenericBlock<DisplayItem>> loader, GenericBlock<DisplayItem> data) {
        //data returned
        Log.d("MainActivity", "dataloaded" + data.toString());
        convert2Adapter(data);
        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
        MainFragment mainFragment = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.main_browse_fragment);
        if (mainFragment != null) {
//            mainFragment.LoadData(data);
        }
    }


    void convert2Adapter(GenericBlock<DisplayItem> aSrc) {
        mAdapter.removeItems(0, mAdapter.size());
//        mAdapter = new ArrayObjectAdapter();

        for (Block<DisplayItem> blk : aSrc.blocks) {
            ArrayObjectAdapter pageAdt = new ArrayObjectAdapter(new BlockHorizontalPresenter());
            ArrayObjectAdapter listAdt = new ArrayObjectAdapter(mDiSel);
            flattenBlock(blk, listAdt, pageAdt);
            Log.d(TAG, "add block " + blk.title);
            mAdapter.add(pageAdt);
        }
    }

    void flattenBlock(Block<DisplayItem> aBlk, ArrayObjectAdapter aLstAdt, ArrayObjectAdapter aPageAdt) {
        if (aBlk.items != null) {
            for (DisplayItem itm : aBlk.items) {
                aLstAdt.add(itm);
            }
        }
        if (aBlk.blocks != null) {
            for (Block<DisplayItem> blk : aBlk.blocks) {
                ArrayObjectAdapter listAdt = new ArrayObjectAdapter(mDiSel);
                flattenBlock(blk, listAdt, aPageAdt);
                if (listAdt.size() > 0) {
                    Log.d(TAG, "add row for page " + blk.title + " cnt " + listAdt.size());
                    aPageAdt.add(new ListRow(new HeaderItem(blk.title), listAdt));
                }
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<GenericBlock<DisplayItem>> loader) {

    }


}
