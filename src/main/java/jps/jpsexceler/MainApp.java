package jps.jpsexceler;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jps.model.DBConnector;
import jps.model.KiotProduct;


public class MainApp extends Application {
    
    private Stage primaryStage;
    
    public static DBConnector dbConnector;
    
    private ObservableList<Node> allChildren = FXCollections.observableArrayList();
    
    // Singpleton
//    private static MainApp instance = null;
    
    public MainApp() {
        MainApp.dbConnector = new DBConnector();
    }
    
//    public static MainApp getInstance() {
//        if (instance == null)
//            instance = new MainApp();
//        return instance;
//    }

    public DBConnector getDbConnector() {
        return dbConnector;
    }
    
    
    @Override
    public void start(Stage stage) throws SQLException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("JPS Exceler");
        
        initRootLayout();
        this.primaryStage.show();
    }
    
    private void initRootLayout() throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/MainApp.fxml"));
            BorderPane root = (BorderPane) loader.load();
            root.getStylesheets().add("/styles/Styles.css");
            // Find the TableViewElement
            Scene scene = new Scene(root);
            
            this.primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Node initDataTableView() throws SQLException {
        TableView<KiotProduct> tbKiotProduct = new TableView<>();
        tbKiotProduct.setItems(MainApp.dbConnector.getAllProducts());   
        return tbKiotProduct;
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Stage getStage() {
        return this.primaryStage;
    }
    
}
