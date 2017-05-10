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
import eu.mihosoft.vrl.v3d.parametrics.*;
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
		LengthParameter tailLength		= new LengthParameter("Cable Cut Out Length",30,[500,0.01])
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
		flange=flange.union(flangeBrace)
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
			tailLength.getMM()//  Z dimention
			)
			.noCenter()
			.toCSG()
			.rotz(-90)
			.movex(3)
			
			.movey(shaftToShortSideDistance-3)
			.movez(-tailLength.getMM())
			
          return shaft.union(flange,body,cord)
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

	return servoFactory(Double.parseDouble(servoConfig.get("servoThinDimentionThickness").toString()),//servoThinDimentionThickness
			Double.parseDouble(servoConfig.get("servoThickDimentionThickness").toString()),// servoThickDimentionThickness
			Double.parseDouble(servoConfig.get("servoShaftSideHeight").toString()), // servoShaftSideHeight
			Double.parseDouble(servoConfig.get("outputShaftDiameter").toString()),// outputShaftDiameter
               Double.parseDouble(servoConfig.get("shaftToShortSideDistance").toString()), //shaftToShortSideDistance
               Double.parseDouble(servoConfig.get("shaftToShortSideFlandgeEdge").toString()),// shaftToShortSideFlandgeEdge
               Double.parseDouble(servoConfig.get("tipOfShaftToBottomOfFlange").toString()), // tipOfShaftToBottomOfFlange
               Double.parseDouble(servoConfig.get("flangeThickness").toString()),//flangeThickness
               Double.parseDouble(servoConfig.get("flangeLongDimention").toString()),// flangeLongDimention
               Double.parseDouble(servoConfig.get("bottomOfFlangeToTopOfBody").toString())//bottomOfFlangeToTopOfBody
                        )
		.setParameter(size)
		.setRegenerate({getNut()})
}

if(args==null)
	args=["standardMicro"]
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
                       args.get(5),// shaftToShortSideFlandgeEdge
                        args.get(6), // tipOfShaftToBottomOfFlange
                        args.get(7) ,//flangeThickness
                        args.get(8),// flangeLongDimention
                        args.get(9)//bottomOfFlangeToTopOfBody
          )     
