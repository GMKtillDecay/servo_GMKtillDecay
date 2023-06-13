import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.parametrics.*;
CSG servoFactory(   
		double servoThinDimentionThickness,
		double servoThickDimentionThickness,
		double servoShaftSideHeight,
		double outputShaftDimeter,
		double shaftToShortSideDistance,
		double tipOfShaftToBottomOfFlange,
		double flangeThickness,
		double flangeLongDimention,
		double bottomOfFlangeToTopOfBody,
		HashMap<String,Object> servoConfig
		){
		shaftToShortSideDistance= servoThinDimentionThickness/2
		LengthParameter tailLength		= new LengthParameter("Cable Cut Out Length",30,[500,0.01])
		LengthParameter printerOffset 			= new LengthParameter("printerOffset",0.5,[1.2,0])
		double totalFlangLen = (flangeLongDimention-servoThickDimentionThickness)/2
		double shaftToShortSideFlandgeEdge = shaftToShortSideDistance+totalFlangLen
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
			
		CSG flangeBrace = new Cube(	flangeThickness,// X dimention
			flangeLongDimention,// Y dimention
			flangeThickness*2//  Z dimention
			).toCSG()
			.toYMax()
			.movey(flange.getMaxY())
			.movex(servoThinDimentionThickness/2)
			.movez(flangeThickness)
		flange=flange
		      .union(flangeBrace)
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
			6,// y dimention
			tailLength.getMM()>0?tailLength.getMM():1//  Z dimention
			)
			.noCenter()
			.toCSG()
			.rotz(-90)
			.movex(3)
			
			.movey(shaftToShortSideDistance-3)
			.movez(-tailLength.getMM())
		CSG builtServo = CSG.unionAll([flange,body])
		if(tailLength.getMM()>flangeThickness) {
			builtServo=builtServo.union(cord)
		}
		if(servoConfig!=null){
			if(servoConfig.get("numberOfHolesPerFlange")!=null){
				LengthParameter boltLength		= new LengthParameter("Servo Bolt Length",bottomOfFlangeToTopOfBody,[500,0.01])
				boltLength.setMM(bottomOfFlangeToTopOfBody)
				double numberOfHolesPerFlange=Double.parseDouble(servoConfig.get("numberOfHolesPerFlange").toString())
				double holeDiameter=Double.parseDouble(servoConfig.get("holeDiameter").toString())
				double holeEdgetoHoleEdgeLongDistance=Double.parseDouble(servoConfig.get("holeEdgetoHoleEdgeLongDistance").toString())
				boolean holesUp = servoConfig.get("holeOrentation").toString().contains("up")
				boolean noBolts = servoConfig.get("holeOrentation").toString().contains("none") ||servoConfig.get("holeOrentation").toString().contains("null") 
				double shaftToHoleSide = -((flangeLongDimention-holeEdgetoHoleEdgeLongDistance)/2)+shaftToShortSideFlandgeEdge
				StringParameter boltSizeParam = new StringParameter("Servo Bolt Size","M5",Vitamins.listVitaminSizes("capScrew"))
				LengthParameter boltLengthDefault		= new LengthParameter("Bolt Length",10,[180,10])
				boltLengthDefault.setMM(boltLength.getMM())
				HashMap<String, Object>  boltData = Vitamins.getConfiguration( "capScrew",boltSizeParam.getStrValue())								
				headDiameter=Double.parseDouble(boltData.get("headDiameter").toString())
				headHeight=Double.parseDouble(boltData.get("headHeight").toString())
				outerDiameter=Double.parseDouble(boltData.get("outerDiameter").toString())
				//CSG bolt =  Vitamins.get( "capScrew",boltSizeParam.getStrValue())
				CSG bolthead =new Cylinder(headDiameter/2+(printerOffset.getMM()/2),headDiameter/2+(printerOffset.getMM()/2),tailLength.getMM(),(int)10).toCSG() // a one line Cylinder
							.toZMin()
				CSG boltshaft =new Cylinder(outerDiameter/2-(printerOffset.getMM()/2),outerDiameter/2-(printerOffset.getMM()/2),boltLength.getMM(),(int)10).toCSG() // a one line Cylinder
							.toZMax()
				CSG bolt=bolthead.union(boltshaft)			
				double capHeight = headHeight
				int stepsOfBolt = 	(tailLength.getMM()/	capHeight	)+1
				//println "Servo cable len = "+	tailLength.getMM()+" cap height = "+ capHeight   +" steps = "+stepsOfBolt
				CSG boltAccum = bolt
				if(	holesUp){
					bolt=bolt.rotx(180)	
					capHeight=-capHeight		  
				}else{
					bolt=bolt.movez(flangeThickness)
				}

				CSG bolts = bolt.movey(holeDiameter/2)	
							.union( bolt.movey(-holeDiameter/2)	.movey(-	holeEdgetoHoleEdgeLongDistance)		)	
							.movey(shaftToHoleSide)	         
				if(servoConfig.get("holeEdgetoHoleEdgeShortDistance")!=null){
					double holeEdgetoHoleEdgeShortDistance=Double.parseDouble(servoConfig.get("holeEdgetoHoleEdgeShortDistance").toString())
					bolts=bolts.movex(holeDiameter/2)	
							.movex(holeEdgetoHoleEdgeShortDistance/2)
							.union(bolts.movex(-holeDiameter/2)	
									.movex(-holeEdgetoHoleEdgeShortDistance/2))
				}
				if(!noBolts){
					//println "Adding bolts"
					builtServo=builtServo
						.union(bolts)
	          			.setParameter(boltLength)
	          			.setParameter(boltSizeParam)
				}
			}
		}
			builtServo=	builtServo.movez(-flangeThickness)
          return builtServo
          	.setParameter(tailLength)
}
/*
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
 */

CSG getNut(){
	String type= "hobbyServo"
	StringParameter size = new StringParameter(	type+" Default",
										args.get(0),
										Vitamins.listVitaminSizes(type))
	//println "Database loaded "+database
	HashMap<String,Object> servoConfig = Vitamins.getConfiguration( type,size.getStrValue())

	//println "Loading " +size.getStrValue()
	return servoFactory(Double.parseDouble(servoConfig.get("servoThinDimentionThickness").toString()),//servoThinDimentionThickness
			Double.parseDouble(servoConfig.get("servoThickDimentionThickness").toString()),// servoThickDimentionThickness
			Double.parseDouble(servoConfig.get("servoShaftSideHeight").toString()), // servoShaftSideHeight
			Double.parseDouble(servoConfig.get("outputShaftDiameter").toString()),// outputShaftDiameter
               Double.parseDouble(servoConfig.get("shaftToShortSideDistance").toString()), //shaftToShortSideDistance
               Double.parseDouble(servoConfig.get("tipOfShaftToBottomOfFlange").toString()), // tipOfShaftToBottomOfFlange
               Double.parseDouble(servoConfig.get("flangeThickness").toString()),//flangeThickness
               Double.parseDouble(servoConfig.get("flangeLongDimention").toString()),// flangeLongDimention
               Double.parseDouble(servoConfig.get("bottomOfFlangeToTopOfBody").toString()),//bottomOfFlangeToTopOfBody
               servoConfig         )
		.setParameter(size)
		.setRegenerate({getNut()})
}

if(args==null){
	args=["hv5932mg"]
	
	Vitamins.setGitRepoDatabase("https://github.com/madhephaestus/Hardware-Dimensions.git")
	CSGDatabase.clear()
}
if(args.size()==1 ){
    return getNut()                  
	
}
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
                        args.get(6), // tipOfShaftToBottomOfFlange
                        args.get(7) ,//flangeThickness
                        args.get(8),// flangeLongDimention
                        args.get(9),//bottomOfFlangeToTopOfBody
                        null
          )     
