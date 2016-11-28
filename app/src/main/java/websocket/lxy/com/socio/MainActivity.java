package websocket.lxy.com.socio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


            mSocket = IO.socket("https://www.58caimi.com/", opts);

            //mSocket = IO.socket("http://www.58caimi.com/");

            //boolean connected = mSocket.connected();
            //System.out.println("connect=====isconn=="+connected);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect=====conn==" + args);
                }
            });
            mSocket.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====msg===" + args);
                }
            });
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====disconn===" + args);
                    mTv.setText("断开连接");
                }
            });
            mSocket.on(Socket.EVENT_PING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====ping===" + args.toString());
                }
            });
            mSocket.on("sub", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====sub===" + args.toString());
                }
            });
            mSocket.on("channel", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====channel===" + args.toString());
                }
            });
            mSocket.on("detail", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect====detail===" + args.toString());
                }
            });

            // mSocket.connect();

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
        mSocket.off("sub");
    }

    //conn 连接股票
    public void connServer(View view) {

        boolean connected = mSocket.connected();

        //{"symbols":["000001.SZ","000001.SS"],"channel":"detail"}
        System.out.println("connect=====connected===" + connected);

        if (connected) {
            mTv.setText("连接成功");

            try {

//                ArrayList<String> list = new ArrayList<>();
//                list.add("000001.SZ");
//                list.add("000001.SS");

                org.json.JSONArray array = new org.json.JSONArray();
                array.put(0, "000001.SZ");
                array.put(1, "000001.SS");


                JSONObject obj = new JSONObject();

                obj.put("channel", "detail");
                obj.put("symbols", array);


                Emitter emit = mSocket.emit("sub", obj);

                emit.once("sub", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("connect=====once-sub===" + args.toString());
                    }
                }).once("channel", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("connect=====once-channel===" + args.toString());
                    }
                }).once("detail", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("connect=====once-detail===" + args.toString());
                    }
                });
                //System.out.println("connect=====emit===" +emit.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }




    /*============================================================================================*/



    //连接直播
    public void connLive(View view) {
        try {
            IO.Options optslive = new IO.Options();
            optslive.forceNew = true;
            optslive.reconnection = true;
            optslive.path = "/v1/ws/live";
            Socket mJoinSocket = IO.socket("https://test.58caimi.com", optslive);

            mJoinSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect===live==conn==" + args);
                }
            });
            mJoinSocket.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect==live==msg===" + args);
                }
            });
            mJoinSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect==live==disconn===" + args);
                    mTv.setText("断开连接");
                }
            });
            mJoinSocket.on("join", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect==live==join===" + args);
                }
            });
            mJoinSocket.on("roomId", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect==live==roomid===" + args);
                }
            });

            mJoinSocket.connect();
            Thread.sleep(2000);

            boolean b = mJoinSocket.connected();
            System.out.println("connect=====live==b====" + b);

            if (b) {
                mTv.setText("直播连接");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //加入直播间
    public void clickJoin(View view) {
        Toast.makeText(MainActivity.this, "join", Toast.LENGTH_SHORT).show();

        String str = "{ roomId: '5825f1ff7f6267a11e77762f' }";
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(str);

        Map<String ,String> map= new HashMap<>();
        map.put("roomId","5825f1ff7f6267a11e77762f");
        Object json = JSON.toJSON(map);


        JSONObject obj = new JSONObject();

        try {
            obj.put("roomId", "5825f1ff7f6267a11e77762f");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("connect=========obj====="+obj);
        mSocket.emit("join", obj);



    }


}
