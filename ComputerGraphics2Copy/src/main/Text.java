package main;

import static etgg.GL.GL_ARRAY_BUFFER;
import static etgg.GL.GL_DYNAMIC_DRAW;
import static etgg.GL.GL_FLOAT;
import static etgg.GL.GL_STATIC_DRAW;
import static etgg.GL.GL_TRIANGLES;
import static etgg.GL.glBindBuffer;
import static etgg.GL.glBindVertexArray;
import static etgg.GL.glBufferData;
import static etgg.GL.glDrawArrays;
import static etgg.GL.glEnableVertexAttribArray;
import static etgg.GL.glGenBuffers;
import static etgg.GL.glGenVertexArrays;
import static etgg.GL.glVertexAttribPointer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import math3d.vec3;

public class Text {
    Texture2DArray texture;
    String txt;
    int x,y;
    int maxw,maxh;
    TreeMap<Character, int[]> metrics = new TreeMap<>();
    int vao;
    int positionBuffer,textureCoordBuffer;
    
    Text(String fontFilename, String text, int x, int y ){
        try{
            this.x=x;
            this.y=y;
            this.txt = text;
            texture = new ImageTexture2DArray(fontFilename);
            ZipFile zf = new ZipFile(fontFilename);
            ZipEntry ze = zf.getEntry("metrics.txt");
            Scanner in = new Scanner(zf.getInputStream(ze));
            maxw = in.nextInt();
            maxh = in.nextInt();
            while(in.hasNextInt()){
                int ascii = in.nextInt();
                String tmp = in.next();
                if(tmp.equals("NaN"))
                    tmp = "0";
                int w = Integer.parseInt(tmp);
                tmp = in.next();
                if(tmp.equals("NaN"))
                    tmp="0";
                int h = Integer.parseInt(tmp);
                metrics.put( (char)ascii, new int[]{w,h} );
            }
            
            int[] tmp = new int[1];
            glGenVertexArrays(1, tmp);
            this.vao = tmp[0];
            glBindVertexArray(vao);

            glGenBuffers(1,tmp);
            positionBuffer = tmp[0];
            glBindBuffer(GL_ARRAY_BUFFER,positionBuffer);
            glBufferData(GL_ARRAY_BUFFER,0,(byte[])null,GL_STATIC_DRAW);
            glVertexAttribPointer(Program.POSITION_INDEX, 3, GL_FLOAT,
                    false, 3*4, 0);
            glEnableVertexAttribArray(Program.POSITION_INDEX);
            glGenBuffers(1,tmp);
            
            glGenBuffers(1,tmp);
            textureCoordBuffer = tmp[0];
            glBindBuffer(GL_ARRAY_BUFFER,textureCoordBuffer);
            glBufferData(GL_ARRAY_BUFFER,0,(byte[])null,GL_STATIC_DRAW);
            glVertexAttribPointer(Program.TEXCOORD_INDEX, 2, GL_FLOAT,
                    false, 2*4, 0);
            glEnableVertexAttribArray(Program.TEXCOORD_INDEX );
            glBindVertexArray(0);
        }
        catch(IOException ex){
            throw new RuntimeException(ex);
        }
        setText(txt);
    }
    void setText(String txt){
        this.txt = txt;
        int x = this.x;
        int y = this.y;
        
        //4 bytes per float, 3 floats per vertex (position), 6 vertices per character
        ByteBuffer posbb = ByteBuffer.allocate(4*3*6*txt.length());
        posbb.order(ByteOrder.nativeOrder());
        FloatBuffer posfb = posbb.asFloatBuffer();
        
        //4 bytes per float, 2 floats per vertex (position), 6 vertices per character
        ByteBuffer texbb = ByteBuffer.allocate(4*2*6*txt.length());
        texbb.order(ByteOrder.nativeOrder());
        FloatBuffer texfb = texbb.asFloatBuffer();
        
        for(int i=0;i<txt.length();++i){
            char c = txt.charAt(i);
            if( c == '\n' ){
                x = this.x;
                y -= maxh;
            }
            else{
                int w = metrics.get(c)[0];
                int h = maxh;


                //   a ___d 
                //    |\  |
                //    | \ |
                //   b|__\|c
                
                //a
                posfb.put(x);
                posfb.put(y);
                posfb.put(c-32);
                texfb.put(0);
                texfb.put(0);
                
                //b
                posfb.put(x);
                posfb.put(y+h);
                posfb.put(c-32);
                texfb.put(0);
                texfb.put(h);
                
                //c
                posfb.put(x+w);
                posfb.put(y+h);
                posfb.put(c-32);
                texfb.put(w);
                texfb.put(h);

                //////////

                //a
                posfb.put(x);
                posfb.put(y);
                posfb.put(c-32);
                texfb.put(0);
                texfb.put(0);
                
                //c
                posfb.put(x+w);
                posfb.put(y+h);
                posfb.put(c-32);
                texfb.put(w);
                texfb.put(h);
                
                //d
                posfb.put(x+w);
                posfb.put(y);
                posfb.put(c-32);
                texfb.put(w);
                texfb.put(0);

                x+=w;
            } //endif
        } //end for
        byte[] B = posbb.array();
        glBindBuffer(GL_ARRAY_BUFFER, positionBuffer);
        glBufferData(GL_ARRAY_BUFFER,B.length,B,GL_DYNAMIC_DRAW);
        B = texbb.array();
        glBindBuffer(GL_ARRAY_BUFFER, textureCoordBuffer);
        glBufferData(GL_ARRAY_BUFFER,B.length,B,GL_DYNAMIC_DRAW);
    }
    
    void draw(){
        glBindVertexArray(vao);
        Program.current.setUniform("fontTexture",texture); 
        Program.current.setUniform("fontColor",new vec3(1,1,1));
        glDrawArrays(GL_TRIANGLES, 0, txt.length()*6);
    }
}
