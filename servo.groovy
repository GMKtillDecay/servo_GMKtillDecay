import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import javafx.scene.paint.Color;

ArrayList<CSG> allCad=new ArrayList<>()

CSG srv = Vitamins.get("smallservo.stl")
          .movex(20)
srv.setColor(Color.GREEN)
allCad.add(srv)

srv = Vitamins.get("smallservo.stl")
          .movey(-40)
srv.setColor(Color.GREEN)
allCad.add(srv)

return allCad