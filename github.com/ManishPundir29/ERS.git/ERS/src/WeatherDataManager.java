import java.util.Scanner;
import java.util.InputMismatchException;

public class WeatherDataManager
{
 public WeatherDataManager(){ //constructor
  city     = new String[N];
  highTemp = new int[N];
  lowTemp  = new int[N];
  humidity = new int[N];
  //city=this.initCities(city);
  /* highTemp = new int[N];
  lowTemp  = new int[N];
  humidity = new int[N];
 */ }
 public void displayMenu(){        //to display the menu bar and ask the user for the entering the option
  int option = 0;
  String etc="";
  while(true){    //works infinitely until user donot enter "7" to exit the system
   System.out.printf("\n\n%10s*************************************************************\n",etc);
   System.out.printf("%10s*%59s*\n",etc,etc);
   System.out.printf("%10s*-----------------!! WEATHER DATA MANAGER !!----------------*\n",etc);
   System.out.printf("%10s*%59s*\n",etc,etc);
   System.out.printf(
   "%10s*************************************************************\n",etc);
   System.out.printf("%10s*\t1. Input Data%41s*\n",etc,etc);
   System.out.printf("%10s*\t2. Display Data%39s*\n",etc,etc);
   System.out.printf("%10s*\t3. Sort by City%39s*\n",etc,etc);
   System.out.printf("%10s*\t4. Sort by low temperature%28s*\n",etc,etc);
   System.out.printf("%10s*\t5. Search%45s*\n",etc,etc);
   System.out.printf("%10s*\t6. Display extreme temperature%24s*\n",etc,etc);
   System.out.printf("%10s*\t7. Exit%47s*\n",etc,etc);
  
   System.out.printf("%10s*\tEnter Option 1-7 : ",etc);
   option = this.getIntValue("!! Enter Option ",1,7,10);
   switch(option){
    case 1:
     inputData();  //take input from the user
     break;
    case 2:
     displayData(this.city, this.lowTemp, this.highTemp, this.humidity,NORMAL);
     break;
    case 3:
     sortByCity(this.city);
     break;
    case 4:
     sortByLowTemp(this.lowTemp);
     break;
    case 5:
     search();
     break;
    case 6:
     displayExtremes();
     break;
    case 7:
     System.exit(0);
     break;
    default:
     System.out.println("Invalid Option Entered. Please Enter Correct Option.");
   }
  }
 }
 public void inputData(){        //to get the data from the user regarding the cities of Australia
  String etc="";
  String temp = "";
  for(int i=0;i<city.length;i++){
   System.out.printf("%5s***********************************************************************\n",etc);
   System.out.printf("%5s*%30sCITY %d%33s*\n",etc,etc,(i+1),etc);
   System.out.printf("%5s*%30s======%33s*",etc,etc,etc);
   System.out.printf("\n%5s*%5s- - Please Enter Information about City %d- - \n%5s*%5sCity Name : ",etc,etc,(i+1),etc,etc);
   city[i]="";
   while(city[i].equals("")){
    city[i] = s.nextLine();      //get the name of city
    if(city[i].equals("")){
     System.out.printf("%5s*%5sCity Name : ",etc,etc);
    }
   }
   System.out.printf("%5s*%5sPlease Enter Low Temperature of city %d range:-20 to 20!! : ",etc,etc,(i+1));
   //inputting low temperature
   lowTemp[i] = getIntValue("!! Please Enter Low Temperature ",-20,20,5);
   System.out.printf("%5s*%5sPlease Enter High Temperature of city %d range:%d to 60!! : ",etc,etc,(i+1),((lowTemp[i]>-1)?lowTemp[i]+1:0));
   //inputting high temperature
   highTemp[i]=getIntValue("!! Please Enter High Temperate ",((lowTemp[i]>-1)?lowTemp[i]+1:0),60,5);
   System.out.printf("%5s*%5sPlease Enter the Humidity of city %d range : 10%% to 100%% : ",etc,etc,(i+1));
   humidity[i]=getIntValue("!! Please Enter the Humidity ",10,100,5);
  }
  System.out.printf("%5s***********************************************************************\n",etc);
  check = true;
  System.out.printf("%5s*%20s%3d cities data entered%26s*\n",etc,etc,N,etc);
  System.out.printf("%5s***********************************************************************\n",etc);
  this.waitForKeyPress();
 }
 public void displayData(String[] city, int[] lowTemp, int[] highTemp, int[] humidity,int SORTACC){ //used to display the data sorted according to the parameter 'SORTACC'
               //'SORTACC' can be CITY, LOW_TEMP i.e will get sorted according city, lowTemp . Can also display data normal if the last
               //argument is 'NORMAL', city - array of cities, lowTemp - array of low temperature, highTemp - array of high Temperature
               //humidity - array of humidity,
  if(check==true){
   String etc="";
   System.out.printf("%10s*************************************************************\n",etc);
   System.out.printf("%10s*%59s*\n",etc,etc);
   System.out.printf("%10s*%11s!!AUSTRALIA CITY WEATHER CONDITION !!%11s*\n",etc,etc,etc);
   System.out.printf("%10s*%59s*\n",etc,etc);
   System.out.printf("%10s*************************************************************\n",etc);
   String cn = "City Name";
   String lht = "Low/High Temperature";
   String hmd = "Humidity";
   System.out.printf("%10s*%-21s%s%18s*\n",etc,cn,lht,hmd);
   System.out.printf("%10s*************************************************************\n",etc);
   int k=0;
   //for my logic when their are two or more equal elements
   //for keeping the check on which data row has been printed
   int[] a= new int[city.length];
   for(int i=0;i<a.length;i++){
    a[i]=0;
   }
   for(int i=0;i<city.length;i++){
    if(SORTACC==CITY){
     for(int j=0;j<city.length;j++){
      if(city[i].equals(this.city[j])){
       k=j;
       if(a[k]==0){
        a[k]=1;
        break;
       }
      }
     }
    }else if(SORTACC==LOW_TEMP){
     for(int j=0;j<city.length;j++){
      if(lowTemp[i]==this.lowTemp[j]){
       k=j;
       if(a[k]==0){
        a[k]=1;
        break;
       }
      }
     }
    }else if(SORTACC==NORMAL){
     
    }
    System.out.printf("%10s* %-20s%8d/%-14d%13d%% *\n",etc,this.city[k],this.lowTemp[k],this.highTemp[k],this.humidity[k]);
    k++;
   }
   System.out.printf("%10s*************************************************************\n",etc);
   System.out.printf("%10s*  Total " +N+" results !\n",etc);
  }else{
   this.showNoDataEnteredError();
  }
  this.waitForKeyPress();
 }
 public void sortByCity(String[] city){     //sort the cities in ascending order and diplay them
  if(check==true){
   String[] city_ = new String[N];
   int i=0;
   for(String s : city){
    city_[i]=s;
    i++;
   }
   sort(city_);
   this.displayData(city_,lowTemp,highTemp,humidity,this.CITY);
  }else{
   this.showNoDataEnteredError();
   this.waitForKeyPress();
  }
 }        
 public void sortByLowTemp(int[] lowTemp){    //sort the cities according to their low temperature and display them accordingly
  if(check==true){
   int[] lowTemp_ = new int[N];
   for(int i=0;i<lowTemp.length;i++){
    lowTemp_[i] = lowTemp[i];
   }
   sort(lowTemp_);
   this.displayData(city,lowTemp_,highTemp,humidity,LOW_TEMP);
  }else{
   this.showNoDataEnteredError();
   this.waitForKeyPress();
  }
 }      
 public void search(){         //let the user to search the city and display information about that city
  if(check==true){
   String etc="";
   String city_="";
   while(city_.equals("")){
    System.out.printf("%10s*%5sPlease Enter City Name : ",etc,etc);
    city_ = s.nextLine();      //get the name of city
   }
   this.showCityInfo(city_);
  }else{
   this.showNoDataEnteredError();
  }
  this.waitForKeyPress();
 }        
 public void displayExtremes(){       //it displays the least temperature city and maximum temperature city information
  if(check==true){
   String etc="";
   int lowInd;
   int highInd;
   lowInd=this.getLowestTempIndex();
   highInd=this.getHighestTempIndex();
   System.out.printf("%3s*************************************************************************\n",etc);
   System.out.printf("%3s*%71s*\n",etc,etc);
   System.out.printf("%3s*  The Minimum Low temperature city is :%13s,%d celcius degree *\n",etc,city[lowInd],lowTemp[lowInd]);
   System.out.printf("%3s*  The Mazimum hign temperature city is:%13s,%d celcius degree *\n",etc,city[highInd],highTemp[highInd]);
   System.out.printf("%3s*%71s*\n",etc,etc);
   System.out.printf("%3s*************************************************************************\n",etc);
  }else{
   this.showNoDataEnteredError();
  }
  this.waitForKeyPress();
 }
 //helper methods
 private void showNoDataEnteredError(){     //used to show error if no data entered
  String etc="";
  String error="!! NO DATA ENTERED !!";
  System.out.printf( "%10s*************************************************************\n",etc);
  System.out.printf("%10s*%59s*\n",etc,etc);
  System.out.printf("%10s*%39s%20s*\n",etc,error,etc);
  System.out.printf("%10s*%59s*\n",etc,etc);
  System.out.printf( "%10s*************************************************************\n",etc);
 }
 private int getLowestTempIndex(){      //return the index of city which have lowest temperature
  int low=40;
  int index=-1;
  for(int i=0;i<lowTemp.length;i++){
   if(low>lowTemp[i])
   {
    low=lowTemp[i];
    index=i;
   }
  }
  return index;
 }
 private int getHighestTempIndex(){      //return the index of city which have highest temperature
  int high=0;
  int index=-1;
  for(int i=0;i<highTemp.length;i++){
   if(high<highTemp[i])
   {
    high=highTemp[i];
    index=i;
   }
  }
  return index;
 }
 private int getIntValue(String error,int low, int high,int fs){ //used to get the integer input from the user,
               //also displays messages on wrong input and let user enter data in correct format fs - first space
               // error - a message, low - lowest range, high- highest range, fs - first space (space at the beginnig of message)
  int option; 
  while(true){
   String etc="";         //just used in displaying the data to set the format
   try{
    //get option value right over here
    String temp = "";
    while(temp.equals("")){  //works until user will not give some input other than newline (enter)
     temp = s.nextLine();     //will store the input in temp
     if(temp.equals("")){
      String n="%"+fs+"s*%5s%s %d to %d :";
      System.out.printf(n,etc,etc,error,low,high);
     }
    }
     //this scanner object is created to handle the extra information which user enters during option value, like 1 3 4 slk fjlksf j" only "1" will be accepted rest will be skipped
    s1 = new Scanner(temp);
    option = Integer.parseInt(s1.next());
    s1 = null;
    if(option<low||option>high){  //if option is out of range i.e. not (low - high) again ask for input with a message
     String n = "%"+fs+"s*%5s%s in range :%d to %d : ";
     System.out.printf(n,etc,etc,error,low,high);
     continue;
    }
    return option;
    //break; //if user enter correct input than this loop will break and control moves to swtich statement
   }catch(InputMismatchException e){
    String n = "%"+fs+"s*%5s%s in Number Format";
    System.out.printf(n,etc,etc,error);
    option=0;
   }catch(NumberFormatException e){
    String n = "%"+fs+"s*%5s%s in Integer/Number Format : ";
    System.out.printf(n,etc,etc,error);
    option=0;
   }
  }
 }
 private void showCityInfo(String city_){    //show the information about the city - city_, if stored in the data, otherwise error - city not found
  int i=-1;
  for(int j=0;j<this.city.length;j++){
   if(city_.equals(this.city[j])){
    i=j;
    String etc="";
    System.out.printf("%10s*************************************************************\n",etc);
    System.out.printf("%10s*%59s*\n",etc,etc);
    System.out.printf("%10s*%12s - LOW/HIGH Temperature: %3d/%-3d  Humidity: %3d*\n",etc,city[i],lowTemp[i], highTemp[i], humidity[i]);
    System.out.printf("%10s*%59s*\n",etc,etc);
    System.out.printf("%10s*************************************************************\n",etc);
   }
  }
  if(i==-1){
   String etc="";
   System.out.printf("%10s*************************************************************\n",etc);
   System.out.printf("%10s*%59s*\n",etc,etc);
   System.out.printf("%10s*%20sCITY NOT FOUND%25s*\n",etc,etc,etc);
   System.out.printf("%10s*%59s*\n",etc,etc);
   System.out.printf("%10s*************************************************************\n",etc);
  }
 }
 private void waitForKeyPress(){       //displays "press enter to continue" and let the user to press the enter key, (implemented to pause the result)
  String etc="";
  System.out.printf("%10s*%15sPress Enter to continue :%19s*",etc,etc,etc);
  s.nextLine();
 }
 private void sort(String[] s){       //sort the array of string in ascending order
  for(int i=0;i<s.length;i++){
   for(int j=0;j<s.length-1-i;j++){
    if(s[j].compareTo(s[j+1])>0){
     String temp = s[j];
     s[j]=s[j+1];
     s[j+1] = temp;
    }
   }
  }
 }
 private void sort(int[] s){        //sort the array of int in ascending order
  for(int i=0;i<s.length;i++){
   for(int j=0;j<s.length-1-i;j++){
    if(s[j]>s[j+1]){
     int temp = s[j];
     s[j]=s[j+1];
     s[j+1] = temp;
    }
   }
  }
 }
 //for testing
 private String[] initCities(String[] city){    //initialize the all the city names, their respective low and high temperatures and humidity
  city=new String[N];
  check=true;
  city[0]="Brisbane"; this.lowTemp[0]=15;this.highTemp[0]=21;this.humidity[0]=65;
  city[1]="Adelaide"; lowTemp[1]=17;highTemp[1]=22;humidity[1]=55;
  city[2]="Canberra";lowTemp[2]=11;highTemp[2]=17;humidity[2]=69;
  city[3]="Newcastle";lowTemp[3]=15;highTemp[3]=19;humidity[3]=69;
  city[4]="ROckhampton";lowTemp[4]=8;highTemp[4]=29;humidity[4]=59;
  city[5]="Melbourne";lowTemp[5]=15;highTemp[5]=17;humidity[5]=77;
  city[6]="Townsville";lowTemp[6]=20;highTemp[6]=39;humidity[6]=68;
  city[7]="Australian City";lowTemp[7]=19;highTemp[7]=20;humidity[7]=99;
  return city;
 }
 public static void main(String [ ] args){    //execution of program starts from here
  WeatherDataManager wdm = new WeatherDataManager(); //creating object of the Class 'WeatherDataManager'
  wdm.displayMenu();         //call to the method which displays the menu screen
 }
 private boolean check = false;    //used to check whether the data is entered or not, true means entered, false means not entered
 private final int N = 8;       //total no of cities
 public static Scanner s = new Scanner(System.in); //for getting input from the keyboard
 public static Scanner s1;       //for handling input entered from the keyboard
 private String[] city;      //for the city names
 private int[] lowTemp;      //for the low temperature -20 to 20
 private int[] highTemp;      //for high temperature  0 to 60
 private int[] humidity;      //for the humidity (10% - 100%)
 //used as option for sorting accordingly
 public final int CITY = 0;     //used
 public final int LOW_TEMP=1;    //used
 public final int HIGH_TEMP=2;    //not used
 public final int HUMIDITY=3;    //not used
 public final int NORMAL=4;     //used
}