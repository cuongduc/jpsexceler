package jps.jpsexceler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jps.model.DBConnector;
import jps.model.KiotProduct;
import static javafx.application.Application.launch;


public class MainApp extends Application {
    
    
    private Stage primaryStage;
    
    public static DBConnector db;
    
    private ObservableList<Node> allChildren = FXCollections.observableArrayList();
    
    
    public MainApp() {
        MainApp.db = new DBConnector();
        initProductsTable();
    }
    

    public DBConnector getDbConnector() {
        return db;
    }
    
    /**
     * Check if products table existed
     * if not, create it
     */
    private void initProductsTable() {
        try {
                db.createProductsTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void start(Stage stage) throws SQLException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("JPS Exceler");
        
        initRootLayout();
        this.primaryStage.show();
        System.out.println(System.getProperty("user.home"));
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
            this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/files/logo.jpg")));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Node initDataTableView() throws SQLException {
        TableView<KiotProduct> tbKiotProduct = new TableView<>();
        tbKiotProduct.setItems(MainApp.db.getAllProducts());   
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
