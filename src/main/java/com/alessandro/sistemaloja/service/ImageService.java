package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.service.exception.FileException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    public BufferedImage getJpgImageFromFile(MultipartFile uploadFile) {
        //pegando extensão do arquivo
        String extension = FilenameUtils.getExtension(uploadFile.getOriginalFilename());
        if (!"png".equals(extension) && !"jpg".equals(extension)) {
            throw new FileException("Somente imagens PNG e JPG são permitidas");
        }

        try {
            //criando um buffer de imagem e testando o tipo de extensão
            BufferedImage img = ImageIO.read(uploadFile.getInputStream());
            if ("png".equals(extension)) {
                //convertendo a extensão para jpg
                img = pngToJpg(img);
            }
            return img;
        }catch (IOException e) {
            throw new FileException("Erro ao ler o arquivo");
        }
    }

    public BufferedImage pngToJpg(BufferedImage img) {
        BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
        return jpgImage;
    }

    //método necessário para salvar o arquivo em formato de inputStream na amazon
    public InputStream getInputStream(BufferedImage img, String extension) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(img, extension, os);
            return new ByteArrayInputStream(os.toByteArray());
        }catch (IOException e) {
            throw new FileException("Erro ao ler o arquivo");
        }
    }

    //função para cortar imagem para que fique quadrada
    public BufferedImage croparImagem(BufferedImage sourceImage) {
        int min = (sourceImage.getHeight() <= sourceImage.getWidth() ? sourceImage.getHeight() : sourceImage.getWidth());
        return Scalr.crop(
                sourceImage,
                (sourceImage.getWidth()/2) - (min/2),
                (sourceImage.getHeight()/2) - (min/2),
                min,
                min);
    }

    //função para redimensionar
    public BufferedImage redimensionarImagem(BufferedImage sourceImage, int size) {
        return Scalr.resize(sourceImage, Scalr.Method.ULTRA_QUALITY, size);
    }
}
