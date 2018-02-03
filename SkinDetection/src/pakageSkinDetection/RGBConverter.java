package pakageSkinDetection;

public class RGBConverter {
	
	
	public int [][][]skin = new int[256][256][256];
	public int [][][]nonSkin = new int[256][256][256];
	
	
	public int getR(int pixel) {
		int value = (pixel >> 16) & 0xff;
		return value;
	}
	
	public int getG(int pixel) {
		int value = (pixel >> 8) & 0xff;
		return value;
	}
	
	public int getB(int pixel) {
		int value = (pixel) & 0xff;
		return value;
	}
	
}
