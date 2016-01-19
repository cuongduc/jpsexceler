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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private BorderPane photoUploaderBorderPane;

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
        FlickrUploader fu = new FlickrUploader();
        Flickr flickr = fu.getFlickr();
        String farmUrl = "";
        
        for(File f: imageFiles) {
            String fName = f.getAbsolutePath();
            
            String photoId = fu.uploadfile(f.getAbsolutePath(), "Image");
            System.out.println(photoId);
            
            Collection<Size> photoSizes = flickr.getPhotosInterface().getSizes(photoId);
            
            for(Size s: photoSizes) {
                String photoSource = s.getSource();
                if (photoSource.lastIndexOf("_o") > 0) {
                    farmUrl = photoSource;
                    this.photoUrls.add(farmUrl);
                }
            }
        }
        
        System.out.println(photoUrls);
    }
    
}