package pakageSkinDetection;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreateImageMask extends RGBConverter {
	
	private int[] splitStringToInteger(String str) {		
		int [] array = new int[5];
		String [] arr = str.split(" ");
		for(int i=0;i<arr.length;i++) {
			array[i] = Integer.valueOf(arr[i]);
		}
		return array;
	}
	
	private void intializeSkinAndNonSkinMaping(String LearnedDataPath) throws IOException {
		
		int [] array = new int [5];
		BufferedReader sr = new BufferedReader(new FileReader(LearnedDataPath));					
		
		String skinNonSkindata = sr.readLine();

		while(skinNonSkindata!=null) {
			array = splitStringToInteger(skinNonSkindata);
			skin[array[0]][array[1]][array[2]] = array[3];
			nonSkin[array[0]][array[1]][array[2]] = array[4];
			skinNonSkindata = sr.readLine();
		}
		sr.close();		
	}
	
	public void createAMaskImage(String imagePath,String LearnedDataPath) throws IOException {
		intializeSkinAndNonSkinMaping(LearnedDataPath);
		File newImage = new File(imagePath);
					
		BufferedImage image =  ImageIO.read(newImage);
	
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage outputMask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<width;i++) 
			for(int j=0;j<height;j++) {
				int imagePixelValue = image.getRGB(i, j);
				int r = getR(imagePixelValue);
				int g = getG(imagePixelValue);
				int b = getB(imagePixelValue);
				if(nonSkin[r][g][b] == 0) outputMask.setRGB(i, j, -1);			
				else {
					double total = (double)skin[r][g][b] / (double) nonSkin[r][g][b];								
					if(total>0.6) outputMask.setRGB(i, j, -1);
					else outputMask.setRGB(i, j, 0);
				}					
			}			
		File output = new File("outputMask.png");
		ImageIO.write(outputMask, "png", output);		
	}
}
