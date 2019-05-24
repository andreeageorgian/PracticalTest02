package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private String address;
    private int port;
    private String word;
    private String length;
    private TextView showDataTextView;

    private Socket socket;

    public ClientThread(String address, int port, String word, String length, TextView showDataTextView) {
        this.address = address;
        this.port = port;
        this.word = word;
        this.length = length;
        this.showDataTextView = showDataTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(word);
            printWriter.flush();
            printWriter.println(length);
            printWriter.flush();

            StringBuilder finfin = new StringBuilder();
            String words;
            while ((words = bufferedReader.readLine()) != null) {
                Log.d(Constants.TAG, words);
                finfin.append(words);
            }
            final String info = finfin.toString();
            showDataTextView.post(new Runnable() {
                @Override
                public void run() {

                    showDataTextView.setText(info);
                }
            });
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.d(Constants.TAG, "ERROR");
                }
            }
        }
    }
}
