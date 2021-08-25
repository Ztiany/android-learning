package com.ztiany.androidipc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ztiany.androidipc.R;
import com.ztiany.androidipc.service.TcpServerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static com.ztiany.androidipc.Utils.close;

public class SocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        startService(new Intent(this, TcpServerService.class));
    }

    public void start(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doConnection();
            }
        }).start();
    }

    private void doConnection() {
        Socket socket = null;
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;

        try {
            socket = new Socket("127.0.0.1", 8889);
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter.print("Hello from client.");
            printWriter.flush();
            Log.d("SocketActivity", "server say: " + bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(printWriter);
            close(bufferedReader);
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, TcpServerService.class));
    }

}
