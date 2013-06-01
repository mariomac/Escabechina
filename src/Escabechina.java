import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.util.Timer;

/* ***************************************
   **  Escabechina ***********************
   *************************************************************************
   *   Juego creado por Mario Macías Lloret para el proyecto de fín        *
   * de carrera en ingeniería informática: "Creación de juegos para J2ME"  *
   * Universidad de Las Palmas de Gran Canaria                             *
   * Curso 2004-2005                                                       *
   *************************************************************************
*/
public class Escabechina extends MIDlet implements CommandListener{
    private Timer timer=null;
    final static public int ESTADO_PRESENTACION=0;
    final static public int ESTADO_JUGANDO=1;
    final static public int ESTADO_PAUSA=2;
    final static public int ESTADO_GAMEOVER=3;

    final public static int DELAY_MS = 50;

    Display display=Display.getDisplay(this);
    private int estado=ESTADO_PRESENTACION;

    TareaJuego tareaJuego=null;
    Juego juego=null;

    protected void startApp() throws MIDletStateChangeException {
        System.out.println("1");
        switch(getEstado()) {
            case ESTADO_PAUSA:
            case ESTADO_JUGANDO:
                System.out.println("2");
                setEstado(ESTADO_PAUSA);
                break;
            default:
                System.out.println("3");
                setEstado(ESTADO_PRESENTACION);
                break;
        }
    }

    public void setEstado(int estado) {
        this.estado=estado;
        switch(estado) {
            case ESTADO_PRESENTACION:
                PresentacionCanvas canvas=new PresentacionCanvas();
                canvas.setCommandListener(this);
                display.setCurrent(canvas);
                canvas.repaint();
                if(timer!=null) {
                    timer.cancel();
                    timer=null;
                }
                tareaJuego=null;
                juego=null;
                break;
            case ESTADO_PAUSA:
                PausaCanvas pausa=new PausaCanvas();
                pausa.setCommandListener(this);
                display.setCurrent(pausa);
                break;
            case ESTADO_JUGANDO:
                if(tareaJuego==null) {
                    tareaJuego=new TareaJuego(this);
                }
                if(juego==null) {
                    juego=new Juego(tareaJuego);
                }
                juego.setCommandListener(this);
                if(timer==null) {
                    timer=new Timer();
                    timer.schedule(tareaJuego,DELAY_MS,DELAY_MS);
                }
                display.setCurrent(juego);
                break;
            case ESTADO_GAMEOVER:
                juego.removeCommand(juego.comandoPausa);
                juego.addCommand(new Command("Salir",Command.STOP,1));
                return;
        }
        System.gc(); //Garbage collection manual
    }

    public int getEstado() {
        return estado;
    }

    protected void pauseApp() {
        System.out.println("**pasuando");
        setEstado(ESTADO_PAUSA);
    }

    protected void destroyApp(boolean b) throws MIDletStateChangeException {
    }

    public void commandAction(Command command, Displayable displayable) {
        try {
            System.out.println("4");
            if(estado==ESTADO_PRESENTACION) {
                switch(command.getCommandType()) {
                    case Command.OK:
                        System.out.println("5");
                        setEstado(ESTADO_JUGANDO);
                        break;
                    case Command.EXIT:
                        System.out.println("6");
                        this.notifyDestroyed();
                        break;
                }
            } else if(estado==ESTADO_JUGANDO) {
                if(command.getCommandType()==Command.STOP) {
                    setEstado(ESTADO_PAUSA);
                }
            } else if(estado==ESTADO_GAMEOVER) {
                if(command.getCommandType()==Command.STOP) {
                    setEstado(ESTADO_PRESENTACION);
                }
            } else if(estado==ESTADO_PAUSA) {
                switch(command.getCommandType()) {
                    case Command.OK:
                        setEstado(ESTADO_JUGANDO);
                        break;
                    case Command.EXIT:
                        setEstado(ESTADO_PRESENTACION);
                        break;
                }
            }


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
