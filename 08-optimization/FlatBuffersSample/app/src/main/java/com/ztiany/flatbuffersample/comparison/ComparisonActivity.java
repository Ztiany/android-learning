package com.ztiany.flatbuffersample.comparison;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztiany.flatbuffersample.R;
import com.ztiany.flatbuffersample.comparison.flatbuffer.PeopleList;
import com.ztiany.flatbuffersample.comparison.json.PeopleListJson;

import java.nio.ByteBuffer;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-02-03 12:15
 */
public class ComparisonActivity extends AppCompatActivity {

    private TextView mTvFlatBuffer;
    private TextView mTvJson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);
        mTvFlatBuffer = (TextView) findViewById(R.id.tv_flatBuffer_time);
        mTvJson = (TextView) findViewById(R.id.tv_json_time);
    }

    public void json(View view) {
        String jsonText = new String(Utils.readRawResource(getApplication(), "sample_json.json"));
        long startTime = System.currentTimeMillis();
        PeopleListJson peopleList = new Gson().fromJson(jsonText, PeopleListJson.class);
        long timeTaken = System.currentTimeMillis() - startTime;
        String logText = "Json : " + timeTaken + "ms";
        mTvJson.setText(logText);
    }

    public void flatBuffer(View view) {
        byte[] buffer = Utils.readRawResource(getApplication(), "sample_flatbuffer.bin");
        long startTime = System.currentTimeMillis();
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        PeopleList peopleList = PeopleList.getRootAsPeopleList(bb);
        long timeTaken = System.currentTimeMillis() - startTime;
        String logText = "FlatBuffer : " + timeTaken + "ms";
        mTvFlatBuffer.setText(logText);
    }

}
