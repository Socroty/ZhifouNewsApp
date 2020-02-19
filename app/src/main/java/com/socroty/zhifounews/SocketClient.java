package com.socroty.zhifounews;

import android.content.Context;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {

    String getDataInfo(String data) throws IOException {

        String result;

        //Socket连接服务端
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("192.168.1.17",5232), 600);

        //发送传入数据
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        pw.println(data);

        //接受返回数据
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        result = bufferedReader.readLine();

        return result;
    }

    void getHeadlineImage(String id, Context context) throws IOException {

        Socket s = new Socket("192.168.1.17", 5233);
        String data = "get_headline_image" + "/&/" + id;
        OutputStream outs = s.getOutputStream();
        outs.write(data.getBytes());

        //创建输入流、byte数组、int型
        InputStream ins = s.getInputStream();
        byte[] buf_in = new byte[1024];
        int len_in;

        File file = new File(context.getFilesDir(), id + ".png");
        if (!file.exists()){
            boolean create_file = file.createNewFile();
            if (create_file){
                System.out.println("Create file success!");
            }else {
                System.out.println("Create file fails!");
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        while ((len_in = ins.read(buf_in)) != -1) {
            fileOutputStream.write(buf_in, 0, len_in);
        }

        fileOutputStream.flush();
        fileOutputStream.close();
        outs.close();
        ins.close();
        s.close();
    }
}
