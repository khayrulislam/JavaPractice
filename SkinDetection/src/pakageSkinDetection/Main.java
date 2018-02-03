package pakageSkinDetection;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		/*ImageReading ir = new ImageReading();
		ir.readImageAndLearn("ImageFolder\\image","ImageFolder\\mask","skinNonSkinMap.txt");*/
		
		CreateImageMask cim = new CreateImageMask();
		cim.createAMaskImage("ImageFolder\\image\\image10.jpg","skinNonSkinMap.txt");
	}

}
