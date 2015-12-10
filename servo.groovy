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
			.rotz(-90)
			.movex(1.5)
			
			.movey(shaftToShortSideDistance-3)
			.movez(-200)
			
          return shaft.union(flange,body,cord)
}
if(args==null)// Deafult to the standard micro servo
	return servoFactory(11.7,//servoThinDimentionThickness
			23.6,// servoThickDimentionThickness
			25.16, // servoShaftSideHeight
			3.8,// outputShaftDiameter
                        5.13, //shaftToShortSideDistance
                       9.8,// shaftToShortSideFlandgeEdge
                        12.9, // tipOfShaftToBottomOfFlange
                        3.0 ,//flangeThickness
                        32.3,// flangeLongDimention
                        10.16//bottomOfFlangeToTopOfBody
          )
if(args.size()!=10)
	throw new RuntimeException("Arguments are : \n"+		
		"double servoThinDimentionThickness,\n"+
		"double servoThickDimentionThickness,\n"+
		"double servoShaftSideHeight,\n"+
		"double outputShaftDimeter,\n"+
		"double shaftToShortSideDistance,\n"+
		"double shaftToShortSideFlandgeEdge,\n"+
		"double tipOfShaftToBottomOfFlange,\n"+
		"double flangeThickness,\n"+
		"double flangeLongDimention,\n"+
		"double bottomOfFlangeToTopOfBody")
return servoFactory(args.get(0),//servoThinDimentionThickness
			args.get(1),// servoThickDimentionThickness
			args.get(2), // servoShaftSideHeight
			args.get(3),// outputShaftDiameter
                       args.get(4), //shaftToShortSideDistance
                       args.get(5),// shaftToShortSideFlandgeEdge
                        args.get(6), // tipOfShaftToBottomOfFlange
                        args.get(7) ,//flangeThickness
                        args.get(8),// flangeLongDimention
                        args.get(9)//bottomOfFlangeToTopOfBody
          )     
