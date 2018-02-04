package pakageSkinDetection;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;

import javax.imageio.ImageIO;

public class ImageReading extends RGBConverter {
	
	private File []listOfImage;
	private File []listOfMask;
	
	private void WriteSkinAndNonSkinMapToAFile(String skinNonSkinMapingFilePath) throws IOException {
		
		File skinNonMapFileName = new File(skinNonSkinMapingFilePath);
		skinNonMapFileName.createNewFile();			
		FileWriter sw = new FileWriter(skinNonMapFileName);
		
		for(int i=0;i<256;i++) 
			for(int j=0;j<256;j++) 
				for(int k=0;k<256;k++) 
					sw.write(skin[i][j][k]+" "+nonSkin[i][j][k]+"\n");
				
		sw.close();
	}

	private void getImageAndMaskList(String ImageFolderPath,String MaskFolderPath) {
		File imageFolder = new File(ImageFolderPath);
		File maskFolder = new File(MaskFolderPath);
		
		listOfImage = imageFolder.listFiles();
		listOfMask = maskFolder.listFiles();		
	}

	private void imageAndMaskPixelMaping(int imagePixelValue,int maskPixelValue) {		
		int iR = getR(imagePixelValue);
		int iG = getG(imagePixelValue);
		int iB = getB(imagePixelValue);
		
		int mR = getR(maskPixelValue);
		int mG = getG(maskPixelValue);
		int mB = getB(maskPixelValue);
		
		if(mR==0 && mG==0 && mB==0) {
			nonSkin[iR][iG][iB]++;
		}
		else {
			skin[iR][iG][iB]++;
		}
	}
		
	public void readImageAndLearn(String ImageFolderPath,String MaskFolderPath,String LearnedDataPath) throws IOException {

		getImageAndMaskList(ImageFolderPath,MaskFolderPath);
		
		for(int k=0;k<listOfImage.length;k++) {			
			File imageFile = new File(listOfImage[k].toString());
			File maskFile = new File(listOfMask[k].toString());			
			BufferedImage image = null;
			BufferedImage mask = null;

			image = ImageIO.read(imageFile);
			mask = ImageIO.read(maskFile);
					
			int width = image.getWidth();
			int height = image.getHeight();
			
			for(int i=0;i<width;i++) 
				for(int j=0;j<height;j++) 				
					imageAndMaskPixelMaping(image.getRGB(i, j),mask.getRGB(i, j));								
		}
		WriteSkinAndNonSkinMapToAFile(LearnedDataPath);
	}
	
}
