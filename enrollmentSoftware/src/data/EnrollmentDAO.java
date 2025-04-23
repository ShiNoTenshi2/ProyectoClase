package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Enrollment;

public class EnrollmentDAO {
    private Connection connection;

    public EnrollmentDAO(Connection connection) {
        this.connection = connection;
    }

	public void save(Enrollment enrollment) {
		// TODO Auto-generated method stub

	}


	public ArrayList<Enrollment> fetch() {
		// TODO Auto-generated method stub
		return null;
	}


	public void update(Enrollment enrollment) {
		// TODO Auto-generated method stub

	}


	public void delete(String id, String code) {
		// TODO Auto-generated method stub

	}

	public boolean authenticate(String id, String code) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int countEnrollmentsByCourse(String courseCode) {
	    String sql = "SELECT COUNT(*) FROM ENROLLMENT WHERE COURSE_CODE = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setString(1, courseCode);
	        ResultSet rs = stmt.executeQuery();
	        return rs.next() ? rs.getInt(1) : 0;
	    } catch (SQLException e) {
	        throw new RuntimeException("Error al contar inscripciones: " + e.getMessage(), e);
	    }
	}
}
