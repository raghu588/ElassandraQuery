import org.json._

import scala.util.matching.UnanchoredRegex

class QueryFormatter(var query:String) {

def formatter(): String = {
  var outputObj=new JSONObject()
  query=query.trim.toUpperCase
  val selectIndex=query.indexOf("SELECT");
  val fromIndex=query.indexOf("FROM");
  var colsStr=query.substring(6,fromIndex);
  var colsArr=new JSONArray()
  colsStr.split(",").foreach{col=>
    colsArr.put(col.toLowerCase())
  }

  outputObj.put("_source",colsArr)

 // var colsFormatted=colsStr.split(",").mcap(x=>"\""+x.toLowerCase+"\"").mkString("\"_source\": [",",","]")
  //println(colsArr)

  var whereIndex=query.indexOf("WHERE");
  var limitIndex=query.indexOf("LIMIT");
  if(whereIndex>=0){
    var afterWhereStr="";
    if(limitIndex>=0){
      afterWhereStr= query.substring(query.indexOf("WHERE")+5,limitIndex)
    }else{
      afterWhereStr= query.substring(query.indexOf("WHERE")+5)
    }


    //println(afterWhereStr)
   // println(getMatches(afterWhereStr))
    outputObj.put("query",getMatches(afterWhereStr))
  }
  println(outputObj)
  //println(afterWhere)
  outputObj.toString()
}


  def getMatches(whereConditionStr:String) :JSONObject={
    var andSepStrs=whereConditionStr.split("AND")
    var totalMatchStr=""
    var conditionsCount=andSepStrs.length;
    var presentCondCnt=0;

    var whereJSON=new JSONObject()
    var shouldJSONArray=new JSONArray()
    andSepStrs.foreach{condition=>
      presentCondCnt=presentCondCnt+1;
      var consplit=condition.split("=")
      totalMatchStr+=" { \"match\": { \""+consplit(0)+"\": \""+consplit(1)+"\" } }"
      if(presentCondCnt<conditionsCount){
        totalMatchStr+=","
      }

      var matchJSON=new JSONObject()
      var condJSON=new JSONObject()
      condJSON.put(consplit(0),consplit(1))
      matchJSON.put("match",condJSON)
      shouldJSONArray.put(matchJSON)
    }
    whereJSON.put("should",shouldJSONArray)
    println("whereJSON=="+whereJSON)
    var boolObj=new JSONObject()
    boolObj.put("bool",whereJSON)
    return boolObj
  }


}


object  main extends  App {
  var qf=new QueryFormatter("SELECT colorfamily,net-sales-var-ly,net-sales-var,sc,year,week,net-sales FROM max_week_rev_cfmly WHERE year = 2017 and week = 28 LIMIT 5")
  qf.formatter()
 // var qf1=new QueryFormatter("GroupBY(State),List(Avg(),Min(),MAx() ")

}