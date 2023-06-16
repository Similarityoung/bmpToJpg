
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UploadServer {

    public static void main(String[] args) {

        System.out.println("服务器端运行...");
        //创建一个子线程

        //接受jpg的路径
        String recFile = "TestDir/recDir/bbb.jpg";

        Thread t=new Thread(()->{
            try (
                    //创建DatagramSocket对象，指定本地主机端口8080。
                    //【作为服务器一般应明确指定绑定的端口】
                    DatagramSocket socket=new DatagramSocket(8080);
                    FileOutputStream fout=new FileOutputStream(recFile);
                    BufferedOutputStream out=new BufferedOutputStream(fout)
            ){
                //准备一个缓冲区
                byte[] buffer=new byte[1024];
                //循环接收数据包
                while(true)
                {
                    //创建数据包对象，用来接收数据
                    DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
                    //接收数据包
                    socket.receive(packet);
                    //接收数据长度
                    int len=packet.getLength();
                    if(len==3)
                    {
                        //获得结束标志
                        String flag=new String(buffer,0,3);
                        //判断结束标志，如果是bye，则结束接收
                        if(flag.equals("bye"))
                        {
                            break;
                        }
                    }
                    //写入数据到文件输出流
                    out.write(buffer,0,len);
                }
                System.out.println("接收完成！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //启动线程
        t.start();
    }
}