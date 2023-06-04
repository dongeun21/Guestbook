package ch10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuestbookDAO {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String JDBC_URL = "jdbc:h2:tcp://localhost/~/jwbookdb2";

    public Connection open() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(JDBC_URL, "jwbook2", "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public List<Guestbook> getAll() throws Exception {
        Connection conn = open();
        List<Guestbook> guestbookList = new ArrayList<>();
        String sql = "SELECT idx, writer, email, date, title FROM guestbook ORDER BY idx DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        try (conn; pstmt; rs) {
            while (rs.next()) {
                Guestbook g = new Guestbook();
                g.setIdx(rs.getInt("idx"));
                g.setWriter(rs.getString("writer"));
                g.setEmail(rs.getString("email"));
                g.setDate(rs.getString("date"));
                g.setTitle(rs.getString("title"));
                guestbookList.add(g);
            }
            return guestbookList;
        }
    }

    public Guestbook getGuestbook(int idx) throws SQLException {
        Connection conn = open();
        Guestbook g = new Guestbook();
        String sql = "SELECT idx, writer, email, FORMATDATETIME(date, 'yyyy-MM-dd HH:mm:ss') AS cdate, title, content FROM guestbook WHERE idx=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idx);
        ResultSet rs = pstmt.executeQuery();
        rs.next();

        try (conn; pstmt; rs) {
            g.setIdx(rs.getInt("idx"));
            g.setWriter(rs.getString("writer"));
            g.setEmail(rs.getString("email"));
            g.setDate(rs.getString("cdate"));
            g.setTitle(rs.getString("title"));
            g.setContent(rs.getString("content"));
            return g;
        }
    }

    public void addGuestbook(Guestbook g) throws Exception {
        Connection conn = open();
        String sql = "INSERT INTO guestbook(writer, email, date, title, password, content) VALUES(?, ?, CURRENT_TIMESTAMP(), ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        try (conn; pstmt) {
            pstmt.setString(1, g.getWriter());
            pstmt.setString(2, g.getEmail());
            pstmt.setString(3, g.getTitle());
            pstmt.setString(4, g.getPassword());
            pstmt.setString(5, g.getContent());

            pstmt.executeUpdate();
        }
    }
    
    public void updateGuestbook(Guestbook g) throws SQLException {
        Connection conn = open();
        String sql = "UPDATE guestbook SET writer=?, email=?, title=?, content=? WHERE idx=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        try (conn; pstmt) {
            pstmt.setString(1, g.getWriter());
            pstmt.setString(2, g.getEmail());
            pstmt.setString(3, g.getTitle());
            pstmt.setString(4, g.getContent());
            pstmt.setString(5, g.getPassword());
            pstmt.setInt(6, g.getIdx());

            pstmt.executeUpdate();
        }
    }

    public void delGuestbook(int idx) throws SQLException {
        Connection conn = open();
        String sql = "DELETE FROM guestbook WHERE idx=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        try (conn; pstmt) {
            pstmt.setInt(1, idx);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("DB에러");
            }
        }
    }
}
