package controller;

import java.sql.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Course;
import application.Main;
import data.CourseDAO;
import data.DBConnection;
import data.EnrollmentDAO;

public class CoursesController {
    
    // ============ CAMPOS DEL FORMULARIO ============
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCreditos;
    
    // ============ COMPONENTES DE TABLA ============
    @FXML private TableView<Course> tblCursos;
    @FXML private TableColumn<Course, String> colCodigo;
    @FXML private TableColumn<Course, String> colNombre;
    @FXML private TableColumn<Course, Integer> colCreditos;
    
    // ============ BOTONES ============
    @FXML private Button btnRegistrar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnVerEstudiantes;
    @FXML private Button btnMenu;
    
    // ============ CONEXIÓN A DATOS ============
    private CourseDAO courseDAO = new CourseDAO(DBConnection.getInstance().getConnection());
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO(DBConnection.getInstance().getConnection());
    
    // ============ INICIALIZACIÓN ============
    @FXML
    public void initialize() {
        configurarTabla();
        cargarCursos();
    }
    
    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("code"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCreditos.setCellValueFactory(new PropertyValueFactory<>("credits"));
        
        tblCursos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    llenarCamposConCurso(newSelection);
                }
            });
    }
    
    private void cargarCursos() {
        tblCursos.getItems().setAll(courseDAO.fetchObservable());
    }
    
    // ============ MÉTODOS DE ACCIÓN ============
    @FXML
    void registrarCurso(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String creditosStr = txtCreditos.getText().trim();
        
        if (!validarCampos(codigo, nombre, creditosStr)) return;
        if (!validarCodigo(codigo)) return;
        if (!validarCreditos(creditosStr)) return;
        
        try {
            if (courseDAO.existsCode(codigo)) {
                mostrarAlerta("Error", "Código duplicado", "Este código de curso ya está registrado.");
                return;
            }
            
            int creditos = Integer.parseInt(creditosStr);
            Course curso = new Course(codigo, nombre, creditos);
            courseDAO.save(curso);
            
            mostrarAlerta("Éxito", "Registro exitoso", "Curso registrado correctamente.");
            limpiarCampos();
            cargarCursos();
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error en base de datos", e.getMessage());
        }
    }
    
    @FXML
    void actualizarCurso(ActionEvent event) {
        Course cursoSeleccionado = tblCursos.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado == null) {
            mostrarAlerta("Error", "Selección requerida", "Por favor seleccione un curso de la tabla.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String creditosStr = txtCreditos.getText().trim();

        if (!validarCampos(cursoSeleccionado.getCode(), nombre, creditosStr)) return;
        if (!validarCreditos(creditosStr)) return;

        try {
            int creditos = Integer.parseInt(creditosStr); 
            cursoSeleccionado.setName(nombre);
            cursoSeleccionado.setCredits(creditos);

            courseDAO.update(cursoSeleccionado);
            
            mostrarAlerta("Éxito", "Actualización exitosa", "Curso actualizado correctamente.");
            cargarCursos();
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error en base de datos", e.getMessage());
        }
    }
    
    @FXML
    void eliminarCurso(ActionEvent event) {
        Course cursoSeleccionado = tblCursos.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado == null) {
            mostrarAlerta("Error", "Selección requerida", "Por favor seleccione un curso de la tabla.");
            return;
        }
        
        try {
            if (enrollmentDAO.countEnrollmentsByCourse(cursoSeleccionado.getCode()) > 0) {
                mostrarAlerta("Error", "No se puede eliminar", 
                    "El curso tiene estudiantes inscritos. Elimine las inscripciones primero.");
                return;
            }
            
            courseDAO.delete(cursoSeleccionado.getCode());
            
            mostrarAlerta("Éxito", "Eliminación exitosa", "Curso eliminado correctamente.");
            limpiarCampos();
            cargarCursos();
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error en base de datos", e.getMessage());
        }
    }
    
    @FXML
    void verEstudiantesInscritos(ActionEvent event) {
        Course cursoSeleccionado = tblCursos.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado == null) {
            mostrarAlerta("Error", "Selección requerida", "Por favor seleccione un curso de la tabla.");
            return;
        }
        
        // Implementar lógica para mostrar estudiantes inscritos
        mostrarAlerta("Información", "Estudiantes inscritos", 
            "Mostrando estudiantes del curso: " + cursoSeleccionado.getName());
    }
    
    @FXML
    void irAlMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }
    
    // ============ VALIDACIONES ============
    private boolean validarCampos(String codigo, String nombre, String creditos) {
        if (codigo.isEmpty() || nombre.isEmpty() || creditos.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }
    
    private boolean validarCodigo(String codigo) {
        // Puedes agregar validación específica para el código del curso si es necesario
        // Ejemplo: if (!codigo.matches("[A-Z]{3}\\d{3}")) { ... }
        return true;
    }
    
    private boolean validarCreditos(String creditos) {
        try {
            int cred = Integer.parseInt(creditos);
            if (cred <= 0) {
                mostrarAlerta("Error", "Créditos inválidos", "Los créditos deben ser mayores a cero.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Créditos inválidos", "Los créditos deben ser un número entero.");
            return false;
        }
    }
    
    // ============ MÉTODOS AUXILIARES ============
    private void llenarCamposConCurso(Course curso) {
        txtCodigo.setText(curso.getCode());
        txtNombre.setText(curso.getName());
        txtCreditos.setText(String.valueOf(curso.getCredits()));
    }
    
    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtCreditos.clear();
    }
    
    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}