import scala.util.matching.UnanchoredRegex

class QueryFormatter(var query:String) {

def formatter(): String = {
  query=query.trim.toUpperCase
  val selectIndex=query.indexOf("SELECT");
  val fromIndex=query.indexOf("FROM");
  var colsStr=query.substring(6,fromIndex);
  var colsFormatted=colsStr.split(",").map(x=>"\""+x.toLowerCase+"\"").mkString("\"_source\": [",",","]")
  println(colsFormatted)

  var whereIndex=query.indexOf("WHERE");
  if(whereIndex>=0){
    var afterWhereStr= query.substring(query.indexOf("WHERE")+5)
    var groupByIndex=afterWhereStr.indexOf("GROUP BY");
    if(groupByIndex>=0){
      afterWhereStr=afterWhereStr.substring(0,groupByIndex)
    }
    println(afterWhereStr)
    println(getMatches(afterWhereStr))
  }

  //println(afterWhere)
  colsStr
}


  def getMatches(whereConditionStr:String) :String={
    var andSepStrs=whereConditionStr.split("AND")
    var totalMatchStr=""
    var conditionsCount=andSepStrs.length;
    var presentCondCnt=0;
    andSepStrs.foreach{condition=>
      presentCondCnt=presentCondCnt+1;
      var consplit=condition.split("=")
      totalMatchStr+=" { \"match\": { \""+consplit(0)+"\": \""+consplit(1)+"\" } }"
      if(presentCondCnt<conditionsCount){
        totalMatchStr+=","
      }

    }
    return totalMatchStr
  }


}


object  main extends  App {
  var qf=new QueryFormatter("SELECT colorfamily,net-sales-var-ly,net-sales-var,sc,year,week,net-sales FROM max_week_rev_cfmly WHERE year = 2017 and week = 28  GROUP BY week,year ")
  qf.formatter()
}