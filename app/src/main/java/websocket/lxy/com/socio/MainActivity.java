package websocket.lxy.com.socio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {

    private Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {

            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = false;
            opts.path = "/stock-ws";


            mSocket = IO.socket("http://www.58caimi.com/", opts);
            //mSocket = IO.socket("http://www.58caimi.com/");

            //boolean connected = mSocket.connected();
            //System.out.println("connect=====isconn=="+connected);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect=====conn==");
                }
            }).on("channel", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====new===");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====disconn===");
                }
            });


            mSocket.connect();
            System.out.println("connect====lianjie===");


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
        System.out.println("connect=====isconn==" + connected);

        if (connected){
            String  str = "{ channel: 'detail', symbols: ['000001.SZ', '000001.SS'] }";
            Object parse = JSON.parse(str);
            System.out.println("parseparse======="+parse);

            mSocket.emit("sub", parse);
        }


    }
}
