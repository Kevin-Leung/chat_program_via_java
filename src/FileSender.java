/*巨信发送文件组件_202211304116梁嘉宏*/
import java.io.*;
import java.net.Socket;

public class FileSender {
    public static void sendFile(File file, String serverIp, int serverPort) {
        try (Socket socket = new Socket(serverIp, serverPort);
             FileInputStream fileInputStream = new FileInputStream(file);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream())) {

            // 发送文件名和文件大小
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.writeLong(file.length());

            // 发送文件内容
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
