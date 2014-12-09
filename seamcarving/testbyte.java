import java.awt.Color;

public class testbyte {

    public static void main(String[] args) {
	
	Color c = new Color(10, 20, 30);
	StdOut.println(c);
	StdOut.println(c.getRGB());
	Color c2 = new Color(c.getRGB());
	StdOut.println(c2);
	StdOut.println(c2.getRGB());

    }
}
