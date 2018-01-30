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

public class ImageReading {
	
	private int [][][]skin = new int[256][256][256];
	private int [][][]nonSkin = new int[256][256][256];
	private int width,height,imagePixelValue,maskPixelValue;
	
	private File []listOfImage;
	private File []listOfMask;
	
	private void WriteSkinAndNonSkinMapToAFile() {
		File skinMapFileName = new File("skinMap.txt");
		File nonSkinMapFileName = new File("nonSkinMap.txt");
		try {
			skinMapFileName.createNewFile();
			nonSkinMapFileName.createNewFile();
			
			FileWriter sw = new FileWriter(skinMapFileName);
			FileWriter nsw = new FileWriter(nonSkinMapFileName);
			
			for(int i=0;i<256;i++) {
				for(int j=0;j<256;j++) {
					for(int k=0;k<256;k++) {
						sw.write(i+" "+j+" "+k+" "+skin[i][j][k]+"\n");
						nsw.write(i+" "+j+" "+k+" "+nonSkin[i][j][k]+"\n");
					}
				}
			}
			sw.close();
			nsw.close();
			
			} 
		catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

	}
	private void getImageAndMaskList() {
		File imageFolder = new File("ImageFolder\\2image");
		File maskFolder = new File("ImageFolder\\2mask");
		
		listOfImage = imageFolder.listFiles();
		listOfMask = maskFolder.listFiles();		
	}
	private int[] splitStringToInteger(String str) {		
		int [] array = new int[4];
		String [] arr = str.split(" ");
		for(int i=0;i<arr.length;i++) {
			array[i] = Integer.valueOf(arr[i]);
		}
		return array;
	}
	
	private void intializeSkinAndNonSkinMaping() {
		try {
			BufferedReader sr = new BufferedReader(new FileReader("skinMap.txt"));
			BufferedReader nsr = new BufferedReader(new FileReader("nonSkinMap.txt"));			
			
			String skinMap,nonSkinMap;
			skinMap = sr.readLine();
			nonSkinMap = nsr.readLine();
			int [] array = new int [4];
			while(skinMap!=null) {
				array = splitStringToInteger(skinMap);
				skin[array[0]][array[1]][array[2]] = array[3];
				array = splitStringToInteger(nonSkinMap);
				nonSkin[array[0]][array[1]][array[2]] = array[3];
				skinMap = sr.readLine();
				nonSkinMap = nsr.readLine();
			}
			sr.close();
			nsr.close();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void readImageFromFolder() {
		intializeSkinAndNonSkinMaping();
		getImageAndMaskList();		
		for(int k=0;k<listOfImage.length;k++) {			
			File imageFile = new File(listOfImage[k].toString());
			File maskFile = new File(listOfMask[k].toString());			
			BufferedImage image = null;
			BufferedImage mask = null;
			try {
				image = ImageIO.read(imageFile);
				mask = ImageIO.read(maskFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			width = image.getWidth();
			height = image.getHeight();
			for(int i=0;i<width;i++) {
				for(int j=0;j<height;j++) {
					imagePixelValue = image.getRGB(i, j);
					maskPixelValue = mask.getRGB(i, j);					
					imageAndMaskPixelMaping(imagePixelValue,maskPixelValue);				
				}
			}	

		}
		WriteSkinAndNonSkinMapToAFile();
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

	private int getR(int pixel) {
		int value = (pixel >> 16) & 0xff;
		return value;
	}
	
	private int getG(int pixel) {
		int value = (pixel >> 8) & 0xff;
		return value;
	}
	
	private int getB(int pixel) {
		int value = (pixel) & 0xff;
		return value;
	}
	
	public void createAMaskImage() {
		intializeSkinAndNonSkinMaping();
		File newImage = new File("ImageFolder\\2image\\image9.jpg");
		File out = new File("out.png");
					
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(newImage);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		width = image.getWidth();
		height = image.getHeight();
		BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				imagePixelValue = image.getRGB(i, j);
				int r = getR(imagePixelValue);
				int g = getG(imagePixelValue);
				int b = getB(imagePixelValue);
				double total = skin[r][g][b] + nonSkin[r][g][b];
				double x = skin[r][g][b] / total;
				//System.out.println(skin[r][g][b]+"  "+nonSkin[r][g][b]+"  "+x);
				if(x>0.2) {
					int z=256*256*256;
					output.setRGB(i, j, -1);
				}
				else
					output.setRGB(i, j, 0);
				
				imageAndMaskPixelMaping(imagePixelValue,maskPixelValue);				
			}
		}
		
		try {
			ImageIO.write(output, "png", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
