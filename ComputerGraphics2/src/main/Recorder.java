package main;

import etgg.GL;
import static etgg.GL.GL_RGB;
import static etgg.GL.GL_UNSIGNED_BYTE;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhudson
 */
public class Recorder {

    static int frame_counter = 0;
    static long last = 0;
    static final int width = 512, height = 512;
    static byte[] pix = new byte[width * height * 3];
    static byte[] Y = new byte[width*height];
    static byte[] Cb = new byte[width*height];
    static byte[] Cr = new byte[width*height];
    static Process encoder;
    static PrintStream encoder_s;

    static void record_frame() {
        if (encoder == null) {
            try {
                System.out.println("Recording to /tmp/cap.ogg");
                ProcessBuilder pb = new ProcessBuilder("theora_encode", "--output", "/tmp/cap.ogg",
                        "-f", "20", "-F", "1", "-");
                encoder = pb.start();
                encoder_s = new PrintStream(encoder.getOutputStream());
                encoder_s.print("YUV4MPEG2 W" + width + " H" + height + " F10:1 Ip A1:1 C444\n");
                Runtime.getRuntime().addShutdownHook( new Thread( () -> {
                    System.out.println("Shutting down encoder...");
                    encoder_s.close();
                    try {
                        encoder.waitFor();
                        System.out.println("Encoder done.");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }));
            } catch (IOException ex) {
                System.out.println("Cannot start encoder");
            }
        }

        long now = System.currentTimeMillis();
        if (now - last >= 50) {
            try {
                last = now;
                GL.glReadPixels(0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, pix);
                encoder_s.print("FRAME\n");
                encoder_s.flush();
                int deltastart = width*3;
                int j=0;
                for(int rowstart = pix.length-width*3;rowstart>=0;rowstart -= deltastart){
                    int k = rowstart;
                    for (int i = 0;i<width;i++,k+=3){
                        int r = 0xff & pix[k] ;
                        int g = 0xff & pix[k + 1] ;
                        int b = 0xff & pix[k + 2] ;
                        //y=0.299*r+0.587*g+0.114*b
                        //1000y = 299*r + 587*g + 114*b
                        //cb=128+-0.169r+-0.331g+0.5b
                        //1000cb = 128000 + -169r + -331g + 500b
                        //cr=128+0.5r+-0.419g+-0.081b
                        //1000cr = 128000 + 500r - 419g - 81b
                        //int y = 299 * r + 587 * g + 114 * b;
                        //int cb = 128000 - 169 * r - 331 * g + 500 * b;
                        //int cr = 128000 + 500 * r - 419 * g - 81 * b;

                        //full range, but altered from source at 
                        //http://www.equasys.de/colorconversion.html
                        //so we can divide by 1024 at the end instead of
                        //using fp math
                        int y = 306 * r + 601 * g + 116 * b;
                        int cb = 131072 - 173 * r - 339 * g + 512 * b;
                        int cr = 131072 + 512 * r - 429 * g - 83 * b;
                        y >>= 10;
                        cb >>= 10;
                        cr >>= 10;

                        //sdtv, to allow for roundoff error
                        //y=16+0.257r + 0.504g + 0.098b
                        //cb=128+-0.148r+-0.291g+0.439b
                        //cr=128+0.439r-0.368b-0.071b
    //                    int y = 16384 + 263*r + 516*g + 100*b;
    //                    int cb = 131072 - 151*r -298*g + 449*b;
    //                    int cr = 131072 + 449*r -377*b - 72*b;
    //                    y>>=10;
    //                    cb>>=10;
    //                    cr>>=10;

                        Y[j] = (byte)y;
                        Cb[j] = (byte)cb;
                        Cr[j] = (byte)cr;
                        j++;
                    }
                }
                encoder_s.write(Y);
                encoder_s.write(Cb);
                encoder_s.write(Cr);
//                InputStream is = encoder.getErrorStream();
//                if (is.available() > 0) {
//                    byte[] b = new byte[is.available()];
//                    is.read(b);
//                    System.out.print(new String(b));
//                }

            } catch (IOException ex) {
                Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
