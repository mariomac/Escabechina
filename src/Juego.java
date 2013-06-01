
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Juego extends Canvas {
    private static Image pers[];  //Imagenes de los personajes
    private static Image persout[]; //Imagenes de los personajes cuando les han pegado
    private static Image hoyoback=null;
    private static Image hoyofront=null;
    private static Image numeros[];
    private static Image guante=null;
    private static Image estrella=null;
    private static Image fin=null;


    public final static int CELDA_WIDTH=26;
    public final static int CELDA_HEIGHT=18;

    private static Image pagina=null;

    TareaJuego tarea=null;
    public Command comandoPausa=null;
    public Juego(TareaJuego tarea) {
        try {
            this.tarea=tarea;
            estrella=Image.createImage("/estrella.png");

            fin=Image.createImage("/fin.png");

            pers=new Image[4];
            pers[0]=Image.createImage("/cerdo.png");
            pers[1]=Image.createImage("/tio.png");
            pers[2]=Image.createImage("/pingu.png");
            pers[3]=Image.createImage("/perro.png");

            persout=new Image[4];
            persout[0]=Image.createImage("/cerdopegao.png");
            persout[1]=Image.createImage("/tiopegao.png");
            persout[2]=Image.createImage("/pingupegao.png");
            persout[3]=Image.createImage("/perropegao.png");

            numeros=new Image[10];
            for(int i=0;i<10;i++) {
                numeros[i]=Image.createImage("/numero_"+i+".png");
            }

            hoyoback=Image.createImage("/bujerofondo.png");
            hoyofront=Image.createImage("/bujerofrente.png");

            guante=Image.createImage("/guante.png");

            pagina=Image.createImage(CELDA_WIDTH*3+18,CELDA_HEIGHT*3);

            this.addCommand(comandoPausa=new Command("Pausar",Command.STOP,1));
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    protected void paint(Graphics graphics) {
        Graphics pg=pagina.getGraphics();
        int hoyo=0;
        for(int y=0;y<3;y++) {
            for(int x=0;x<3;x++) {
                pg.drawImage(hoyoback,x*CELDA_WIDTH,y*CELDA_HEIGHT,0);
                if(tarea.ocupado[hoyo]) {
                    Image tmpimg=null;
                    if(tarea.herido[hoyo]) {
                        tmpimg=persout[tarea.personaje[hoyo]];
                    } else {
                        tmpimg=pers[tarea.personaje[hoyo]];
                    }

                    pg.drawImage(tmpimg,x*CELDA_WIDTH,y*CELDA_HEIGHT+tarea.altura[hoyo],0);
                }
                pg.drawImage(hoyofront,x*CELDA_WIDTH,y*CELDA_HEIGHT,0);

                if(tarea.posicionguante==(hoyo+1) && tarea.programa.getEstado()==Escabechina.ESTADO_JUGANDO ) {
                    pg.drawImage(guante,x*CELDA_WIDTH+4-tarea.framesguante*2,y*CELDA_HEIGHT+4-tarea.framesguante*2,0);
                } else if( tarea.programa.getEstado()==Escabechina.ESTADO_GAMEOVER ) {
                    pg.drawImage(fin,14,10,0);
                }

//                pg.setColor(255,0,0);
//                pg.drawString("t: "+tarea.maxtmparriba,0,0,0);
                hoyo++;
            }

        }

        //dibujar estrellas
        for(int i=0;i<4;i++) {
            if(tarea.xstar[i]>0 && tarea.xstar[i]<(26*3) && tarea.ystar[i]<18*3) {
                pg.drawImage(estrella,tarea.xstar[i],tarea.ystar[i],0);
            }
        }

        //dibujar el marcador
        int pt=tarea.puntos;
        for(int i=0;i<3;i++) {
            pg.drawImage(numeros[pt%10],26*3,18*(2-i),0);
            pt=pt/10;
        }

        graphics.drawImage(pagina,0,0,0);

    }

    protected void keyPressed(int i) {
        super.keyPressed(i);
        switch(i) {
/* para cuando se juega con el teclado del ordena (emulador) */

            case Canvas.KEY_NUM1:
                tarea.posicionguante=7;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM2:
                tarea.posicionguante=8;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM3:
                tarea.posicionguante=9;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM4:
                tarea.posicionguante=4;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM5:
                tarea.posicionguante=5;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM6:
                tarea.posicionguante=6;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM7:
                tarea.posicionguante=1;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM8:
                tarea.posicionguante=2;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM9:
                tarea.posicionguante=3;
                tarea.framesguante=0;
                break;

/*
            case Canvas.KEY_NUM1:
                tarea.posicionguante=1;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM2:
                tarea.posicionguante=2;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM3:
                tarea.posicionguante=3;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM4:
                tarea.posicionguante=4;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM5:
                tarea.posicionguante=5;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM6:
                tarea.posicionguante=6;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM7:
                tarea.posicionguante=7;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM8:
                tarea.posicionguante=8;
                tarea.framesguante=0;
                break;
            case Canvas.KEY_NUM9:
                tarea.posicionguante=9;
                tarea.framesguante=0;
                break;
*/
        }

    }

}
