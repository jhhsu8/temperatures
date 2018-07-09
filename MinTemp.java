/**
 * This Java program finds the coldest day of the year and other interesting facts about the temperature and humidity in a day.
 * 
 * @author Joanne Hsu 
 * @version 1.0
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class MinTemp {
    //get coldest temperature in file
    public CSVRecord coldestTempInFile(CSVParser parser) {
        CSVRecord smallestSoFar = null;
        for(CSVRecord currentRow : parser) {
            smallestSoFar = getSmallestofTwo(currentRow,smallestSoFar);
        }
        return smallestSoFar;
    }
    //print coldest temperature and its hour in file
    public void testColdestHourInFile() {
        FileResource fr = new FileResource("nc_weather/2015/weather-2015-01-02.csv");
        CSVRecord smallest = coldestTempInFile(fr.getCSVParser());
        System.out.println("coldest temperature was " + smallest.get("TemperatureF") + " at " + smallest.get("TimeEST"));
    }
    
    //get file with coldest temperature
    public void fileWithColdestTemperature() {
        DirectoryResource dr = new DirectoryResource();
        File coldestFileSoFar = null;
        CSVRecord coldestRecordSoFar = null;
        double coldestTempSoFar = 0.0d;
        
        for (File file : dr.selectedFiles()) {
            FileResource fr = new FileResource(file);
            CSVParser parser = fr.getCSVParser();
            if (coldestRecordSoFar == null) {
               coldestFileSoFar = file;
               coldestRecordSoFar = coldestTempInFile(parser);
               coldestTempSoFar = Double.parseDouble(coldestRecordSoFar.get("TemperatureF"));
            }
           else {
               CSVRecord coldestCurrent = coldestTempInFile(parser);
               double currentTemp = Double.parseDouble(coldestCurrent.get("TemperatureF"));
               coldestTempSoFar = Double.parseDouble(coldestRecordSoFar.get("TemperatureF"));
               
               if (currentTemp < coldestTempSoFar){
                   coldestFileSoFar = file;
                   coldestRecordSoFar = coldestCurrent;
                }
            }
        }
        System.out.println("Coldest file: " + coldestFileSoFar.getName());
        System.out.println("Coldest temperature in file: " + coldestRecordSoFar.get("TemperatureF"));
        // Print temperatures in file
        System.out.println("Temperatures in file:");
        FileResource fr = new FileResource(coldestFileSoFar);
        CSVParser parser = fr.getCSVParser();
        for (CSVRecord record : parser) {
            System.out.println(record.get("DateUTC")+": "+record.get("TemperatureF"));
        }
    }
    
    //get lowest humidity in file
    public CSVRecord lowestHumidityInFile(CSVParser parser) {
        CSVRecord lowestSoFar = null;
        for(CSVRecord currentRow : parser) {
            
            if (currentRow.get("Humidity").equals("N/A")) {
             continue;   
            }
            
            if(lowestSoFar == null){
             lowestSoFar = currentRow;   
            }
            else {
             double currentHumidity = Double.parseDouble(currentRow.get("Humidity"));
             double lowestHumidity = Double.parseDouble(lowestSoFar.get("Humidity"));
             if (currentHumidity < lowestHumidity) {
                 lowestSoFar = currentRow;
                }
            }
             
        }
        return lowestSoFar;
    }
    
    //print lowest humidity and its hour in file
    public void testlowestHumidityInFile() {
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-01-20.csv");
        CSVRecord lowest = lowestHumidityInFile(fr.getCSVParser());
        System.out.println("Lowest humidity was " + lowest.get("Humidity") + " at " + lowest.get("DateUTC"));
    }
    
    //get lowest humidity among files
    public CSVRecord lowestHumidityInManyFiles(){
        CSVRecord lowestSoFar = null;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()) {
           FileResource fr = new FileResource(f);
           CSVRecord currentRow = lowestHumidityInFile(fr.getCSVParser());
           if(lowestSoFar == null){
             lowestSoFar = currentRow;   
            }
            else {
             double currentHumidity = Double.parseDouble(currentRow.get("Humidity"));
             double lowestHumidity = Double.parseDouble(lowestSoFar.get("Humidity"));
             
             if (currentHumidity < lowestHumidity) {
                 lowestSoFar = currentRow;
                }
            }
        }
        return lowestSoFar;
    }
    
    //print lowest humidity among files
    public void testlowestHumidityInManyFiles() {
     CSVRecord lowest = lowestHumidityInManyFiles();
     System.out.println("Lowest humidity was " + lowest.get("Humidity") + " at " + lowest.get("DateUTC"));
    }
    
    //get average temperature in file
    public double averageTemperatureInFile(CSVParser parser){
        double temp = 0;
        int count = 0;
        for(CSVRecord currentRow : parser) {
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            temp += currentTemp;
            count++;
        }
        double averageTemp = temp/count;
        return averageTemp;
    }
    
    //print average temperature in file
    public void testaverageTemperatureInFile() {
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-01-20.csv");
        double average = averageTemperatureInFile(fr.getCSVParser());
        System.out.println(average);
        
    }
    
    // get average temperature with a humidity value or higher than that value
    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value){
        double temp = 0;
        int count = 0;
        for(CSVRecord currentRow : parser) {
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            double currentHumidity = Double.parseDouble(currentRow.get("Humidity"));
            if(value <= currentHumidity) {
                temp += currentTemp;
                count++;
            }  
        }
        double averageTemp = temp/count;
        if(Double.isNaN(averageTemp)) {
            return -1;
        }
        else {
            return averageTemp;
        }
    }
    
    // print average temperature with a humidity value or higher than that value
    public void testaverageTemperatureWithHighHumidityInFile() {
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-01-20.csv");
        double average = averageTemperatureWithHighHumidityInFile(fr.getCSVParser(),50);
        if (average == -1.0) {
            System.out.println("No temperature with that humidity");
        }
        else {
            System.out.println(average);
        }
    }
    
    // get coldest temperature among files
    public CSVRecord coldestInManyDays() {
        CSVRecord smallestSoFar = null;
        DirectoryResource dr = new DirectoryResource();
        
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = coldestTempInFile(fr.getCSVParser());
            smallestSoFar = getSmallestofTwo(currentRow,smallestSoFar);   
        }
        return smallestSoFar;
    }
    
    // print coldest temperature among files
    public void testColdestInManyDays() {
     CSVRecord smallest = coldestInManyDays();
     System.out.println("coldest temperature was " + smallest.get("TemperatureF") + " at " + smallest.get("DateUTC"));
    }
    
    // get coldest temperature
    public CSVRecord getSmallestofTwo(CSVRecord currentRow, CSVRecord smallestSoFar) {
        
        if (smallestSoFar == null) {
            smallestSoFar = currentRow;
            } else {
                double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
                double largestTemp = Double.parseDouble(smallestSoFar.get("TemperatureF"));
                if (currentTemp < largestTemp) {
                    smallestSoFar = currentRow;
                }
            }
            return smallestSoFar;
        }   
    }