package pi.histograma;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;

/**
 *
 * @author gabri
 */
public class PIHistograma {
    BufferedImage abreImagem() throws IOException{
        BufferedImage image = ImageIO.read(new File("Imagens-in\\2.jpg"));
        return image;
        
    }
    int[] calculaHistogramaRGB(BufferedImage img){
        int[] histograma = new int[256];
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x, y));
                int rgb = (color.getRed()+color.getBlue()+color.getGreen())/3;
                histograma[rgb] += 1;
            }
        }
        return histograma;
    }
    public int[] calculaHistogramaAcumulado(int[] histograma) {
        int[] acumulado = new int[256];
        acumulado[0] = histograma[0];
        for(int i=1;i < histograma.length;i++) {

            acumulado[i] = histograma[i] + acumulado[i-1];
        }
        return acumulado;
    }
    private int menorValor(int[] histograma) {
        for(int i=0; i <histograma.length; i++) {
            if(histograma[i] != 0){
                return histograma[i];
            }
        }
        return 0;
    }
    private int[] calculaMapadeCores(int[] histograma, int pixels) {
        int[] mapaDeCores = new int[256];
        int[] acumulado = calculaHistogramaAcumulado(histograma);
        float menor = menorValor(histograma);
        for(int i=0; i < histograma.length; i++) {
            mapaDeCores[i] = Math.round(((acumulado[i] - menor) / (pixels - menor)) * 255);
        }
        return mapaDeCores;
    }
    public BufferedImage equalizacao(BufferedImage img) {
        int[] histograma = calculaHistogramaRGB(img);
        int[] mapaDeCores = calculaMapadeCores(histograma, img.getWidth() * img.getHeight());
        BufferedImage NovaImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x, y));
                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();
                int novoRed = mapaDeCores[red];
                int novoBlue = mapaDeCores[blue];
                int novoGreen = mapaDeCores[green];
                Color newColor = new Color(novoRed, novoGreen, novoBlue);
                NovaImg.setRGB(x, y, newColor.getRGB());
            }
        }
        return NovaImg;
    }
    void imprimeHistograma(BufferedImage img, String nomeDoc) throws IOException{
        int[] histograma = calculaHistogramaRGB(img);
        FileWriter arq = new FileWriter(nomeDoc+".txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("--Resultado--%n");
        for (int i=1; i < histograma.length; i++) {
            gravarArq.println(histograma[i]);
        }
        arq.close();
    }
    void Cria_Image_Resultante(BufferedImage img) throws IOException{
        ImageIO.write(img,"jpg",new File("Resultante.jpg"));
    }
    void run() throws IOException {
        BufferedImage img = abreImagem();
        BufferedImage ImgEqualizada = equalizacao(img);
        imprimeHistograma(img, "Histograma inicial");
        imprimeHistograma(ImgEqualizada, "Histograma equalizado");
        Cria_Image_Resultante(ImgEqualizada);
    }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        new PIHistograma().run();
    }
}
