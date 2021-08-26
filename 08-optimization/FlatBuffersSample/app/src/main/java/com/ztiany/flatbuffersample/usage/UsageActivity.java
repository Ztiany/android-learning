package com.ztiany.flatbuffersample.usage;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.flatbuffers.FlatBufferBuilder;
import com.ztiany.flatbuffersample.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class UsageActivity extends AppCompatActivity {

    private static final String TAG = UsageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);
    }


    public void serialize(View v) {
        //==================序列化========================
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int id1 = builder.createString("兰博基尼");
        //准备Car对象
        int car1 = Car.createCar(builder, 10001L, 88888L, id1);
        int id2 = builder.createString("奥迪A8");
        //准备Car对象
        int car2 = Car.createCar(builder, 10001L, 88888L, id2);
        int id3 = builder.createString("奥迪A9");
        //准备Car对象
        int car3 = Car.createCar(builder, 10001L, 88888L, id3);

        int[] cars = new int[3];
        cars[0] = car1;
        cars[1] = car2;
        cars[2] = car3;

        //创建Basic对象里面的Car集合
        int carList = Basic.createCarListVector(builder, cars);

        int name = builder.createString("jack");
        int email = builder.createString("jack@qq.com");
        int basic = Basic.createBasic(builder, 10, name, email, 100L, true, 100, carList);
        int basicOffset = Items.createBasicVector(builder, new int[]{basic});
        /*
         table Items {
             ItemId : long;
             timestemp : int;
             basic:[Basic];
         }
         */
        Items.startItems(builder);
        Items.addItemId(builder, 1000L);
        Items.addTimestemp(builder, 2016);
        Items.addBasic(builder, basicOffset);

        int rootItems = Items.endItems(builder);
        Items.finishItemsBuffer(builder, rootItems);

        //============保存数据到文件=================
        File sdcard = Environment.getExternalStorageDirectory();
        //保存的路径
        File file = new File(sdcard, "Items.txt");
        if (file.exists()) {
            file.delete();
        }
        ByteBuffer data = builder.dataBuffer();
        FileOutputStream out = null;
        FileChannel channel = null;
        try {
            out = new FileOutputStream(file);
            channel = out.getChannel();
            while (data.hasRemaining()) {
                channel.write(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //===================反序列化=============================
        FileInputStream fis = null;
        FileChannel readChannel = null;
        try {
            fis = new FileInputStream(file);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            readChannel = fis.getChannel();
            int readBytes = 0;
            while ((readBytes = readChannel.read(byteBuffer)) != -1) {
                System.out.println("读取数据个数：" + readBytes);
            }
            //把指针回到最初的状态，准备从byteBuffer当中读取数据
            byteBuffer.flip();
            //解析出二进制为Items对象。
            Items items = Items.getRootAsItems(byteBuffer);
            //读取数据测试看看是否跟保存的一致
            Log.i(TAG, "items.id:" + items.ItemId());
            Log.i(TAG, "items.timestemp:" + items.timestemp());

            Basic basic2 = items.basic(0);
            Log.i(TAG, "basic2.name:" + basic2.name());
            Log.i(TAG, "basic2.email:" + basic2.email());

            //carList
            int length = basic2.carListLength();
            for (int i = 0; i < length; i++) {
                Car car = basic2.carList(i);
                Log.i(TAG, "car.number:" + car.number());
                Log.i(TAG, "car.describle:" + car.describle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (readChannel != null) {
                    readChannel.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
