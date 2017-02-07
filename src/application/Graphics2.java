
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Graphics2 extends Application {
    
    static Image inputImageJfx;
    static Image inputImage2Jfx;
    static MyImage outputImageJfx;
    static MyImage outputImage2Jfx;
    static MyImage outputImage3Jfx;
    static FileChooser fileChooser = new FileChooser();

    public static void main(String[] args) throws Exception{
       
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	
    	final Button openButton = new Button("Open Picture 1");
    	final Button openButton2 = new Button("Open Picture 2");
    	final Button blurButton = new Button("Blur");
    	TextField textField = new TextField ();
    	
    	fileChooser.setTitle("Open picture");
        
        GridPane root = new GridPane();
        HBox hb = new HBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-background-color: DAE6F3;");
        hb.setPadding(new Insets(10, 10, 10, 10));
        hb.setStyle("-fx-background-color: DAE699;");
        hb.setSpacing(10);
        root.setHgap(10);
        root.setVgap(10);
        hb.getChildren().add(openButton);
        hb.getChildren().add(openButton2);
        hb.getChildren().add(blurButton);
        hb.getChildren().add(textField);
        root.add(hb, 0, 0);
        //root.add(openButton2, 1, 0);
        //root.add(blurButton, 2, 0);
        //root.add(textField, 3, 0);
        
    	openButton.setOnAction(
	        new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {
	            	ImageView inputImageView;
	                File file = fileChooser.showOpenDialog(primaryStage);
	                if (file != null) {
	                	try {
							inputImageJfx = new Image(new FileInputStream(file));
							
							outputImageJfx = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());;
					        
					        outputImageJfx.copyFrom(inputImageJfx);
					        
					        inputImageView = new ImageView(inputImageJfx);
					        
					        root.add(inputImageView, 0,1);
					        
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                }
	            }
	        });
		
		openButton2.setOnAction(
			new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					ImageView inputImageView2;
					ImageView outputImageView;
					File file = fileChooser.showOpenDialog(primaryStage);
					if (file != null) {
						try {
							inputImage2Jfx = new Image(new FileInputStream(file));
							
							 outputImageJfx.combine(inputImage2Jfx);
							 outputImageJfx.save();
							 
							 inputImageView2 = new ImageView(inputImage2Jfx);
							 outputImageView = new ImageView(outputImageJfx);
							 
							 root.add(inputImageView2, 1, 1);
							 root.add(outputImageView, 2, 1);
						     
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
    	
		blurButton.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						ImageView inputImageView2;
						textField.getText();
						
						int n = Integer.parseInt(textField.getText());
						
						outputImage3Jfx = new MyImage((int)outputImageJfx.getWidth(), (int)outputImageJfx.getHeight());;
						
						outputImage3Jfx.copyFrom(outputImageJfx);
						 outputImage3Jfx.blur(n);
						 outputImage3Jfx.save();
						 
						 inputImageView2 = new ImageView(outputImage3Jfx);
						 root.add(inputImageView2, 0, 2);
					}
				});
    
        
        Scene scene = new Scene(root, 1920, 1080);
        
        primaryStage.setTitle("Duplicates");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}