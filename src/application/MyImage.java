/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

/**
 *
 * @author unouser
 */
public class MyImage extends WritableImage{

    public MyImage(int i, int i0) {
        super(i, i0);
    }
    
    public void copyFrom(Image inImage){
        for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
                this.getPixelWriter().setColor(x, y,  inImage.getPixelReader().getColor(x, y));
            }
        }
    }
    
    public void save(){
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(this, null);
        try {
            ImageIO.write(renderedImage, "PNG", new File(System.getProperty("user.home") + "/Desktop/newPhoto.png") );
        } catch (IOException ex) {
            Logger.getLogger(MyImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void applyKernel(float[][] kernel, int w)
    {
        Color[][] newColors = new Color[(int)this.getHeight()][(int)this.getWidth()];
        
        for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
                
                Color myColor = this.getPixelReader().getColor(x, y );
                if(myColor.getOpacity() == 0)
                {
                    newColors[y][x] = Color.TRANSPARENT;
                    continue;
                }
                
                
                float sumR= 0;
                float sumG= 0;
                float sumB= 0;
                
                float power = 0;
                
                for(int i = -1 * w; i <= 1 * w; i++)
                {
                    for(int j = -1 * w; j <= 1 * w; j++)
                    {
                        if(this.doesPixelExist(x + i, y + j))
                        {
                            Color currentPixel = this.getPixelReader().getColor(x + i, y + j);
                            float kernelValue = kernel[j + w][i + w];
                            sumR += currentPixel.getRed() * kernelValue;
                            sumG += currentPixel.getGreen()* kernelValue;
                            sumB += currentPixel.getBlue()* kernelValue;
                            power += kernelValue;
                        }
                        
                    }
                }
                
                sumR /= power;
                sumG /= power;
                sumB /= power;
                
                newColors[y][x] = new Color(sumR, sumG, sumB, 1.0f );
            }
        }
        
        for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
                this.getPixelWriter().setColor(x, y, newColors[y][x]);
            }
        }
    }
    
    public void blur(int w) {
        
        float[][] blurKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                blurKernel[y][x] = 1;
            }
        }
        
        this.applyKernel(blurKernel, w);
        
    }
    
    public void combine(Image image2) {
    	int width;
    	int height;
    	
    	if(this.getHeight() < image2.getHeight()) {
    		height = (int) this.getHeight();
    	}else{
    		height = (int) image2.getHeight();
    	}
    	
    	if(this.getWidth() < image2.getWidth()) {
    		width = (int) this.getWidth();
    	}else{
    		width = (int) image2.getWidth();
    	}
    	
    	 Color[][] newColors = new Color[height][width];
         
         for(int y = 0; y < height ;  y++)
         {
             for(int x = 0; x < width ; x++)
             {
                 
                 Color myColor = this.getPixelReader().getColor(x, y );
                 if(myColor.getOpacity() == 0)
                 {
                     newColors[y][x] = Color.TRANSPARENT;
                     continue;
                 }
                 
                 
                 float sumR= 0;
                 float sumG= 0;
                 float sumB= 0;
                    
                 Color currentPixel = this.getPixelReader().getColor(x, y);
                 Color currentPixel2 = image2.getPixelReader().getColor(x, y);
                 
                 sumR = (float) (currentPixel.getRed() + currentPixel2.getRed()) / 2;
                 sumG = (float) (currentPixel.getGreen() + currentPixel2.getGreen()) / 2;
                 sumB = (float) ((currentPixel.getBlue() + currentPixel2.getBlue()) / 2);
                 
                 newColors[y][x] = new Color(sumR, sumG, sumB, 1.0f );
             }
         }
         
         for(int y = 0; y < height ;  y++)
         {
             for(int x = 0; x < width ; x++)
             {
                 this.getPixelWriter().setColor(x, y, newColors[y][x]);
             }
         }
        
    }

    private boolean doesPixelExist(int x, int y) {
        return x < this.getWidth() && x >= 0 && y < this.getHeight() && y >= 0;
    }
    
    
    
}
