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

CSG servoFactory(   double outputShaftDimeter,
                    double shaftToShortSideDistance,
                    double tipOfShaftToBottomOfFlange
                    ){
          CSG shaft = new Cylinder(	outputShaftDimeter/2, // Radius at the top
				outputShaftDimeter/2, // Radius at the bottom
				tipOfShaftToBottomOfFlange, // Height
			         (int)36//resolution
			         ).toCSG()
         return shaft
}

allCad.add(servoFactory(3.8,// outputShaftDiameter
                        5.13, //shaftToShortSideDistance
                        12.9 // tipOfShaftToBottomOfFlange
          ))

return allCad