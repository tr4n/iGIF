package com.example.mypc.officaligif.database_dir;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.models.SuggestTopicModel;
import com.example.mypc.officaligif.models.TopicModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TopicDatabaseManager {
    private static final String TAG = "TopicDatabaseManager";

    private static final String TABLE_MAIN = "main_topics";
    private static final String TABLE_TOPICS = "topic_list";
    private static final String SEARCHED_TOPICS = "searched_topics";
    private static final String RECENT_TOPICS = "recent_search";
    private static final String FAVORITE_TABLE = "tbl_favorites";


    private SQLiteDatabase sqLiteDatabase;
    private TopicDatabase topicDatabase;

    private static TopicDatabaseManager TopicDatabaseManager;

    public static TopicDatabaseManager getInstance(Context context) {
        if (TopicDatabaseManager == null) {
            TopicDatabaseManager = new TopicDatabaseManager(context);
        }
        return TopicDatabaseManager;
    }

    public TopicDatabaseManager(Context context) {
        topicDatabase = new TopicDatabase(context);
        sqLiteDatabase = topicDatabase.getWritableDatabase();
    }

    @SuppressLint("LongLogTag")
    public List<SuggestTopicModel> getSuggestTopicModelList() {


        List<SuggestTopicModel> suggestTopicModelList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_MAIN, null);
        int NUMBER_TOPICS = cursor.getCount();

        for (int topic = 1; topic <= NUMBER_TOPICS; topic++) {
            cursor = sqLiteDatabase.rawQuery(
                    "select * from " + TABLE_MAIN + " , " + TABLE_TOPICS +
                            " where " + TABLE_MAIN + ".id = " +
                            TABLE_TOPICS + ".parent_id and " +
                            TABLE_TOPICS + ".parent_id = " + topic,
                    null);
            if(cursor.getCount() == 0) continue;
            cursor.moveToFirst();


            int parent_id = cursor.getInt(0);
            String parent_key = cursor.getString(1);
            int count = cursor.getInt(2);
            String color = cursor.getString(3);

            List<TopicModel> topicModelList = new ArrayList<>();


            do {
                //read data
                int id = cursor.getInt(4);
                String key = cursor.getString(5);
                String url = cursor.getString(6).trim();
                int parentid = cursor.getInt(7);

                topicModelList.add(new TopicModel(id, key, url, parentid));

                final String keySearch = key;

            } while (cursor.moveToNext());

            suggestTopicModelList.add(new SuggestTopicModel(
                    parent_id,
                    parent_key,
                    count,
                    color,
                    topicModelList
            ));
        }


        return suggestTopicModelList;


    }

    @SuppressLint("LongLogTag")
    public void saveSearchedTopic(String topic) {
        updateRecentTopic(topic);


        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + SEARCHED_TOPICS + " where " + SEARCHED_TOPICS + ".topic LIKE \"" + topic + "\"", null);

        if (cursor.getCount() == 0) {
            int id = sqLiteDatabase.rawQuery("select * from " + SEARCHED_TOPICS, null).getCount();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("topic", topic);
            contentValues.put("searching_times", 1);

            sqLiteDatabase.insert(SEARCHED_TOPICS, null, contentValues);
        } else {
            cursor.moveToFirst();
            Log.d(TAG, "saveSearchedTopic: " + cursor.getInt(0) + " " +
                    cursor.getString(1) + " " +
                    cursor.getInt(2) + " "

            );
            int searchTimes = cursor.getInt(2);
            if (searchTimes < 20) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("searching_times", (++searchTimes));
                sqLiteDatabase.update(SEARCHED_TOPICS, contentValues, "topic LIKE \"" + topic + "\"", null);

            }
            Log.d(TAG, "saveSearchedTopic: " + searchTimes);
        }


    }

    private void updateRecentTopic(String topic) {


        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + RECENT_TOPICS, null);

        if (cursor.getCount() == 0) { // if table is empty
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", 0);
            contentValues.put("topic", topic);
            sqLiteDatabase.insert(RECENT_TOPICS, null, contentValues);
            Log.d(TAG, "updateRecentTopic: " + "add First <" + topic + ">");
            Log.d(TAG, "updateRecentTopic: " + cursor.getCount());
        } else { // if table is not empty

            List<String> recentSearchList = new ArrayList<String>();
            HashSet<String> recentSearchHashSet = new HashSet<>();
            recentSearchHashSet.clear();
            recentSearchList.clear();
            cursor.moveToFirst();

            recentSearchHashSet.add(topic);
            recentSearchList.add(topic);
            do {
                String recentTopic = cursor.getString(1);
                {
                    if (recentSearchHashSet.add(recentTopic))
                        recentSearchList.add(recentTopic);
                }
            } while (cursor.moveToNext());

            Log.d(TAG, "updateRecentTopic: after while" + recentSearchList.size());
            Log.d(TAG, "updateRecentTopic: " + "add <" + topic + ">");
            sqLiteDatabase.execSQL("delete from " + RECENT_TOPICS);
            for (int id = 0; id < Math.min(10, recentSearchList.size()); id++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", id);
                contentValues.put("topic", recentSearchList.get(id));
                sqLiteDatabase.insert(RECENT_TOPICS, null, contentValues);
            }

        }
    }

    public List<MediaModel> getFavoriteList() {
        List<MediaModel> favoriteList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + FAVORITE_TABLE, null);
        if(cursor.getCount() == 0) return favoriteList;
        cursor.moveToNext();

        do {
            String id = cursor.getString(0);
            String original_url = cursor.getString(1);
            String original_width = cursor.getString(2);
            String original_height = cursor.getString(3);
            String source_tld = cursor.getString(4);
            String title = cursor.getString(5);
            String caption = cursor.getString(6);
            String fixed_height_url = cursor.getString(7);
            String fixed_height_width = cursor.getString(8);
            String fixed_height_height = cursor.getString(9);
            String fixed_height_small_url = cursor.getString(10);
            String fixed_height_small_width = cursor.getString(11);
            String fixed_height_small_height = cursor.getString(12);
            String fixed_width_url = cursor.getString(13);
            String fixed_width_width = cursor.getString(14);
            String fixed_width_height = cursor.getString(15);
            String fixed_width_small_url = cursor.getString(16);
            String fixed_width_small_width = cursor.getString(17);
            String fixed_width_small_height = cursor.getString(18);
            String fixed_width_still_url = cursor.getString(19);
            String fixed_width_still_width = cursor.getString(20);
            String fixed_width_still_height = cursor.getString(21);
            String preview_gif_url = cursor.getString(22);
            String preview_gif_width = cursor.getString(23);
            String preview_gif_height = cursor.getString(24);
            String fixed_width_downsampled_url = cursor.getString(25);
            String fixed_width_downsampled_width = cursor.getString(26);
            String fixed_width_downsampled_height = cursor.getString(27);
            String fixed_height_downsampled_url = cursor.getString(28);
            String fixed_height_downsampled_width = cursor.getString(29);
            String fixed_height_downsampled_height = cursor.getString(30);
            String original_mp4_url = cursor.getString(31);
            int position = cursor.getInt(32);

            MediaModel mediaModel = new MediaModel(
                    id,
                    original_url,
                    original_width,
                    original_height,
                    source_tld,
                    title,
                    caption,
                    fixed_height_url,
                    fixed_height_width,
                    fixed_height_height,
                    fixed_height_small_url,
                    fixed_height_small_width,
                    fixed_height_small_height,
                    fixed_width_url,
                    fixed_width_width,
                    fixed_width_height,
                    fixed_width_small_url,
                    fixed_width_small_width,
                    fixed_width_small_height,
                    fixed_width_still_url,
                    fixed_width_still_width,
                    fixed_width_still_height,
                    preview_gif_url,
                    preview_gif_width,
                    preview_gif_height,
                    fixed_width_downsampled_url,
                    fixed_width_downsampled_width,
                    fixed_width_downsampled_height,
                    fixed_height_downsampled_url,
                    fixed_height_downsampled_width,
                    fixed_height_downsampled_height,
                    original_mp4_url,
                    position
            );

            favoriteList.add(mediaModel);
        }while(cursor.moveToNext());


        return favoriteList;

    }

    public boolean inFavoriteList(MediaModel mediaModel) {
        Cursor cursor = sqLiteDatabase.rawQuery(" select * from " + FAVORITE_TABLE + " where id LIKE \"" + mediaModel.id + "\"", null);

        return (cursor.getCount() > 0);
    }

    public boolean addFavoriteItem(MediaModel mediaModel) {
        if (inFavoriteList(mediaModel)) return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", mediaModel.id);
        contentValues.put("original_url", mediaModel.original_url);
        contentValues.put("original_width", mediaModel.original_width);
        contentValues.put("original_height", mediaModel.original_height);
        contentValues.put("source_tld", mediaModel.source_tld);
        contentValues.put("title", mediaModel.title);
        contentValues.put("caption", mediaModel.caption);
        contentValues.put("fixed_height_url", mediaModel.fixed_height_url);
        contentValues.put("fixed_height_width", mediaModel.fixed_height_width);
        contentValues.put("fixed_height_height", mediaModel.fixed_height_height);
        contentValues.put("fixed_height_small_url", mediaModel.fixed_height_small_url);
        contentValues.put("fixed_height_small_width", mediaModel.fixed_height_small_width);
        contentValues.put("fixed_height_small_height", mediaModel.fixed_height_small_height);
        contentValues.put("fixed_width_url", mediaModel.fixed_width_url);
        contentValues.put("fixed_width_width", mediaModel.fixed_width_width);
        contentValues.put("fixed_width_height", mediaModel.fixed_width_height);
        contentValues.put("fixed_width_small_url", mediaModel.fixed_width_small_url);
        contentValues.put("fixed_width_small_width", mediaModel.fixed_width_small_width);
        contentValues.put("fixed_width_small_height", mediaModel.fixed_width_small_height);
        contentValues.put("fixed_width_still_url", mediaModel.fixed_width_still_url);
        contentValues.put("fixed_width_still_width", mediaModel.fixed_width_still_width);
        contentValues.put("fixed_width_still_height", mediaModel.fixed_width_still_height);
        contentValues.put("preview_gif_url", mediaModel.preview_gif_url);
        contentValues.put("preview_gif_width", mediaModel.preview_gif_width);
        contentValues.put("preview_gif_height", mediaModel.preview_gif_height);
        contentValues.put("fixed_width_downsampled_url", mediaModel.fixed_width_downsampled_url);
        contentValues.put("fixed_width_downsampled_width", mediaModel.fixed_width_downsampled_width);
        contentValues.put("fixed_width_downsampled_height", mediaModel.fixed_width_downsampled_height);
        contentValues.put("fixed_height_downsampled_url", mediaModel.fixed_height_downsampled_url);
        contentValues.put("fixed_height_downsampled_width", mediaModel.fixed_height_downsampled_width);
        contentValues.put("fixed_height_downsampled_height", mediaModel.fixed_height_downsampled_height);
        contentValues.put("original_mp4", mediaModel.original_mp4_url);
        contentValues.put("position", mediaModel.position);
        Log.d(TAG, "addFavoriteItem: " + mediaModel.title);

        sqLiteDatabase.insert(FAVORITE_TABLE, null, contentValues);
        return true;
    }

    public boolean removeFavoriteItem(MediaModel mediaModel) {
        return sqLiteDatabase.delete(FAVORITE_TABLE, "id LIKE \"" + mediaModel.id + "\"", null) > 0;
    }

    public List<String> getHistorySearchList() {
        List<String> historySearchList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SEARCHED_TOPICS + " order by searching_times", null);
        int countAll = cursor.getCount();
        if(countAll == 0) return historySearchList;
        cursor.moveToFirst();
        int pointer = 0;
        while ((pointer++) < Math.min(countAll, 15)) {
            historySearchList.add(cursor.getString(1));
            cursor.moveToNext();
        }

        return historySearchList;

    }


}
