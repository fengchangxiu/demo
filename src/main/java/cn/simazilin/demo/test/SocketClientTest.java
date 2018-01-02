package cn.simazilin.demo.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-27 15:40
 * @Project:
 * @Description:
 */
public class SocketClientTest {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 5052);
        System.out.println("客户端启动成功");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter write = new PrintWriter(socket.getOutputStream());
        System.out.println("serve数据：" + in.readLine());
        String readline;
        readline = br.readLine();
        while (!readline.equals("end")) {
            write.println(readline);
            write.flush();
            System.out.println("client数据：" + readline);

            readline = br.readLine();
        }
        write.close();
        in.close();
        socket.close();
    }
}
