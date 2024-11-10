import java.sql.*;
import java.util.Scanner;

public class FlightBookingSystem {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/th3";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "tung.2802";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Menu:");
                System.out.println("1. Tìm chuyến bay");
                System.out.println("2. Đặt chuyến bay");
                System.out.println("3. Xem đặt chỗ");
                System.out.println("0. Thoát");
                System.out.print("Chọn chức năng: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Đọc bỏ newline

                if (choice == 1 ) findFlights(conn, scanner);
                else if ( choice == 2 ) bookFlight(conn, scanner);
                else if (choice == 3 ) viewBookings(conn, scanner);
                else if ( choice == 0 ) System.exit(0);
                else System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!!");
            } while (choice != 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void findFlights(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nhập thành phố xuất phát: ");
        String originCity = scanner.nextLine();
        System.out.print("Nhập thành phố đích: ");
        String destCity = scanner.nextLine();

        String sql = """
        SELECT fid, flight_num, origin_city, dest_city, year, month_id, day_of_month 
        FROM Flights 
        WHERE origin_city LIKE ? AND dest_city LIKE ?""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + originCity + "%");
            stmt.setString(2, "%" + destCity + "%");
            ResultSet rs = stmt.executeQuery();

            System.out.println("Các chuyến bay phù hợp:");
            while (rs.next()) {
                System.out.println("Mã chuyến bay: " + rs.getInt("fid") +
                        ", Số hiệu chuyến bay: " + rs.getInt("flight_num") +
                        ", Thành phố xuất phát: " + rs.getString("origin_city") +
                        ", Thành phố đích: " + rs.getString("dest_city") +
                        ", Ngày bay: " + rs.getInt("day_of_month") +
                        "/" + rs.getInt("month_id") + "/" + rs.getInt("year"));
            }
        }
    }



    private static void bookFlight(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nhập mã khách hàng: ");
        int customerId = scanner.nextInt();
        System.out.print("Nhập mã chuyến bay: ");
        int flightId = scanner.nextInt();
        scanner.nextLine(); // Đọc bỏ newline
        System.out.print("Nhập hạng ghế (Economy/Business): ");
        String seatClass = scanner.nextLine();
        System.out.print("Nhập số ghế: ");
        String seatNumber = scanner.nextLine();
        System.out.print("Nhập giá: ");
        double price = scanner.nextDouble();

        // Kiểm tra chuyến bay tồn tại
        String checkFlightSql = "SELECT COUNT(*) FROM Flights WHERE fid = ?";
        try (PreparedStatement checkFlightStmt = conn.prepareStatement(checkFlightSql)) {
            checkFlightStmt.setInt(1, flightId);
            ResultSet rs = checkFlightStmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    System.out.println("Chuyến bay với mã " + flightId + " không tồn tại.");
                    return;
                }
            }
        }

        // Kiểm tra khách hàng tồn tại
        String checkCustomerSql = "SELECT COUNT(*) FROM Customers WHERE customer_id = ?";
        try (PreparedStatement checkCustomerStmt = conn.prepareStatement(checkCustomerSql)) {
            checkCustomerStmt.setInt(1, customerId);
            ResultSet rs = checkCustomerStmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    System.out.println("Khách hàng với mã " + customerId + " không tồn tại.");
                    return;
                }
            }
        }

        //Đặt chỗ
        String sql = "INSERT INTO Bookings (customer_id, flight_id, booking_date, seat_class, seat_number, price) VALUES (?, ?, CURDATE(), ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, flightId);
            stmt.setString(3, seatClass);
            stmt.setString(4, seatNumber);
            stmt.setDouble(5, price);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Đặt chỗ thành công cho khách hàng " + customerId + " trên chuyến bay " + flightId);
            }
        }
    }

    private static void viewBookings(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nhập mã khách hàng: ");
        int customerId = scanner.nextInt();

        String sql = """
        SELECT c.first_name, c.last_name, 
               b.booking_id, b.flight_id, b.booking_date, b.seat_class, b.seat_number, b.price
        FROM Bookings b
        JOIN Customers c ON b.customer_id = c.customer_id
        WHERE b.customer_id = ?""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                System.out.println("Lịch sử đặt chỗ của khách hàng: " + firstName + " " + lastName);

                // Hiển thị thông tin đặt chỗ
                do {
                    System.out.println("Mã đặt chỗ: " + rs.getInt("booking_id") +
                            ", Mã chuyến bay: " + rs.getInt("flight_id") +
                            ", Ngày đặt: " + rs.getDate("booking_date") +
                            ", Hạng ghế: " + rs.getString("seat_class") +
                            ", Số ghế: " + rs.getString("seat_number") +
                            ", Giá: " + rs.getDouble("price"));
                    System.out.println();
                } while (rs.next());
            } else {
                System.out.println("Không tìm thấy đặt chỗ nào cho khách hàng có mã " + customerId);
            }
        }
    }


}
