// http://coursera.cs.princeton.edu/algs4/assignments/seamCarving.html
import java.awt.Color;

public class SeamCarver {

    private int[][] pix;  // a 2D array to represent a picture (RGB values)
    private double[][] energies;  // energy of each pixel

    private final int borderEnergy;
    private final double DINF;
    private int H, W;

    public SeamCarver(Picture picture) {
        borderEnergy = 3*255*255;
        DINF = Double.POSITIVE_INFINITY;
        W = picture.width();
        H = picture.height();
        pix = new int[W][H];
        energies = new double[W][H];

        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                pix[i][j] = picture.get(i, j).getRGB();
            }
        }

        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                energies[i][j] = energy(i, j);
            }
        }
    }
    
    // It x,y border?
    private boolean isBorder(int x, int y) {
        if (x < 0 || x >= W || y < 0 || y >= H) {
            throw new IndexOutOfBoundsException("isBorder() Out of range");
        }
        if (x == 0 || y == 0 || x == this.width() - 1 || y == this.height() - 1)
            return true;
        return false;
    }

    private int grad1D(Color c1, Color c2) {
        int dR = c1.getRed() - c2.getRed();
        int dG = c1.getGreen() - c2.getGreen();
        int dB = c1.getBlue() - c2.getBlue();
        return dR*dR + dG*dG + dB*dB;
    }

    // find a vertical seam
    private int[] findSeam(boolean transpose) {
        int wi = W, he = H;
        if (transpose) {
            wi = H;
            he = W;
        }
        // cumulative energy to this pixel
        double[][] energyTo = new double[wi][he];
        // The x-coordinate of the previous row
        int[][] pixelTo = new int[wi][he];
        // Initialize energyTo[][]
        for (int i = 0; i < wi; i++) {
            for (int j = 0; j < he; j++) {
                energyTo[i][j] = DINF;
            }
        }

        // copy the energy in the top row
        for (int i = 0; i < wi; i++) {
            double e;
            if (transpose)
                e = energies[0][i];
            else
                e = energies[i][0];
            energyTo[i][0] = e;
            pixelTo[i][0] = 0;
        }
        // go through it row by row
        for (int j = 1; j < he; j++) {
            for (int i = 0; i < wi; i++) {
                double e;
                if (transpose)
                    e = energies[j][i];
                else
                    e = energies[i][j];
                int xmin = -1;
                double emin = DINF;
                for (int k = -1; k <= 1; k++) {
                    if (i+k < 0 || i+k >= wi)
                        continue;
                    if (energyTo[i][j] > e + energyTo[i+k][j-1]) {
                        energyTo[i][j] = e + energyTo[i+k][j-1];
                        xmin = i+k;
                    }
                }
                pixelTo[i][j] = xmin;
            }
        }
        // Find the minimum cumulative energy at the bottom row
        int xmin = -1;
        double emin = DINF;
        for (int i = 0; i < wi; i++) {
            if (energyTo[i][he-1] < emin) {
                emin = energyTo[i][he-1];
                xmin = i;
            }
        }
        // Build the path back to top
        int[] path = new int[he];
        path[he-1] = xmin;
        for (int j = 1; j < he; j++) {
            path[he-j-1] = pixelTo[path[he-j]][he-j];
        }       
        return path;
    }


    // current picture
    public Picture picture() {
        Picture P = new Picture(W, H);
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                P.set(i, j, new Color(pix[i][j]));
            }
        }
        return P;
    }

    // width  of current picture
    public     int width() {
        return W;
    }

    // height of current picture
    public     int height() {
        return H;
    }

    // energy of pixel at column x and row y in current picture
    public  double energy(int x, int y) {
        if (x < 0 || x >= W || y < 0 || y >= H) {
            throw new IndexOutOfBoundsException("energy() Out of range");
        }
        if (isBorder(x, y))
            return borderEnergy;

        Color cxL = new Color(pix[x-1][y]); // Left
        Color cxR = new Color(pix[x+1][y]); // Right
        Color cyB = new Color(pix[x][y-1]); // Bottom
        Color cyT = new Color(pix[x][y+1]); // Top

        return grad1D(cxL, cxR) + grad1D(cyB, cyT);
    }

    // sequence of indices for horizontal seam in current picture
    public   int[] findHorizontalSeam() {
        return findSeam(true);
    }

    // sequence of indices for vertical   seam in current picture
    public   int[] findVerticalSeam() {
        return findSeam(false);
    }

    private void checkSeam(int[] a, int length) {
        if (a.length != length)
            throw new IllegalArgumentException("array has wrong length");
        for (int i = 1; i < length; i++) {
            if (Math.abs(a[i]-a[i-1]) > 1)
                throw new IllegalArgumentException("array is not continuous");
        }
    }

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] a) {
        if (H <= 1)
            throw new IllegalArgumentException("picture too small");
        checkSeam(a, W);
        for (int i = 0; i < W; i++) {
            for (int j = a[i]; j < H-1; j++) {
                // move energy and pixel over by one
                energies[i][j] = energies[i][j+1];
                pix[i][j] = pix[i][j+1];
            }
        }
        H--;
        // update energy
        for (int i = 0; i < W; i++) {
            energies[i][a[i]] = energy(i, a[i]);
            if (a[i] > 0)
                energies[i][a[i]-1] = energy(i, a[i]-1);
        }
    }
    
    // remove vertical   seam from current picture 
    public    void removeVerticalSeam(int[] a) {
        if (W <= 1)
            throw new IllegalArgumentException("picture too small");
        checkSeam(a, H);
        for (int j = 0; j < H; j++) {
            for (int i = a[j]; i < W-1; i++) {
                energies[i][j] = energies[i+1][j];
                pix[i][j] = pix[i+1][j];
            }
        }
        W--;
        // update energy
        for (int j = 0; j < H; j++) {
            energies[a[j]][j] = energy(a[j], j);
            if (a[j] > 0)
                energies[a[j]-1][j] = energy(a[j]-1, j);
        }
    }

    public static void main(String[] args) {

        Picture inputImg = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(inputImg);

        StdOut.println(sc.width() + "  " + sc.height());
        Picture outputImg = sc.picture();
        inputImg.show();
        outputImg.show();
    }
}
