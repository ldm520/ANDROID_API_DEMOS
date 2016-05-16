/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.app;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * 利用类加载器Loader对自定义的内容提供者共享的数据进行管理
 * 
 * @description：
 * @author ldm
 * @date 2016-5-16 上午11:27:03
 */
public class LoaderThrottle extends Activity {
	// Log标签TAG
	static final String TAG = "LoaderThrottle";

	// 定义主机名，用以拼接Uri，Uri表明了内容提供的地址，外部应用通过Uri访问内容提供者，来实现对数据的增删改查
	public static final String AUTHORITY = "com.example.android.apis.app.LoaderThrottle";

	/*
	 * 定义一个类用于定义关于内容提供者和工作表的一些常量，该类实现了BaseColumns接口，表示将继承该接口
	 * _id和_icount两列，我们无需再定义就会在表中创建出这两个列
	 */
	public static final class MainTable implements BaseColumns {
		private MainTable() {// 私有构造，外部不能调用
		}

		// 定义表的名字
		public static final String TABLE_NAME = "main";
		// 定义这张表的uri
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/main");
		// 定义表中某个条目（某行数据）的Uri的前面公共的部分，使用时我们还需要在后面添加条目的Id
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://"
				+ AUTHORITY + "/main/");
		// 定义Uri的命名机制，/前面的部分是android系统定义的，不能改变，/后面的部分可以自定义任意字符串
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.api-demos-throttle";
		// 定义某个条目的Uri的命名机制
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.api-demos-throttle";
		// 定义默认的排序方式
		public static final String DEFAULT_SORT_ORDER = "data COLLATE LOCALIZED ASC";
		// 定义列的名字，本例中只有一列
		public static final String COLUMN_NAME_DATA = "data";
	}

	/**
	 * 数据库操作帮助类
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-16 上午11:31:01
	 */
	static class DatabaseHelper extends SQLiteOpenHelper {
		// 数据库名称
		private static final String DATABASE_NAME = "loader_throttle.db";
		// 数据库版本
		private static final int DATABASE_VERSION = 2;

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// 创建数据库中的表
			db.execSQL("CREATE TABLE " + MainTable.TABLE_NAME + " ("
					+ MainTable._ID + " INTEGER PRIMARY KEY,"
					+ MainTable.COLUMN_NAME_DATA + " TEXT" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS notes");// 删除表
			onCreate(db);// 再创建表
		}
	}

	/**
	 * A very simple implementation of a content provider.
	 */
	public static class SimpleProvider extends ContentProvider {
		// 定义一个集合，把从数据库中选出的列映射到该集合中
		private Map<String, String> mNotesProjectionMap;
		// 定义Uri的匹配器，用于解析传入的Uri
		private UriMatcher mUriMatcher;
		// 定义当Uri匹配时的返回码
		// 匹配整个表时的返回码
		private static final int MAIN = 1;
		// 匹配某一行时的返回码
		private static final int MAIN_ID = 2;
		private DatabaseHelper mOpenHelper;

		public SimpleProvider() {
			// 为内容提供者注册Uri
			mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
			mUriMatcher.addURI(AUTHORITY, MainTable.TABLE_NAME, MAIN);
			// #表示通配符
			mUriMatcher.addURI(AUTHORITY, MainTable.TABLE_NAME + "/#", MAIN_ID);

			mNotesProjectionMap = new HashMap<String, String>();
			mNotesProjectionMap.put(MainTable._ID, MainTable._ID);
			mNotesProjectionMap.put(MainTable.COLUMN_NAME_DATA,
					MainTable.COLUMN_NAME_DATA);
		}

		@Override
		public boolean onCreate() {
			// 创建数据库和数据表
			mOpenHelper = new DatabaseHelper(getContext());
			return true;
		}

		/*
		 * 用于供外部应用从内容提供者中获取数据
		 */
		@Override
		public Cursor query(Uri uri, String[] projection, String selection,
				String[] selectionArgs, String sortOrder) {
			// 使用Sql查询语句构建的辅助类
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(MainTable.TABLE_NAME);
			// 根据匹配Uri的返回码来判定是查询整个数据表，还是查询某条数据
			switch (mUriMatcher.match(uri)) {
			case MAIN:
				// 查询整个表
				qb.setProjectionMap(mNotesProjectionMap);
				break;
			case MAIN_ID:
				qb.setProjectionMap(mNotesProjectionMap);
				// 追加筛选条件
				qb.appendWhere(MainTable._ID + "=?");
				// 获取查询参数即所要查询条目的Id
				selectionArgs = DatabaseUtils.appendSelectionArgs(
						selectionArgs,
						new String[] { uri.getLastPathSegment() });
				break;

			default:
				throw new IllegalArgumentException("UnKnown Uri" + uri);
			}

			// 如果没定义排序规则，则按照默认的规则进行排序
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = MainTable.DEFAULT_SORT_ORDER;
			}

			// 获取到可读的数据库
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			Cursor c = qb.query(db, projection, selection, selectionArgs, null,
					null, sortOrder);
			// 监听uri的变化
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		}

		/*
		 * 返回对应Uri MIME类型用以验证数据的合法性
		 */
		@Override
		public String getType(Uri uri) {
			switch (mUriMatcher.match(uri)) {
			case MAIN:
				return MainTable.CONTENT_TYPE;
			case MAIN_ID:
				return MainTable.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("UnKnown Uri" + uri);
			}
		}

		/*
		 * 用于外部应用向内容提供者中插入数据
		 */
		@Override
		public Uri insert(Uri uri, ContentValues values) {
			// 只能插入到数据表中
			if (mUriMatcher.match(uri) != MAIN) {
				throw new IllegalArgumentException("UnKnown Uri" + uri);
			}
			if (values != null) {
				SQLiteDatabase db = mOpenHelper.getWritableDatabase();
				long row_Id = db.insert(MainTable.TABLE_NAME, null, values);
				// 如果插入成功，则插入行的Id存在
				if (row_Id > 0) {
					// ContentUris用于操作Uri路径后面的Id部分
					Uri noteUri = ContentUris.withAppendedId(
							MainTable.CONTENT_ID_URI_BASE, row_Id);
					// 必须设置对Uri的监听，不然loader无法获取到数据库的更新，不能实现实时更新
					getContext().getContentResolver().notifyChange(noteUri,
							null);
					// 返回所插入条目的Uri
					return noteUri;
				}
			}
			throw new SQLException("Failed to insert row into " + uri);
		}

		/*
		 * 用于外部应用删除内容提供者中的数据
		 */
		@Override
		public int delete(Uri uri, String where, String[] whereArgs) {
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			String findWhere;
			int count = -1;

			switch (mUriMatcher.match(uri)) {
			case MAIN:
				count = db.delete(MainTable.TABLE_NAME, where, whereArgs);
				break;
			case MAIN_ID:
				// 组装查询条件
				findWhere = DatabaseUtils.concatenateWhere(MainTable._ID + "="
						+ ContentUris.parseId(uri), where);
				count = db.delete(MainTable.TABLE_NAME, findWhere, whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
			}

			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}

		@Override
		public int update(Uri uri, ContentValues values, String where,
				String[] whereArgs) {
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			String findWhere;
			int count = -1;

			switch (mUriMatcher.match(uri)) {
			case MAIN:
				count = db.update(MainTable.TABLE_NAME, values, where,
						whereArgs);
				break;
			case MAIN_ID:
				// 组装查询条件
				findWhere = DatabaseUtils.concatenateWhere(MainTable._ID + "="
						+ ContentUris.parseId(uri), where);
				count = db.update(MainTable.TABLE_NAME, values, findWhere,
						whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
			}

			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentById(android.R.id.content) == null) {
			LoaderThrottleFragment frg = new LoaderThrottleFragment();
			fm.beginTransaction().add(android.R.id.content, frg).commit();
		}
	}

	public static class LoaderThrottleFragment extends ListFragment {
		private static final int POPULATE_ID = Menu.FIRST;
		private static final int CLEAR_ID = Menu.FIRST + 1;
		private SimpleCursorAdapter mAdapter;
		private AsyncTask<Void, Void, Void> mPopulatingTask;
		final String[] projection = new String[] { MainTable._ID,
				MainTable.COLUMN_NAME_DATA };

		private LoaderCallbacks<Cursor> myLoader = new LoaderCallbacks<Cursor>() {

			@Override
			public Loader<Cursor> onCreateLoader(int id, Bundle args) {
				CursorLoader c = new CursorLoader(getActivity(),
						MainTable.CONTENT_URI, projection, null, null, null);
				System.out.println("数据变化了吗？");
				// 最多每2秒更新一次
				c.setUpdateThrottle(2000);
				return c;
			}

			@Override
			public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
				System.out.println("数据准备完毕了吗？" + data.getCount());
				mAdapter.swapCursor(data);
				if (isResumed()) {
					setListShown(true);
				} else {
					setListShownNoAnimation(true);
				}
			}

			@Override
			public void onLoaderReset(Loader<Cursor> loader) {
				mAdapter.swapCursor(null);
			}
		};

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setEmptyText("No data.  Select 'Populate' to fill with data from Z to A at a rate of 4 per second.");
			setHasOptionsMenu(true);
			mAdapter = new SimpleCursorAdapter(getActivity(),
					android.R.layout.simple_list_item_1, null,
					new String[] { MainTable.COLUMN_NAME_DATA },
					new int[] { android.R.id.text1 }, 0);
			setListAdapter(mAdapter);
			setListShown(false);

			getLoaderManager().initLoader(111, null, myLoader);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			menu.add(Menu.NONE, POPULATE_ID, 0, "populate").setShowAsAction(
					MenuItem.SHOW_AS_ACTION_IF_ROOM);
			menu.add(Menu.NONE, CLEAR_ID, 0, "clear").setShowAsAction(
					MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case POPULATE_ID:
				// 添加数据
				addToDatabase();
				return true;
			case CLEAR_ID:
				deleteData();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}

		/**
		 * 删除操作
		 * 
		 * @description：
		 * @author ldm
		 * @date 2016-5-16 上午11:35:05
		 */
		private void deleteData() {
			if (mPopulatingTask != null) {
				mPopulatingTask.cancel(false);
				mPopulatingTask = null;
			}
			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					getActivity().getContentResolver().delete(
							MainTable.CONTENT_URI, null, null);
					return null;
				}
			};
			task.execute((Void[]) null);
		}

		private void addToDatabase() {
			// 如果异步任务不为空，则取消任务
			if (mPopulatingTask != null) {
				mPopulatingTask.cancel(false);
				mPopulatingTask = null;
			}
			// 开启线程向数据库中添加内容
			mPopulatingTask = new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// 向数据库中添加内容
					for (char c = 'z'; c >= 'a'; c--) {
						if (isCancelled()) {
							break;
						}
						StringBuilder sb = new StringBuilder("Data ");
						sb.append(c);
						ContentValues values = new ContentValues();
						values.put(MainTable.COLUMN_NAME_DATA, sb.toString());
						Uri rowUri = getActivity().getContentResolver().insert(
								MainTable.CONTENT_URI, values);
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					return null;
				}

			};
			// 使用系统默认的线程池来管理线程
			mPopulatingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					(Void[]) null);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			Toast.makeText(getActivity(), "Item click:" + id, 0).show();
		}
	}
}
