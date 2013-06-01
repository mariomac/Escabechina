
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;

public class PausaCanvas extends Canvas {
    private Image pausa = null;
    public PausaCanvas() {
        try {
            pausa=Image.createImage("/pausa.png");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        this.addCommand(new Command("Continuar",Command.OK,1));
        this.addCommand(new Command("Salir",Command.EXIT,0));

    }


    protected void paint(Graphics graphics) {
        graphics.setColor(0,0,0);
        graphics.fillRect(0,0,this.getWidth(),this.getHeight());
        graphics.drawImage(pausa,0,0,0);
    }

}
