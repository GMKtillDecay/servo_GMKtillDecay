import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import javafx.scene.paint.Color;

ArrayList<CSG> allCad=new ArrayList<>()

CSG srv = Vitamins.get("smallservo.stl")
srv.setColor(Color.GREEN)
allCad.add(srv)

return allCad