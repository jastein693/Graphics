package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class MoreKernels extends Application {
    
    static Image inputImageJfx;
    static MyImage outputImageJfx = new MyImage(1,1);
    
    static List<MyImage> undoStack = new ArrayList<MyImage>();
    static List<MyImage> redoStack = new ArrayList<MyImage>();
    
    static FileChooser fileChooser = new FileChooser();
    

    public static void main(String[] args) throws Exception{
        
        //inputImageJfx = new Image(new FileInputStream(System.getProperty("user.home") + "/Desktop/photo.jpg"));;
        
       // outputImageJfx = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());;
        
        //outputImageJfx.copyFrom(inputImageJfx);
        
        
        //outputImageJfx.blur(1);
        //outputImageJfx.save();
        
        
        
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        //ImageView inputImageView = new ImageView(inputImageJfx);
        ImageView outputImageView = new ImageView(outputImageJfx);
      
        VBox menuPane = new VBox();
        HBox hb = new HBox();
        
        Button openButton = new Button("Open Picture 1");
    	Button openButton2 = new Button("Open Picture 2");
        
        TextField textField = new TextField ();
    	
    	fileChooser.setTitle("Open picture");
        
        hb.setPadding(new Insets(10, 10, 10, 10));
        hb.setStyle("-fx-background-color: DAE699;");
        hb.setSpacing(10);
        hb.getChildren().add(openButton);
        hb.getChildren().add(openButton2);
        hb.getChildren().add(textField);
        
        openButton.setOnAction(
	        new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {
	            	//ImageView inputImageView;
	                File file = fileChooser.showOpenDialog(primaryStage);
	                if (file != null) {
	                	try {
                                    inputImageJfx = new Image(new FileInputStream(file));

                                    outputImageJfx = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());

                                    outputImageJfx.copyFrom(inputImageJfx);

                                    //inputImageView = new ImageView(inputImageJfx);

                                    outputImageView.setImage(outputImageJfx);

                                    } catch (FileNotFoundException e1) {
                                            // TODO Auto-generated catch block
                                            e1.printStackTrace();
                                    }
	                }
	            }
	        });
        
        for(Method method : outputImageJfx.getClass().getDeclaredMethods())
        {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            
            
            Button button = new Button(method.getName());
            button.setOnAction(
                    new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        try {  
                            
                            MyImage newImage = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());
                            
                            newImage.copyFrom(outputImageJfx);
                            
                            undoStack.add(outputImageJfx);
                            
                            outputImageJfx = newImage;
                            
                            
                            
                            
                            method.invoke(outputImageJfx, new Object[]{1});
                            
                            outputImageView.setImage(outputImageJfx);
                            
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            Logger.getLogger(MoreKernels.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            );
            
            
            menuPane.getChildren().add(button);
        }
        
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-background-color: DAE6F3;");
        root.setCenter(outputImageView);
        
        root.setLeft(menuPane);
        root.setTop(hb);
        
        Scene scene = new Scene(root, 400, 20 );
        
        primaryStage.setTitle("Duplicates");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}