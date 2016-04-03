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
			6,// y dimention
			200//  Z dimention
			)
			.noCenter()
			.toCSG()
			.rotz(-90)
			.movex(3)
			
			.movey(shaftToShortSideDistance-3)
			.movez(-200)
			
          return shaft.union(flange,body,cord)
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
if(args==null)
	args=["standardMicro"]
if(args.size()==1 ){
	// we are using the default vitamins configuration
	//https://github.com/madhephaestus/Hardware-Dimensions.git
	//Create the type, this tells GSON what datatypes to instantiate when parsing and saving the json
	Type TT_mapStringString = new TypeToken<HashMap<String,HashMap<String,Object>>>(){}.getType();
	//chreat the gson object, this is the parsing factory
	Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	// create some variables, including our database
	HashMap<String,HashMap<String,Object>> database=new HashMap<String,HashMap<String,Object>>();
	String jsonString;
	InputStream inPut = null;

	// attempt to load the JSON file from the GIt Repo and pars the JSON string
	File f=ScriptingEngine
							.fileFromGit(
								"https://github.com/madhephaestus/Hardware-Dimensions.git",// git repo, change this if you fork this demo
								"json/hobbyServo.json"// File from within the Git repo
							)
	//println "Loading file "+f	+" "+f.exists()					
	inPut = FileUtils.openInputStream(f);
				
	jsonString= IOUtils.toString(inPut);
	//println "Contains: "+jsonString
	// perfoem the GSON parse
	database=gson.fromJson(jsonString, TT_mapStringString);

	//println "Database loaded "+database
	HashMap<String,Object> servoConfig = database.get(args.get(0))

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
