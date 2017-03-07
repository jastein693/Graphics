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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
        HBox hb2 = new HBox();
        
        Button openButton = new Button("Open Picture");
    	Button openButton2 = new Button("Overlay picture");
    	Button openButton3 = new Button("Save picture");
        
        TextField textField = new TextField ();
        Label label = new Label("Method Value:");
        
        Label red = new Label("Red:");
        Label green = new Label("Green:");
        Label blue = new Label("Blue:");
        Label hue = new Label("Hue:");
        Label saturation = new Label("Saturation:");
        Label brightness = new Label("Brightness:");
        Label opacity = new Label("Opacity:");
    	
        Slider sliderRed = new Slider();
        sliderRed.setMin(0);
        sliderRed.setMax(1);
        sliderRed.setShowTickLabels(true);
        sliderRed.setShowTickMarks(true);
        
        Slider sliderGreen = new Slider();
        sliderGreen.setMin(0);
        sliderGreen.setMax(1);
        sliderGreen.setShowTickLabels(true);
        sliderGreen.setShowTickMarks(true);
        
        Slider sliderBlue = new Slider();
        sliderBlue.setMin(0);
        sliderBlue.setMax(1);
        sliderBlue.setShowTickLabels(true);
        sliderBlue.setShowTickMarks(true);
        
        Slider sliderHue = new Slider();
        sliderHue.setMin(0);
        sliderHue.setMax(360);
        sliderHue.setShowTickLabels(true);
        sliderHue.setShowTickMarks(true);
        
        Slider sliderSaturation = new Slider();
        sliderSaturation.setMin(0);
        sliderSaturation.setMax(1);
        sliderSaturation.setShowTickLabels(true);
        sliderSaturation.setShowTickMarks(true);
        
        Slider sliderBrightness = new Slider();
        sliderBrightness.setMin(0);
        sliderBrightness.setMax(1);
        sliderBrightness.setShowTickLabels(true);
        sliderBrightness.setShowTickMarks(true);
        
        Slider sliderOpacity = new Slider();
        sliderOpacity.setMin(0);
        sliderOpacity.setMax(1);
        sliderOpacity.setShowTickLabels(true);
        sliderOpacity.setShowTickMarks(true);
        
    	fileChooser.setTitle("Open picture");
        
        hb.setPadding(new Insets(10, 10, 10, 10));
        hb.setStyle("-fx-background-color: DAE699;");
        hb.setSpacing(10);
        hb.getChildren().add(openButton);
        hb.getChildren().add(openButton2);
        hb.getChildren().add(label);
        hb.getChildren().add(textField);
        hb.getChildren().add(openButton3);
        
        hb2.setPadding(new Insets(10, 10, 10, 10));
        hb2.setStyle("-fx-background-color: DFE699;");
        hb2.setSpacing(10);
        hb2.getChildren().add(red);
        hb2.getChildren().add(sliderRed);
        hb2.getChildren().add(green);
        hb2.getChildren().add(sliderGreen);
        hb2.getChildren().add(blue);
        hb2.getChildren().add(sliderBlue);
        hb2.getChildren().add(hue);
        hb2.getChildren().add(sliderHue);
        hb2.getChildren().add(saturation);
        hb2.getChildren().add(sliderSaturation);
        hb2.getChildren().add(brightness);
        hb2.getChildren().add(sliderBrightness);
        hb2.getChildren().add(opacity);
        hb2.getChildren().add(sliderOpacity);
        
        textField.setText("1");
        
        Button undoButton = new Button("Undo");
        undoButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                
                if(undoStack.size() > 0)
                {
                    MyImage previousImage = undoStack.get(undoStack.size() - 1);
                    undoStack.remove(undoStack.size() - 1);
                    
                    redoStack.add(outputImageJfx);
                    
                    outputImageJfx = previousImage;
                            
                            
                    outputImageView.setImage(outputImageJfx);
                    
                    
                }
                
            }
        });
        menuPane.getChildren().add(undoButton);
        
        Button redoButton = new Button("Redo");
        redoButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(redoStack.size() > 0)
                {
                    MyImage previousImage = redoStack.get(redoStack.size() - 1);
                    redoStack.remove(redoStack.size() - 1);
                    
                    undoStack.add(outputImageJfx);
                    
                    outputImageJfx = previousImage;
                            
                            
                    outputImageView.setImage(outputImageJfx);
                    
                    
                }
            }
        });
        menuPane.getChildren().add(redoButton);
        
        menuPane.getChildren().add(new Separator());
        
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
        
        openButton2.setOnAction(
    	        new EventHandler<ActionEvent>() {
    	            @Override
    	            public void handle(final ActionEvent e) {
    	            	//ImageView inputImageView;
    	                File file = fileChooser.showOpenDialog(primaryStage);
    	                if (file != null) {
    	                	try {
                                    Image inputImageJfx2 = new Image(new FileInputStream(file));
                                    	
                                    outputImageJfx.combine(inputImageJfx2);
                                    //inputImageView = new ImageView(inputImageJfx);

                                    outputImageView.setImage(outputImageJfx);

                                    } catch (FileNotFoundException e1) {
                                            // TODO Auto-generated catch block
                                            e1.printStackTrace();
                                    }
    	                }
    	            }
    	        });
        
        openButton3.setOnAction(
    	        new EventHandler<ActionEvent>() {
    	            @Override
    	            public void handle(final ActionEvent e) {
    	            	//ImageView inputImageView;
    	                File file = fileChooser.showSaveDialog(primaryStage);
    	                if (file != null) {
    	                	
    	                	outputImageJfx.save(file);
                                 
    	                }
    	            }
    	        });
        
        for(Method method : outputImageJfx.getClass().getDeclaredMethods())
        {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            
            if(method.getName() != "save" && method.getName() != "combine" && method.getName() != "copyFrom" && method.getName() != "setRed"  && method.getName() != "setGreen" && method.getName() != "setBlue" ) {   
	            Button button = new Button(method.getName());
	            button.setOnAction(
	                    new EventHandler<ActionEvent>(){
	                    @Override
	                    public void handle(ActionEvent event) {
	                        try {  
	                            
	                            MyImage newImage = new MyImage((int)outputImageJfx.getWidth(), (int)outputImageJfx.getHeight());
	                            
	                            newImage.copyFrom(outputImageJfx);
	                            
	                            undoStack.add(outputImageJfx);
	                            
	                            redoStack.clear();
	                            
	                            outputImageJfx = newImage;
	                            
	                            int n = Integer.parseInt(textField.getText());
	                            
	                            if(method.getName() == "addBorder") {
	                            	outputImageJfx = outputImageJfx.addBorder(n);
	                            }else if(method.getName() == "grow") {
	                            	outputImageJfx = outputImageJfx.grow(n);
	                            }else if(method.getName() == "shrink") {
	                            	outputImageJfx = outputImageJfx.shrink(n);
	                            }else{
	                            	method.invoke(outputImageJfx, new Object[]{n});
	                            }
	                            
	                            outputImageView.setImage(outputImageJfx);
	                            
	                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
	                            Logger.getLogger(MoreKernels.class.getName()).log(Level.SEVERE, null, ex);
	                        }
	                    }
	                    
	                }
	            );
	            menuPane.getChildren().add(button);
            }
        }
        
        sliderRed.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		outputImageJfx.setRed(new_val.doubleValue());
            }
        });
        
        sliderGreen.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		outputImageJfx.setGreen(new_val.doubleValue());
            }
        });
        
        sliderBlue.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		outputImageJfx.setBlue(new_val.doubleValue());
            }
        });
        
        sliderHue.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		outputImageJfx.setHue(new_val.doubleValue());
            }
        });
        
        sliderSaturation.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		outputImageJfx.setSaturation(new_val.doubleValue());
            }
        });
        
        sliderBrightness.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		outputImageJfx.setBrightness(new_val.doubleValue());
            }
        });
        
        sliderOpacity.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		outputImageJfx.setOpacity(new_val.doubleValue());
            }
        });
        
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-background-color: DAE6F3;");
        root.setCenter(outputImageView);
        
        root.setLeft(menuPane);
        root.setTop(hb);
        root.setBottom(hb2);
        
        Scene scene = new Scene(root, 1920, 1080 );
        
        primaryStage.setTitle("Duplicates");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}