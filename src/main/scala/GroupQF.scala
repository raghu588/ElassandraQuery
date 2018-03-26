

import com.sun.prism.PixelFormat.DataType
import org.json._

import scala.collection.immutable.Range.Int
class GroupQF(groupCols:Map[String,String], selCols:Map[String,String]){

 var obj=new JSONObject();
 obj.put("size","0")
 var i=0;
 var latestObj=new JSONObject()
  groupCols foreach {case (col, coltype) =>
    i=i+1
   var termsObj=new JSONObject()
    if(coltype.equals("String")) {
      termsObj.put("field", col + ".keyword")
    }else{
      termsObj.put("field", col)
    }
   var grpByObj=new JSONObject()
   grpByObj.put("terms",termsObj)
   var aggsObj=new JSONObject()
   aggsObj.put("group_by_"+col,grpByObj)
   if(i==1){
    //println("i="+i+"   "+obj)
    obj.put("aggs",aggsObj)
   }else{
    latestObj.put("aggs",aggsObj)
   }
   latestObj=aggsObj;

  }

 var ovrallGrpObj=new JSONObject()
 selCols foreach {case (key, value) =>
   var fieldObj=new JSONObject()
   fieldObj.put("field",value)
  var grpObj=new JSONObject()
  grpObj.put(key,fieldObj)

  ovrallGrpObj.put(key+"_"+value,grpObj)

  }
 latestObj.put("aggs",ovrallGrpObj)

 println(obj)
}



object test extends App {

 var qf=new GroupQF(Map("state"->"String","Dist"->"Int"),Map("avg"->"state","sum"->"Dist"))
}