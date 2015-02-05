package com.example.riversystem;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * use ListView and CursorAdapter,FragmentActivity
 * @author Administrator
 *
 */
public class RiverSystemActivity extends FragmentActivity{
	
	private ListView riverListView;
	private CursorAdapter adapter;
	private CursorLoader cursorLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_river_system);
        
        riverListView=(ListView) findViewById(R.id.riverList);
        initRiverCursorLoader();
//        initRiverListView();
    }

	//use CursorLoader to adapter listView data
    private void initRiverCursorLoader() {
		// TODO Auto-generated method stub
    	//First, create (and set up) the CursorAdapter.the cursor is null
		adapter=new SimpleCursorAdapter(this, R.layout.row, null,
				new String[]{
				RiverContentProvider.NAME,RiverContentProvider.INTRODUCTION},
				new int[]{R.id.riverName,R.id.riverIntroduction},
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		//Next, initialize the loader.
		getSupportLoaderManager().initLoader(0,null,
				new LoaderCallbacks<Cursor>(){

					/**The LoaderManager calls onCreateLoader(int id, Bundle args).
					 * returns a subclass of the Loader<Cursor>
					 * This Cursorloader will perform the initial query and will update itself 
					 * when the data set changes.
					 */
					@Override
					public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
						Log.d("listview","on create loader");
						cursorLoader=new CursorLoader(
								RiverSystemActivity.this,
								RiverContentProvider.CONTENT_URI,new String[]{
								RiverContentProvider._ID,
								RiverContentProvider.NAME,
								RiverContentProvider.INTRODUCTION},null,null,null
								);
						return cursorLoader;
					}

					/**
					 * The queried cursor is passed to the adapter.
					 * Immediately after the CursorLoader is instantiated and returned , 
					 * the CursorLoader performs the initial query on a separate thread and a cursor is returned. 
					 * When the CursorLoader finishes the query, it returns the newly queried cursor to the LoaderManager,
					 * which then passes the cursor to the onLoadFinished method. 
					 * From the documentation, "the onLoadFinished method is called 
					 * when a previously created loader has finished its load."
					 * @param arg0
					 * @param cursor
					 */
					@Override
					public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
						Log.d("listview","on create finished");
						adapter.swapCursor(cursor);
						
					}

					@Override
					public void onLoaderReset(Loader<Cursor> arg0) {
						Log.d("listview","on create reset");
						adapter.swapCursor(null);
					}
			
		});
		riverListView.setAdapter(adapter);
		riverListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("listItem", ">>" + id);
				Intent intent=new Intent();
				intent.setClass(RiverSystemActivity.this, RiverDetailActivity.class);
				intent.putExtra(GlobalValues.RIVER_ID, id);
				startActivity(intent);
				
			}
			
		});		
	}    
}
