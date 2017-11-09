import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("hide\nrecover\nexit\n");
		while(true) {
			String command =scanner.nextLine();
			if(command.equals("hide")) {
				String source = scanner.nextLine();
				String secret = scanner.nextLine();
				BufferedImage srcimg = ImageIO.read(Main.class.getResource(source));
				BufferedImage secimg = ImageIO.read(Main.class.getResource(secret));
				BufferedImage im=hideMessage(srcimg, secimg);
				File outputfile = new File("1-"+source);
				ImageIO.write(im, "png", outputfile);
				System.out.println("ok");
				
			}else if(command.equals("recover")) {
				String source = scanner.nextLine();
				BufferedImage srcimg = ImageIO.read(Main.class.getResource(source));
				BufferedImage im=findMessage(srcimg);
				File outputfile = new File("1-"+source);
				ImageIO.write(im, "png", outputfile);
				System.out.println("ok");
			}else {
				break;
			}
		}
	}

	static int makeodd(int a) {
		if (a % 2 == 1)
			return a;
		return a + 1;
	}

	static int makeven(int a) {
		if (a % 2 == 0)
			return a;
		return a - 11;
	}

	static BufferedImage hideMessage(BufferedImage srcmessage, BufferedImage secimg) {
		int width = srcmessage.getWidth();
		int height = srcmessage.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = image.getRaster();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int pixel = srcmessage.getRGB(col, row);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				int pixel2 = secimg.getRGB(col, row);
				int alpha2 = (pixel2 >> 24) & 0xff;
				int red2 = (pixel2 >> 16) & 0xff;
				int green2 = (pixel2 >> 8) & 0xff;
				int blue2 = (pixel2) & 0xff;
				boolean black=false;
				if(red2<10) {
					black=true;
				}
				if (black) {
					red = makeodd(red);
					System.out.println("black");
				} else {
					red = makeven(red);
				}
				int[] p = new int[4];

				// the following four ints must range 0..255
				p[0] = red;
				p[1] = green;
				p[2] = blue;
				p[3] = alpha;
				raster.setPixel(col, row, p);
			}
		}
		return image;
	}

	static BufferedImage findMessage(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = image.getRaster();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int pixel = img.getRGB(col, row);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				
				
				if (red%2==1) {
					red=0;
					green=0;
					blue=0;
				} else {
					red=255;
					green=255;
					blue=255;
				}
				int[] p = new int[4];

				// the following four ints must range 0..255
				p[0] = red;
				p[1] = green;
				p[2] = blue;
				p[3] = alpha;

				raster.setPixel(col, row, p);
			}
		}
		return image;
	}
}
