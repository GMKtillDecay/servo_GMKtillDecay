import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import javafx.scene.paint.Color;

CSG servoFactory(   
		double servoThinDimentionThickness,
		double servoThickDimentionThickness,
		double servoShaftSideHeight,
		double outputShaftDimeter,
		double shaftToShortSideDistance,
		double shaftToShortSideFlandgeEdge,
		double tipOfShaftToBottomOfFlange,
		double flangeThickness,
		double flangeLongDimention,
		double bottomOfFlangeToTopOfBody
		){
		CSG shaft = new Cylinder(	outputShaftDimeter/2, // Radius at the top
				outputShaftDimeter/2, // Radius at the bottom
				tipOfShaftToBottomOfFlange, // Height
			         (int)36//resolution
			         ).toCSG()
			         
		CSG flange = new Cube(	servoThinDimentionThickness,// X dimention
			flangeLongDimention,// Y dimention
			flangeThickness//  Z dimention
			)
			.noCenter()
			.toCSG()
			.movey(shaftToShortSideFlandgeEdge-flangeLongDimention)
			.movex(-servoThinDimentionThickness/2)
		CSG body = new Cube(	
			servoThinDimentionThickness,// X dimention
			servoThickDimentionThickness,// Y dimention
			servoShaftSideHeight//  Z dimention
			)
			.noCenter()
			.toCSG()
			.movez(bottomOfFlangeToTopOfBody-servoShaftSideHeight)
	    		.movex(-servoThinDimentionThickness/2)
	    		.movey(shaftToShortSideDistance-servoThickDimentionThickness)
	    	CSG cord = new Cube(
			9,// x dimention	
			3,// y dimention
			200//  Z dimention
			)
			.noCenter()
			.toCSG()
			.movex(-4.5)
			.movey(shaftToShortSideDistance-3)
			.movez(-200)
          return shaft.union(flange,body,cord)
}

return servoFactory(11.7,//servoThinDimentionThickness
			23.6,// servoThickDimentionThickness
			25.16, // servoShaftSideHeight
			3.8,// outputShaftDiameter
                        5.13, //shaftToShortSideDistance
                       11.0,// shaftToShortSideFlandgeEdge
                        12.9, // tipOfShaftToBottomOfFlange
                        3.0 ,//flangeThickness
                        33.3,// flangeLongDimention
                        10.16//bottomOfFlangeToTopOfBody
                        
          )
