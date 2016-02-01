package jps.jpsexceler;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Size;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import jps.model.FlickrUploader;

public class PhotoUploadController implements Initializable {
    
    // Single instance of MainApp
    private final MainApp app;
    
    private List<File> imageFiles;
    
    private List<String> photoUrls = new ArrayList<>();
    
    @FXML
    private Button choosePhotoButton;
    
    @FXML
    private Button uploadFlickrButton;
    
    @FXML 
    private Button closePhotoUploadPaneButton;
    
    @FXML
    private BorderPane photoUploaderBorderPane;
    
    @FXML
    private TextField photoUrlsTextField;

    public PhotoUploadController() throws InstantiationException, IllegalAccessException {
        app = MainApp.class.newInstance();
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO:
    }
    
    @FXML
    private void choosePhotoButtonClickedHandler(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Chọn ảnh sản phẩm để upload lên Flickr");
        imageFiles = fc.showOpenMultipleDialog(app.getStage());
        
        if (imageFiles == null) ;
        
        createImagePreviewPane();
        
    }
    
    private void createImagePreviewPane() throws FileNotFoundException {
        TilePane preview = new TilePane();
            
        preview.setHgap(5);
        preview.setPrefColumns(3);
        
        if (imageFiles == null)
            return;
        
        for (File im : imageFiles) {
            System.out.println(im.getAbsolutePath());
            Image image = new Image(new FileInputStream(im.getAbsolutePath()), 200, 0, true, true);
            ImageView imView = new ImageView();
            imView.maxWidth(120);
            imView.setImage(image);
            preview.getChildren().add(imView);
        }
        // Add the preview pane to main layout
        this.photoUploaderBorderPane.setCenter(preview);
    }
    
    @FXML
    private void uploadFlickrButtonClickedHandler(ActionEvent event) throws FlickrException, IOException, Exception {
        if (imageFiles == null) {
            showEmtyImageMessage();
            return;
        }
        
        FlickrUploader fu = new FlickrUploader();
        Flickr flickr = fu.getFlickr();
        String farmUrl = "";
        photoUrls.clear();
        
        for(File f: imageFiles) {
            String fName = f.getAbsolutePath();
            
            String photoId = fu.uploadfile(f.getAbsolutePath(), "Image");
            
            Collection<Size> photoSizes = flickr.getPhotosInterface().getSizes(photoId);
            for(Size s: photoSizes) {
                String photoSource = s.getSource();
                System.out.println(photoSource);

                if (photoSource.lastIndexOf("_b") > 0) {
                    farmUrl = photoSource;
                    this.photoUrls.add(farmUrl);
                }
            }
        }
        
        addUrlsToPhotoUrlsTextField();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("JPS Exceler");
        alert.setHeaderText("Upload ảnh lên FLickr");
        alert.setContentText("Đã upload thành công hình ảnh lên Flickr."
                + " Vui lòng Copy urls trong ô URL và Paste vào ô URL trong giao diện chính");
        alert.showAndWait();
    }
    
    private void showEmtyImageMessage() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("JPS Exceler");
        alert.setHeaderText("Upload ảnh lên FLickr");
        alert.setContentText("Không có hình ảnh nào được chọn. Bạn phải chọn ít nhất 01 hình ảnh để upload");
        alert.showAndWait();
    }
    
    /**
     * Pass data to main
     * @param event 
     */
    @FXML
    private void closePhotoUploadPaneButtonClickedHandler(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    private void addUrlsToPhotoUrlsTextField() {
        String urls = "";
          
        for (String url : photoUrls) {
            urls += (url + ",");
        }
        urls = urls.substring(0, urls.length() - 1);
        this.photoUrlsTextField.setText(urls);
        copyUrlsToClipboard(urls);

    }
    
    private void copyUrlsToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
}