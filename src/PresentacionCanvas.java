
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class PresentacionCanvas extends Canvas {
    private Image presentacion = null;
    public PresentacionCanvas() {
        try {
            presentacion=Image.createImage("/presentacion.png");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        this.addCommand(new Command("Jugar",Command.OK,1));
        this.addCommand(new Command("Salir",Command.EXIT,0));

    }


    protected void paint(Graphics graphics) {
        graphics.setColor(0,0,0);
        graphics.fillRect(0,0,this.getWidth(),this.getHeight());
        graphics.drawImage(presentacion,0,0,0);
    }
}
