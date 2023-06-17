import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UploadClient {

    public static void main(String[] args) throws IOException, AWTException {
        System.out.println("客户端运行...");


        //bmp的路径
        String bmpFile = "TestDir/bbb.bmp";
        //捕获桌面图象
        CatchScreen.screen(bmpFile);
        //发送jpg的路径
        String senFile = "TestDir/senDir/bbb.jpg";
        //将bmp转换为jpg
        BmpReader.bmpToJpg(bmpFile,senFile);

        try (
                //创建DatagramSocket对象，由系统分配可以使用的端口
                DatagramSocket socket=new DatagramSocket();
                //创建文件输入流
                FileInputStream fin=new FileInputStream(senFile);
                //由文件输入流创建缓冲输入流
                BufferedInputStream in=new BufferedInputStream(fin)
        ){
            //创建远程主机IP地址对象
            InetAddress address=InetAddress.getByName("localhost");

            //准备一个缓冲区
            byte[] buffer=new byte[1024];
            //首次读取文件
            int len=in.read(buffer);
            while(len!=-1)
            {
                //创建数据报包对象
                DatagramPacket packet=new DatagramPacket(buffer,len,address,8080);
                //发送数据包
                socket.send(packet);
                //再次读取文件
                len=in.read(buffer);
            }
            //创建数据报对象
            DatagramPacket packet=new DatagramPacket("bye".getBytes(),3,address,8080);
            //发送结束标志
            socket.send(packet);
            System.out.println("上传成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}