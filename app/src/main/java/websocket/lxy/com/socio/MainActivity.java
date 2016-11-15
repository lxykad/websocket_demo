package websocket.lxy.com.socio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    private TextView mTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv = (TextView) findViewById(R.id.tv_conn);


        try {

            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            opts.path = "/stock-ws";


            mSocket = IO.socket("http://www.58caimi.com/", opts);
            mSocket.connect();
            //mSocket = IO.socket("http://www.58caimi.com/");

            //boolean connected = mSocket.connected();
            //System.out.println("connect=====isconn=="+connected);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect=====conn==" + args);
                }
            }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====msg===" + args);
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====disconn===" + args);
                    mTv.setText("断开连接");
                }
            }).on(Socket.EVENT_PING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====ping===");
                }
            }).on("", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====server===" + args.toString());
                }
            });


            /**
             *
             * // Sending an object
             JSONObject obj = new JSONObject();
             obj.put("hello", "server");
             obj.put("binary", new byte[42]);
             socket.emit("foo", obj);

             // Receiving an object
             socket.on("foo", new Emitter.Listener() {
            @Override public void call(Object... args) {
            JSONObject obj = (JSONObject)args[0];
            }
            });
             */


            //发送数据
            //mSocket.emit("from_client", "client_message");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //断开连接，取消监听
        mSocket.disconnect();
        mSocket.off("new_event");
    }

    //conn
    public void connServer(View view) {

        boolean connected = mSocket.connected();

        //{"symbols":["000001.SZ","000001.SS"],"channel":"detail"}

        if (connected) {
            mTv.setText("连接成功");

            JSONObject obj = new JSONObject();

            try {
                obj.put("channel", "detail");
                obj.put("symbols", "['000001.SZ','000001.SS']");

                System.out.println("connect=====obj===" +obj);

                mSocket.emit("sub", obj);
                

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
