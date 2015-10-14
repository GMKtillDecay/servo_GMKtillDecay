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

CSG servoFactory(   
		double servoThinDimentionThickness,
		double servoThickDimentionThickness,
		double outputShaftDimeter,
		double shaftToShortSideDistance,
		double shaftToShortSideFlandgeEdge,
		double tipOfShaftToBottomOfFlange,
		double flangeThickness,
		double flangeLongDimention
		){
        CSG shaft = new Cylinder(	outputShaftDimeter/2, // Radius at the top
				outputShaftDimeter/2, // Radius at the bottom
				tipOfShaftToBottomOfFlange, // Height
			         (int)36//resolution
			         ).toCSG()
			         
	CSG flange = new Cube(	servoThinDimentionThickness,// X dimention
			flangeLongDimention,// Y dimention
			flangeThickness//  Z dimention
			).toCSG()
			.noCenter()
         return shaft.unionAll(flange)
}

allCad.add(servoFactory(11.7,//servoThinDimentionThickness
			23.6,// servoThickDimentionThickness
			3.8,// outputShaftDiameter
                        5.13, //shaftToShortSideDistance
                        10.0,// shaftToShortSideFlandgeEdge
                        12.9, // tipOfShaftToBottomOfFlange
                        1.6 ,//flangeThickness
                        32.3// flangeLongDimention
                        
          ))

return allCad