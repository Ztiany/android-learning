package com.ztiany.androidipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.ztiany.androidipc.Utils.close;

public class TcpServerService extends Service {

    private static final String TAG = "TcpServerService";

    private volatile boolean isServiceDestroy = false;

    public TcpServerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        initServer();
    }

    private void initServer() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        ServerSocket mServerSocket;

                        try {
                            mServerSocket = new ServerSocket(8889);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        while (!isServiceDestroy) {
                            try {
                                final Socket clientSocket = mServerSocket.accept();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            responseClient(clientSocket);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }

    private void responseClient(Socket clientSocket) throws IOException {
        Log.d(TAG, "responseClient() called with: clientSocket = [" + clientSocket + "]");

        BufferedReader bufferedInputStream = null;
        PrintStream printStream = null;

        try {
            bufferedInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            printStream = new PrintStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            printStream.println("Welcome! from server.");
            printStream.flush();
            Log.d("TcpServerService", "client say: " + bufferedInputStream.readLine());
        } finally {
            close(printStream);
            close(bufferedInputStream);
            clientSocket.close();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceDestroy = true;
        Log.d(TAG, "onDestroy() called");
    }

}
