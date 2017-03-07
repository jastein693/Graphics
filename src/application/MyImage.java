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
    
    public void save(File file){
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(this, null);
        try {
            ImageIO.write(renderedImage, "png", file );
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
                            power +=kernelValue;
                        }
                        
                    }
                }
                
                sumR /= power;
                sumG /= power;
                sumB /= power;
                
                
                
                newColors[y][x] = new Color(
                        clampAbs(sumR), 
                        clampAbs(sumG), 
                        clampAbs(sumB), 
                        1.0f );
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
    
    public void doNothing(int w) {
        
        float[][] doNothingKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                doNothingKernel[y][x] = 0;
                if(y == w && x == w)
                    doNothingKernel[y][x] = 1;
            }
        }
        
        this.applyKernel(doNothingKernel, w);
    } 
    
        
    public void magic(int w) {
        
        float[][] magicKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                magicKernel[y][x] = 0;
               
            }
        }
        
        magicKernel[0][0] = 1;
        magicKernel[2*w][2*w] = 1;
        
        this.applyKernel(magicKernel, w);
        
    }
    
    public void edgeDetector(int i) {
        
        int w = 1; ///3x3 kernel
        float[][] magicKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                magicKernel[y][x] = 0;
               
            }
        }
        
        magicKernel[w][w] = 4;
        magicKernel[w][0] = -1;
        magicKernel[0][w] = -1;
        magicKernel[w][2] = -1;
        magicKernel[2][w] = -1;
        
        
        
        this.applyKernel(magicKernel, w);
        
    }
    
    public void sharpen(int i) {
        
        int w = 1; ///3x3 kernel
        float[][] magicKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                magicKernel[y][x] = 0;
               
            }
        }
        
        magicKernel[w][w] = 5;
        magicKernel[w][0] = -1;
        magicKernel[0][w] = -1;
        magicKernel[w][2] = -1;
        magicKernel[2][w] = -1;
        
        
        
        this.applyKernel(magicKernel, w);
        
    }

    private boolean doesPixelExist(int x, int y) {
        return x < this.getWidth() && x >= 0 && y < this.getHeight() && y >= 0;
    }
    
    private float clampAbs(float f){
        float toReturn = Math.abs(f);
        
        if(toReturn < 0)
            return 0;
        if(toReturn > 1)
            return 1;
        
        return toReturn;
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
    
    public void rotate(float degrees) {
    	Color[][] newColors = new Color[(int) this.getHeight()][(int) this.getWidth()];
    	
    	int halfHeight = (int) (this.getHeight() / 2);
    	int halfWidth = (int) (this.getWidth() / 2);
    	
    	float rad = -(float) Math.toRadians(degrees);
    	
    	 for(int y = 0; y < this.getHeight() ;  y++)
         {
             for(int x = 0; x < this.getWidth() ; x++)
             {
            	
				int tmp = (int) ((x - halfWidth) * Math.cos(rad) - (-y + halfHeight) * Math.sin(rad));
				int yNew = (int)((x - halfWidth) * Math.sin(rad) + (-y + halfHeight) * Math.cos(rad));
				int xNew = tmp;
				
				xNew += halfWidth;
				yNew = -(yNew - halfHeight);
				
				if(yNew < this.getHeight() && yNew >= 0 && xNew < this.getWidth() && xNew >= 0) {
					newColors[yNew][xNew] = this.getPixelReader().getColor(x, y);
				}
             }
         }
    	 
    	 for(int y = 0; y < this.getHeight() ;  y++)
         {
             for(int x = 0; x < this.getWidth() ; x++)
             {
            	 if(newColors[y][x] == null) {
            		 this.getPixelWriter().setColor(x, y, Color.WHITE);
            	 }else{
            		 this.getPixelWriter().setColor(x, y, newColors[y][x]);
            	 }
             }
         }
	}
    
    public MyImage addBorder(int size) {
    	Color[][] newColors = new Color[(int) this.getHeight()][(int) this.getWidth()];
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	
            	newColors[y][x] = this.getPixelReader().getColor(x, y);
            }
        }
    	
    	MyImage newImage = new MyImage((int)this.getWidth() + (size * 2), (int)this.getHeight() + (size * 2));
    	
    	for(int y = 0; y < newImage.getHeight() ;  y++)
        {
            for(int x = 0; x < newImage.getWidth() ; x++)
            {
            	if(x < size || x >= size + this.getWidth() || y < size || y >= size + this.getHeight()) {
            		newImage.getPixelWriter().setColor(x, y, Color.WHITE);
            	}else{
            		newImage.getPixelWriter().setColor(x, y, newColors[y-size][x-size]);
            	}
            }
        }
    	return newImage;
    }
    
    public void verticalFlip(int i) {
    	Color[][] newColors = new Color[(int) this.getHeight()][(int) this.getWidth()];
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	
            	newColors[y][x] = this.getPixelReader().getColor(x, y);
            }
        }
    	
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	
            	this.getPixelWriter().setColor(x, y, newColors[((int)this.getHeight() - 1) - y][x]);
            }
        }
    }

    public void horizontalFlip(int i) {
    	Color[][] newColors = new Color[(int) this.getHeight()][(int) this.getWidth()];
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	
            	newColors[y][x] = this.getPixelReader().getColor(x, y);
            }
        }
    	
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	
            	this.getPixelWriter().setColor(x, y, newColors[y][((int)this.getWidth() - 1) - x]);
            }
        }
    }
    
    public void grayScale(int i) {
    	
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	
            	double red = this.getPixelReader().getColor(x, y).getRed() * 0.2126;
            	double green = this.getPixelReader().getColor(x, y).getGreen() * 0.7152;
            	double blue = this.getPixelReader().getColor(x, y).getBlue() * 0.0722;
            	double gray = red + green + blue;
            	this.getPixelWriter().setColor(x, y, new Color(gray,gray,gray,1));
            }
        }
    }
    
    public void setRed(double red) {
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	this.getPixelWriter().setColor(x, y, new Color(red,this.getPixelReader().getColor(x, y).getGreen(),this.getPixelReader().getColor(x, y).getBlue(),1));
            }
        }
    }
    
    public void setGreen(double green) {
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	this.getPixelWriter().setColor(x, y, new Color(this.getPixelReader().getColor(x, y).getRed(),green,this.getPixelReader().getColor(x, y).getBlue(),1));
            }
        }
    }
    
    public void setBlue(double blue) {
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	this.getPixelWriter().setColor(x, y, new Color(this.getPixelReader().getColor(x, y).getRed(), this.getPixelReader().getColor(x, y).getGreen(), blue, 1));
            }
        }
    }
    
    public void setHue(double hue) {
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	this.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.hsb(hue, this.getPixelReader().getColor(x, y).getSaturation(), this.getPixelReader().getColor(x, y).getBrightness()));
            }
        }
    }
    
    public void setSaturation(double saturation) {
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	this.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.hsb(this.getPixelReader().getColor(x, y).getHue(), saturation, this.getPixelReader().getColor(x, y).getBrightness()));
            }
        }
    }
    
    public void setBrightness(double brightness) {
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	this.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.hsb(this.getPixelReader().getColor(x, y).getHue(), this.getPixelReader().getColor(x, y).getSaturation(), brightness));
            }
        }
    }
    
    public void setOpacity(double opacity) {
    	for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
            	this.getPixelWriter().setColor(x, y, javafx.scene.paint.Color.hsb(this.getPixelReader().getColor(x, y).getHue(), this.getPixelReader().getColor(x, y).getSaturation(), this.getPixelReader().getColor(x, y).getBrightness(), opacity));
            }
        }
    }
    
    public MyImage grow(float w) {
    	MyImage newImage = new MyImage((int)(this.getWidth() * w), (int)(this.getHeight() * w));
    	for(int y = 0; y < newImage.getHeight() - w;  y++)
        {
            for(int x = 0; x < newImage.getWidth() - w; x++)
            {
                ///Where on the original photo I'm sampling
                float x_old = (x/w);
                float y_old = (y/w);
                
                Color cFirst = this.getPixelReader().getColor((int)x_old, (int)y_old );
                Color cSecond =  this.getPixelReader().getColor((int)x_old + 1, (int)y_old  );
                Color cThird = this.getPixelReader().getColor((int)x_old, (int)y_old + 1);
                Color cFourth =  this.getPixelReader().getColor((int)x_old + 1, (int)y_old  + 1);
                
                float iValueX = (float)(x/w) -  (int)(x/w);
                float iValueY = (float)(y/w) -  (int)(y/w);
                
                Color iPixelOne = interpolate(iValueX, cFirst, cSecond);
                Color iPixelTwo = interpolate(iValueX, cThird, cFourth);
                
                Color iPixelFinal = interpolate(iValueY, iPixelOne, iPixelTwo);
                
                newImage.getPixelWriter().setColor(x, y, iPixelFinal);
            }
        }
    	return newImage;
    }
    
    public MyImage shrink(float w) {
    	MyImage newImage = new MyImage((int)(this.getWidth() / w), (int)(this.getHeight() / w));
    	for(int y = 0; y < this.getHeight() - w;  y += w)
        {
            for(int x = 0; x < this.getWidth() - w; x += w)
            {   
                newImage.getPixelWriter().setColor((int)(x/w), (int)(y/w), this.getPixelReader().getColor(x, y));
            }
        }
    	return newImage;
    }
    
    private static Color interpolate(float iValue, Color cFirst, Color cSecond) {
        double iRed = ((1 - iValue) * cFirst.getRed() + iValue * cSecond.getRed());
        double iGreen = ((1 - iValue) * cFirst.getGreen() + iValue * cSecond.getGreen());
        double iBlue = ((1 - iValue) * cFirst.getBlue() + iValue * cSecond.getBlue());
        Color iColor = new Color(iRed, iGreen, iBlue, 1);
        return iColor;
    }
}