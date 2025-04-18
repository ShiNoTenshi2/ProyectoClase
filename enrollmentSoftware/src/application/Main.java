package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	private static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			Main.primaryStage = primaryStage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
			Parent root = loader.load(); // Ya no asumimos que es BorderPane
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Enrollment Software");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadView(String fxmlFile) {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
			Parent newView = loader.load();
			Scene scene = new Scene(newView);
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
