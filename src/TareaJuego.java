/*
 *    Escabechina v1.0
 *    Autor: Mario Macías Lloret
 *    Juego creado como demostración para el proyecto de fin de carrera
 *    "
 *
 */

import java.util.TimerTask;
import java.util.Random;

public class TareaJuego extends TimerTask {
    private boolean pausado=false;
    Escabechina programa=null;
    public final int ESTADO_SUBE = 0;
    public final int ESTADO_PARADO = 1;
    public final int ESTADO_BAJA = 2;

    static Random rnd=new Random(System.currentTimeMillis());

    int vidas=3;
    int golpesseguidos=0;

    int nivel=0;
    int puntos=0;
    boolean ocupado[]=new boolean[9]; //indica si un agujero esta ocupado por algun personaje
    int estado[]=new int[9]; //indica qué hace el muñeco (sube, baja, esta quieto...)
    boolean herido[]=new boolean[9]; //si true, al personaje le han dado

    int maxtmparriba=1300; //inicialmente el muñeco esta 1 segundo parado
    int tmparriba[]=new int[9]; //indica cuanto tiempo lleva un muñeco arriba

    int velocidad=1; //velocidad a la que suben y bajan los muñecos
    int altura[]=new int[9]; //altura a la que estan los personajes
    int personaje[]=new int[9]; //cual de los 4 personajes es (0..3)

    public final int margen= 8; //Indica a qué distancia en pixels de su maxima altura se puede golpear a un muñeco

    int intervalo=3000; //El intervalo en ms entre que aparece un muñeco o otro
    int tactual=2300; //cuenta el tiempo que lleva, cuando tactual>intervalo --> tactual=0; e intervalo--;

    int posicionguante=0;  //Nº de agujero donde esta golpeando el guante (0=no golpea)
    int maxframesguante=3; //Nº de frames en que el guante esta visible
    int framesguante=0;

    //datos para visualizar las estrellitas cuando se golpea
    int xstar[]=new int[4]; //posicion x
    int vxstar[]=new int[4]; //velocidad x (-4..4)
    int ystar[]=new int[4]; //posicion y
    int vystar[]=new int[4]; //velocidad y (-4..infinito) <-- usaremos aceleracion de la gravedad

    public TareaJuego(Escabechina programa) {
        pausado=false;
        this.programa=programa;
        for(int i=0;i<9;i++) {
            ocupado[i]=false;
        }
    }
    public void run() {
        //Esta parte del IF hace el Game Over
        if(programa.getEstado()==Escabechina.ESTADO_GAMEOVER) {
            for(int i=0;i<9;i++) {
                if(!ocupado[i]) {
                    ocupado[i]=true;
                    personaje[i]=i%4;
                    estado[i]=ESTADO_SUBE;
                    herido[i]=false;
                    altura[i]=18;
                    break;
                } else {
                    if(estado[i]==ESTADO_SUBE) {
                        if(altura[i]>0) {
                            altura[i]--;
                        } else {
                            estado[i]=ESTADO_BAJA;
                        }
                    } else {
                        if(altura[i]<2) {
                            altura[i]++;
                        } else {
                            estado[i]=ESTADO_SUBE;
                        }
                    }
                }
            }
        } else if(programa.getEstado()==Escabechina.ESTADO_JUGANDO) {
            //Movimiento de las estrellas
            for(int i=0;i<4;i++) {
                if(xstar[i]>0 && xstar[i]<(26*3) && ystar[i]<18*3) {
                    xstar[i]+=vxstar[i];
                    ystar[i]+=vystar[i];
                    vystar[i]+=1;
                }
            }
            //control del guante
            if(posicionguante>0) {
                framesguante++;
                if((altura[posicionguante-1]-margen)<0 && ocupado[posicionguante-1] && !herido[posicionguante-1]) {
                    //preparamos los datos de las estrellas
                    for(int i=0;i<4;i++) {
                        vxstar[i]=rnd.nextInt()%4;
                        vystar[i]=rnd.nextInt()%6;
                        xstar[i]=((posicionguante-1)%3)*26+13;
                        ystar[i]=((posicionguante-1)/3)*18+7;
                    }

                    herido[posicionguante-1]=true;

                    golpesseguidos++;
                    if(golpesseguidos==20) {
                        vidas=vidas==10?10:vidas+1;
                        golpesseguidos=0;
                    }
                    //estado[posicionguante-1]=ESTADO_BAJA;
                    puntos++;
                }
                if(framesguante>maxframesguante) {
                    framesguante=0;
                    posicionguante=0;
                }
            }         

            tactual+=Escabechina.DELAY_MS;
            //Mirar si hay que sacar algun muñeco nuevo
            if(tactual>intervalo) {
                tactual=10;

                if(intervalo<800) {
                    intervalo-=45;
                    if(intervalo<(400-nivel)) {
                        //cada vez que sobrepase el intervalo, subimos el nivel un poco
                        intervalo=700-nivel;
                        if(nivel<105) {
                            nivel+=15;
                        }

                    }
                } else {
                    intervalo-=intervalo/7;
                }
                velocidad=(3-(intervalo/700));
                if(velocidad<1) velocidad=1;

                maxtmparriba-=10;
                if(maxtmparriba<150) {
                    maxtmparriba=150;
                }
                int hoyo=Math.abs(rnd.nextInt()%9);
                int hoyosocupados=0;
                while(ocupado[hoyo] && hoyosocupados<9) {
                    hoyo=(hoyo+1)%9;
                    hoyosocupados=(hoyosocupados+1)%9;
                }
                //cuando vaya muy rapido, todos los hoyos pueden estar ocupados
                //de esta manera, esperamos al siguiente frame, hasta que haya alguno libre
                if(hoyosocupados<9) {
                    tactual=0;
                    estado[hoyo]=ESTADO_SUBE;
                    herido[hoyo]=false;
                    ocupado[hoyo]=true;
                    altura[hoyo]=18;
                    personaje[hoyo]=Math.abs(rnd.nextInt()%4);
                    tmparriba[hoyo]=0;
                }
            }
            //Controlar los que ya hay moviendose
            for(int hoyo=0;hoyo<9;hoyo++) {
                if(ocupado[hoyo]) {
                    switch(estado[hoyo]) {
                        case ESTADO_SUBE:
                            altura[hoyo]-=velocidad;
                            if(altura[hoyo]<0) {
                                altura[hoyo]=0;
                                estado[hoyo]=ESTADO_PARADO;
                            }
                            break;
                        case ESTADO_PARADO:
                            tmparriba[hoyo]+=Escabechina.DELAY_MS;
                            if(tmparriba[hoyo]>maxtmparriba) {
                                estado[hoyo]=ESTADO_BAJA;
                            }
                            break;
                        case ESTADO_BAJA:
                            altura[hoyo]+=velocidad;
                            if(altura[hoyo]>18) {
                                altura[hoyo]=18;
                                ocupado[hoyo]=false;
                                if(!herido[hoyo]) {
                                    vidas--;
                                    golpesseguidos=0;
                                    if(vidas==0) {
                                        programa.setEstado(Escabechina.ESTADO_GAMEOVER);
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
        programa.juego.repaint();
    }

}
