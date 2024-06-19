/*巨信接收文件组件_202211304116梁嘉宏*/
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(10580)) {
            System.out.println("文件接收器启动...");
            while (true) {
                new FileHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class FileHandler extends Thread {
        private Socket socket;

        public FileHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                 DataInputStream dataInputStream = new DataInputStream(bufferedInputStream)) {

                // 接收文件名和文件大小
                String fileName = dataInputStream.readUTF();
                long fileSize = dataInputStream.readLong();
                File file = new File("received_" + fileName);

                try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

                    // 接收文件内容
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    long totalBytesRead = 0;
                    while (totalBytesRead < fileSize && (bytesRead = bufferedInputStream.read(buffer)) != -1) {
                        bufferedOutputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                    }
                    bufferedOutputStream.flush();
                }

                System.out.println("文件接收完成: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
