package com.example.photoshopfx;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.FFT;
import ij.process.ImageProcessor;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import static ij.IJ.getProcessor;

public class Conversora {

    static public Image tonsCinza(Image image){
        // converte um Image em BufferedImage
        BufferedImage bimg;
        bimg= SwingFXUtils.fromFXImage(image, null);
        // captura pixels da imagem
        //Red, Green, Blue, Alpha
        int pixel[] = { 0 , 0 , 0 , 0 };
        WritableRaster raster=bimg.getRaster();
        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                raster.getPixel(col,lin,pixel); // obtenha um pixel
                int tonsCinza = (int)(0.299*pixel[0]+0.587+pixel[1]*0.114*pixel[2]);
                pixel[0] = pixel[1] = pixel[2];
//                pixel[0] = 255-pixel[0];  //IMAGEM INVERTIDA
//                pixel[1] = 255-pixel[1];
//                pixel[2] = 255-pixel[2];
                //... Transforme o pixel
                raster.setPixel(col,lin,pixel); // reaplique o pixel
            }
        }
        // se necessário, volte para um Image
        return SwingFXUtils.toFXImage(bimg, null);
    }

    static public Image pretoBranco(Image image){
        BufferedImage bimg;
        bimg= SwingFXUtils.fromFXImage(image, null);
        int pixel[] = { 0 , 0 , 0 , 0 };
        WritableRaster raster=bimg.getRaster();
        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                raster.getPixel(col,lin,pixel);
                int tonsPretoBranco = (int)(0.145*pixel[0]+0.145+pixel[1]*0.145*pixel[2]);
                if(tonsPretoBranco > 140){
                    pixel[0] = pixel[1] = pixel[2] = 220;
                }
                else{
                    pixel[0] = pixel[1] = pixel[2] = 10;
                }
                raster.setPixel(col,lin,pixel);
            }
        }
        return SwingFXUtils.toFXImage(bimg, null);
    }

    public static Image negativo(Image image) {
        BufferedImage bimg;
        bimg= SwingFXUtils.fromFXImage(image, null);
        int pixel[] = { 0 , 0 , 0 , 0 };
        WritableRaster raster=bimg.getRaster();
        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                raster.getPixel(col,lin,pixel);
                pixel[0] = 255-pixel[0];
                pixel[1] = 255-pixel[1];
                pixel[2] = 255-pixel[2];
                pixel[0] = pixel[1] = pixel[2];
                raster.setPixel(col,lin,pixel);
            }
        }
        return SwingFXUtils.toFXImage(bimg, null);
    }

    public static Image inverter(Image image){
        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(image,null);
        BufferedImage espelhada = new BufferedImage((int)image.getWidth(),(int)image.getHeight(),bimg.getType());
        WritableRaster raster = bimg.getRaster();
        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                int pixel = bimg.getRGB(col,lin);
                espelhada.setRGB((int)image.getWidth()-col-1,lin,pixel);
            }
        }
        return SwingFXUtils.toFXImage(espelhada,null);
    }

    public static Image vertical(Image image) {
        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(image,null);
        BufferedImage espelhada = new BufferedImage((int)image.getWidth(),(int)image.getHeight(),bimg.getType());
        WritableRaster raster = bimg.getRaster();
        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                int pixel = bimg.getRGB(col,lin);
                espelhada.setRGB(col,(int)image.getHeight()-lin-1,pixel);
            }
        }
        return SwingFXUtils.toFXImage(espelhada,null);
    }

    static public Image detectarBordasij(Image image){
        ImagePlus imagePlus = new ImagePlus();
        BufferedImage bimg = SwingFXUtils.fromFXImage(image,null);
        imagePlus.setImage(bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();
        imageProcessor.findEdges(); //detecta as bordas
        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
    }

    static public Image detectarBlur(Image image){
        ImagePlus imagePlus = new ImagePlus();
        BufferedImage bimg = SwingFXUtils.fromFXImage(image,null);
        imagePlus.setImage(bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();
        imageProcessor.blurGaussian(2.0);
        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
    }

    public static Image detectarThereshold(Image image) {
        ImagePlus imagePlus = new ImagePlus();
        BufferedImage bimg = SwingFXUtils.fromFXImage(image,null);
        imagePlus.setImage(bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();
        imageProcessor.autoThreshold();
        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
    }

    public static Image detectarDilate(Image image) {
        ImagePlus imagePlus = new ImagePlus();
        BufferedImage bimg = SwingFXUtils.fromFXImage(image,null);
        imagePlus.setImage(bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();
        imageProcessor.dilate();
        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
    }
    public static Image desenharImagemIJ(Image image, int x, int y) {
        ImagePlus imagePlus = new ImagePlus();
        BufferedImage bimg = SwingFXUtils.fromFXImage(image, null);
        imagePlus.setImage(bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();
        imageProcessor.fillOval(x,y,25,25);
        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(), null);
    }
}
