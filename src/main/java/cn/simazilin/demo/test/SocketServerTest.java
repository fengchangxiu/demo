package cn.simazilin.demo.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-27 15:40
 * @Project:
 * @Description:
 */
public class SocketServerTest {
    public static void main(String[] args) throws Exception {
        SocketServerTest socketServerTest = new SocketServerTest();
        socketServerTest.oneServer();
    }

    private void oneServer() throws Exception {
        ServerSocket serverSocket = new ServerSocket(5052);
        System.out.println("服务启动成功");
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("clien数据："+in.readLine());
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader out = new BufferedReader(new InputStreamReader(System.in));
        String readLine;
        readLine= out.readLine();
        while (!readLine.equals("end")){
            writer.println(readLine);
            writer.flush();
            System.out.println("server数据："+readLine);
            readLine= out.readLine();
        }
        in.close();
        writer.close();
        socket.close();
        serverSocket.close();
    }
}
