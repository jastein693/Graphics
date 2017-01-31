/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package application;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Graphics extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        ImageView imageView = new ImageView(outputImageJfx);
        ImageView imageView2 = new ImageView(outputImage2Jfx);
        ImageView imageView3 = new ImageView(outputImage3Jfx);
        ImageView imageView4 = new ImageView(outputImage4Jfx);
        ImageView imageView5 = new ImageView(outputImage5Jfx);
        ImageView imageView6 = new ImageView(outputImage6Jfx);
        
        
        Button btn = new Button();
        btn.setText("I am a button");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Joseph likes to eat sandwiches");
            }
        });
        
        StackPane root = new StackPane();
        StackPane.setAlignment(imageView, Pos.TOP_LEFT);
        root.getChildren().add(imageView);
        StackPane.setAlignment(imageView2, Pos.TOP_RIGHT);
        root.getChildren().add(imageView2);
        StackPane.setAlignment(imageView3, Pos.CENTER_LEFT);
        root.getChildren().add(imageView3);
        StackPane.setAlignment(imageView4, Pos.CENTER_RIGHT);
        root.getChildren().add(imageView4);
        StackPane.setAlignment(imageView5, Pos.BOTTOM_LEFT);
        root.getChildren().add(imageView5);
        StackPane.setAlignment(imageView6, Pos.BOTTOM_RIGHT);
        root.getChildren().add(imageView6);
        
        Scene scene = new Scene(root, 1920, 1080);
        
        primaryStage.setTitle("Quien es tu Papa");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    static BufferedImage inputImage;
    static Image inputImageJfx;
    static WritableImage outputImageJfx;
    static WritableImage outputImage2Jfx;
    static WritableImage outputImage3Jfx;
    static WritableImage outputImage4Jfx;
    static WritableImage outputImage5Jfx;
    static WritableImage outputImage6Jfx;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        
        inputImageJfx = new Image(new FileInputStream("E:/Users/Joseph/Pictures/2980/KeepOnTruckin.png"));
        
        outputImageJfx = new WritableImage(600, 279);
        outputImage2Jfx = new WritableImage(600, 279);
        outputImage3Jfx = new WritableImage(600, 279);
        outputImage4Jfx = new WritableImage(600, 279);
        outputImage5Jfx = new WritableImage(600, 279);
        outputImage6Jfx = new WritableImage(600, 279);
        
        for(int y = 0; y < 279 ;  y++)
        {
            for(int x = 0; x < 600 ; x++)
            {
                
            	Color c = inputImageJfx.getPixelReader().getColor(x, y);
            	
            	double red = Math.random();
            	double green = Math.random();
            	double blue = Math.random();
                
            	double gray = (c.getBlue() + c.getRed() + c.getGreen()) / 3;
            			
                outputImageJfx.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.color(c.getRed(), c.getGreen(), c.getBlue()));
                outputImage2Jfx.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.color(gray, gray, gray));
                outputImage3Jfx.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.color(c.getRed(), c.getRed(), c.getRed()));
                outputImage4Jfx.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.color(c.getGreen(), c.getGreen(), c.getGreen()));
                outputImage5Jfx.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.color(c.getBlue(), c.getBlue(), c.getBlue()));
                outputImage6Jfx.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.color(red, green, blue));
            }
        }
 
        launch(args);
    }

   
    
    
}