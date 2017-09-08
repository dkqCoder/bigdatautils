package day0831

import scala.util.matching.Regex

object Test002 {

  def main(args: Array[String]): Unit = {
    println(isFlag("(0000)","(021)313308820 "))
  }



  val LANDINE = 1

  def isFlag( _type:String , targetStr:String ): Boolean ={
    //11        正常的11为           手机号码
    //(0000)    区号四位的带括号      座机
    //(000)     区号三位的带括号      座机
    //(000)11   区号三位带括号        手机号码
    //(0000)11  区号四位带括号        手机号码
    //00011     区号三位不带括号       手机号码
    //000011    区号四位不带括号       手机号码
    //000       三位的区号不带括号     座机
    //000       四位区号不带括号       座机

    //-1        判断全为数字

    _type match {
      case "11" =>{
        if( ( !(new Regex( "^1[3|4|5|6|7|8|9][0-9]\\d{8}$" ) findPrefixOf targetStr ).isEmpty ) ){
          true
        }else{
          false
        }
      }
      case "(0000)" =>{
        if( ( !(new Regex( s"(\\(0\\d{3}\\))[${LANDINE}-9].*" ) findPrefixOf targetStr ) .isEmpty ) && CodeDefinition.AREACODE_COMPARISON.contains( targetStr.substring(0,6) ) ){
          true
        }else{
          false
        }
      }
      case "(000)" =>{
        if( ( !(new Regex( s"(\\(0\\d{2}\\))[${LANDINE}-9].*" ) findPrefixOf targetStr ) .isEmpty ) && CodeDefinition.AREACODE_COMPARISON.contains( targetStr.substring(0,5) ) ){
          true
        }else{
          false
        }
      }
      case "(0000)11" =>{
        if( ( !(new Regex( "(\\(0\\d{3}\\))?(1[3|4|5|6|7|8|9][0-9])\\d{8}$" ) findPrefixOf targetStr ) .isEmpty ) && CodeDefinition.AREACODE_COMPARISON.contains( targetStr.substring(0,6) ) ){
          true
        }else{
          false
        }
      }
      case "(000)11" =>{
        if( ( !(new Regex( "(\\(0\\d{2}\\))?(1[3|4|5|6|7|8|9][0-9])\\d{8}$" ) findPrefixOf targetStr ) .isEmpty ) && CodeDefinition.AREACODE_COMPARISON.contains( targetStr.substring(0,5) ) ){
          true
        }else{
          false
        }
      }
      case "00011" => {
        if( ( !(new Regex( "(0\\d{2})?(1[3|4|5|6|7|8|9][0-9])\\d{8}$" ) findPrefixOf targetStr ) .isEmpty ) && CodeDefinition.AREACODE_COMPARISON.contains( targetStr.substring(0,3) ) ){
          true
        }else{
          false
        }
      }
      case "000011" => {
        if( ( !(new Regex( "(0\\d{3})?(1[3|4|5|6|7|8|9][0-9])\\d{8}$" ) findPrefixOf targetStr ) .isEmpty ) && CodeDefinition.AREACODE_COMPARISON.contains( targetStr.substring(0,4) ) ){
          true
        }else{
          false
        }
      }
      case "000" =>{
        if( ( !(new Regex( s"(0\\d{2})[${LANDINE}-9].*" ) findPrefixOf targetStr ).isEmpty ) && CodeDefinition.AREACODE_COMPARISON.contains( targetStr.substring(0,3) ) ){
          true
        }else{
          false
        }
      }
      case "0000" =>{
        if( ( !(new Regex( s"(0\\d{3})[${LANDINE}-9].*" ) findPrefixOf targetStr ) .isEmpty ) && CodeDefinition.AREACODE_COMPARISON.contains( targetStr.substring(0,4) ) ){
          true
        }else{
          false
        }
      }
      case "-1" =>{
        if( ( !( new Regex( "^\\d+$" ) findPrefixOf targetStr ).isEmpty ) ){
          true
        }else{
          false
        }
      }
    }
  }
}


