
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Main {
    public static final String DB_URL = "jdbc:h2:/../DB/OrderDB";
    public static final String DB_Driver = "org.h2.Driver";
    //блок констант

    public static void main(String[] args) {
        String createTableSQL = "CREATE TABLE Orders(" +
                "orderID BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "orderDate datetime NOT NULL," +
                "lastUpdateDate datetime NOT NULL);";
        String insertSQL = "INSERT INTO Orders (orderDate,lastUpdateDate) VALUES ( ?, ?)";
        String selectSQL = "SELECT * FROM Orders";

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Сервер запущен!");



            try {
                Class.forName(DB_Driver);
                Connection connection = DriverManager.getConnection(DB_URL);
                System.out.println("Соединение с СУБД выполнено.");
                Statement statement = connection.createStatement();
                statement.execute(createTableSQL);
                System.out.println("Таблица успешно создана!");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("JDBC драйвер для СУБД не найден!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Ошибка SQL !");
            }
            String deleteSQL = "DELETE FROM Orders";

            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.executeUpdate();
                System.out.println("Record deleted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement PreSta = connection.prepareStatement(insertSQL)) {
                PreSta.setDate(1, new Date(115,4,3) );
                PreSta.setDate(2, new Date(124,6,8));
                PreSta.executeUpdate();
                System.out.println("Record inserted successfully!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement PreSta = connection.prepareStatement(insertSQL)) {
                PreSta.setDate(1, new Date(119,9,5) );
                PreSta.setDate(2, new Date(122,6,10));
                PreSta.executeUpdate();
                System.out.println("Record inserted successfully!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement PreSta = connection.prepareStatement(insertSQL)) {
                PreSta.setDate(1, new Date(120,6,10) );
                PreSta.setDate(2, new Date(122,9,11));
                PreSta.executeUpdate();
                System.out.println("Record inserted successfully!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement PreSta = connection.prepareStatement(insertSQL)) {
                PreSta.setDate(1, new Date(122,10,2) );
                PreSta.setDate(2, new Date(123,11,10));
                PreSta.executeUpdate();
                System.out.println("Record inserted successfully!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключен!");


                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                    while (!input.ready()) ;
                    System.out.println();
                    while (input.ready()) {
                        System.out.println(input.readLine());
                    }

                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html; charset=utf-8");
                    output.println();
                    try (Connection connection = DriverManager.getConnection(DB_URL);
                         Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery(selectSQL)) {

                        while (resultSet.next()) {
                            int id = resultSet.getInt("OrderID");
                            Date OrderDate = resultSet.getDate("OrderDate");
                            Date UpdateDate = resultSet.getDate("lastUpdateDate");
                            output.print(id+" ");
                            output.print(OrderDate+" ");
                            output.print(UpdateDate+" ");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("Клиент отключен!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

