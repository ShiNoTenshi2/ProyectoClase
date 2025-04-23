package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Course;;

public class CourseDAO implements CRUD_Operation<Course,String> {
    private Connection connection;

    public CourseDAO(Connection connection) {
        this.connection = connection;
    }
	@Override
	public void save(Course course) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Course> fetch() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void delete(String code) {
	    String sql = "DELETE FROM COURSE WHERE CODE = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setString(1, code);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        throw new RuntimeException("Error al eliminar curso: " + e.getMessage(), e);
	    }
	}
	
	@Override
	public void update(Course course) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean authenticate(String id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ObservableList<Course> fetchObservable() {
	    ObservableList<Course> cursos = FXCollections.observableArrayList();
	    String sql = "SELECT CODE, NAME, CREDITS FROM COURSE";
	    
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        while (rs.next()) {
	            cursos.add(new Course(
	                rs.getString("CODE"),
	                rs.getString("NAME"),
	                rs.getInt("CREDITS")
	            ));
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("Error al cargar cursos: " + e.getMessage(), e);
	    }
	    return cursos;
	}

}
