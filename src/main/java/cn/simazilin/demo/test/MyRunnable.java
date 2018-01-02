package cn.simazilin.demo.test;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-27 17:42
 * @Project:
 * @Description:
 */
public class MyRunnable implements Runnable{
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        oneServer();
    }

    private void oneServer() {
        try {
            //ServerSocket serverSocket = new ServerSocket(5052);
            //System.out.println("服务启动成功");
            ////接受数据
            //Socket socket = serverSocket.accept();
            //System.out.println("服务端开始接受数据");
            //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ////发送数据
            //PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            //BufferedReader out = new BufferedReader(new InputStreamReader(System.in));
            //String outLine;
            //outLine= out.readLine();
            //if(!outLine.equals("end")){
            //    printWriter.println(out);
            //    printWriter.flush();
            //    System.out.println("server发送数据："+out);
            //    System.out.println("server接受数据："+in.readLine());
            //    outLine= out.readLine();
            //}
            //in.close();
            //printWriter.close();
            //socket.close();
            //serverSocket.close();
        }catch (Exception e){
            System.out.println("error");
        }

    }
}
