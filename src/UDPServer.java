import static java.lang.Math.*;
import java.io.*;
import java.net.*;

    public class UDPServer {
        private static final int BUFFER_SIZE = 1024;

        public static void main(String[] args) {
            try {
                DatagramSocket serverSocket = new DatagramSocket(1234);
                System.out.println("Сервер запущен и ожидает подключения...");

                while (true) {
                    byte[] receiveData = new byte[BUFFER_SIZE];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    String clientInput = new String(receivePacket.getData()).trim();
                    InetAddress clientAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();
                    System.out.println("Получены данные от клиента (" + clientAddress + ":" + clientPort + "): " + clientInput);

                    // Обрабатываем данные (выполняем операцию и сохраняем в файл)
                    String result = calculateAndSave(clientInput);

                    byte[] sendData = result.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static String calculateAndSave(String clientInput) {
            // Разбиваем строку на операнды и операцию
            String[] parts = clientInput.split(" ");
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            double z = Double.parseDouble(parts[2]);

            // Выполняем операцию
            double alfa = Math.log(Math.pow(y,-1*sqrt(abs(x))))*(x-y/2) + Math.pow(sin(Math.atan(z)),2) + Math.exp(x+y);

            // Сохраняем исходные параметры и значение функции в файл
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true))) {
                writer.write("Исходные параметры: x= " + x + ", y= " + y + ", z= " + z + " | Результат: alfa= " + alfa);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Double.toString(alfa);
        }
    }