package Test;


import java.util.*;
import java.io.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONException;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalTime;
@SuppressWarnings("serial")
class InvalidKey extends Exception {

}
@SuppressWarnings({ "serial", "unused" })
class DuplicateKey extends Exception {

}
@SuppressWarnings("serial")
class ValueSizeExceeded extends Exception{
	
}

@SuppressWarnings("serial")
class KeySizeExceeded extends Exception{
	
}
@SuppressWarnings("serial")
class TimeExceeded extends Exception {

}



public class DataFile {
	private static Instrumentation instrument;
   private final String DataPath; // Making DataPath immutable
   
    DataFile(String pathway) throws JSONException {
        DataPath = pathway;
        JSONObject values = new JSONObject();
        values.put(" ", " ");      
         try (FileWriter fw = new FileWriter(DataPath,false))
        		{
        	      fw.write(values.toString());
        	      fw.close();
        		}
         catch (IOException E)
         {
        	System.out.println("IOException have been Caught"); 
         }
        
        
    }
    
    DataFile() throws JSONException {
        DataPath = "C://Users//Yogesh.JSON";
        JSONObject values = new JSONObject();
        values.put(" ", " ");     
         try (FileWriter fw = new FileWriter(DataPath,false))
        		{
        	      fw.write(values.toString());   
        	      fw.close();
        		}
         catch (IOException E)
         {
        	System.out.println("IOException have been Caught"); 
         }
        
    }
    
    //CREATE method when LifeTime value is provided
    public void CreateEntrySet(String KeyValue, JSONObject DataValue, int LifeTime) throws Exception 
    {    
         try{ if((instrument.getObjectSize((Object)DataValue)/1024)>16) // Checks whether size of JSONObject is more than 16 KB
        	 throw new ValueSizeExceeded();
         else if(KeyValue.length()>32)
        	 throw new KeySizeExceeded();
         }
         catch (ValueSizeExceeded e) {
          	System.out.println("Value size exceeds maximum limit");
          } catch (KeySizeExceeded e) {
          	System.out.println("Enter a Valid Key");
          }
        
         try (FileReader read = new FileReader(DataPath)) {
            
        	JSONTokener tokenValue = new JSONTokener(read);
             JSONObject tempValue = new JSONObject(tokenValue);
            if (tempValue.has(KeyValue)) //Validating key value pair
                throw new DuplicateKey();
            JSONArray tArray = new JSONArray();
            tArray.put(LifeTime);     // Adding  JSONOBject provided by user as first element
            tArray.put(DataValue);  // Adding LifeTime value provided by user as second element

            LocalTime t = LocalTime.now();
            int TimeValue = t.toSecondOfDay();
            tArray.put(TimeValue);  // Adding time of creation of Key as third element
            tempValue.put(KeyValue, tArray);
            try (FileWriter fw = new FileWriter(DataPath,false)) 
            {

                fw.write(tempValue.toString());
                fw.close();

            } catch (IOException e) {
                System.out.println("IO Exception have been Caught");
            }

        } catch (DuplicateKey e) {
            System.out.println("Duplicate keys have been entered");
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            System.out.println("IO Exception have been Caught");
        } 
    }
    
    // CREATE method using only Key and Data value pair
    public void CreateEntrySet(String KeyValue, JSONObject DataValue) throws Exception 
    {
    	try {
    	 if((instrument.getObjectSize((Object)DataValue)/1024)>16) // Checks whether the size of JSONObject is more than 16 KB
       	 throw new ValueSizeExceeded();
    	 else if(KeyValue.length()>32)
    		 throw new KeySizeExceeded();
        }
        catch (ValueSizeExceeded e) {
         	System.out.println("Value size exceeds maximum limit");
         } catch (KeySizeExceeded e) {
         	System.out.println("Enter a Valid Key");
         }
        try (FileReader read = new FileReader(DataPath)) {
            
            
        	JSONTokener tokenValue = new JSONTokener(read);
            JSONObject tempValue = new JSONObject(tokenValue);
            if (tempValue.has(KeyValue)) //Validating  key value pair
                throw new DuplicateKey();
            JSONArray tArray = new JSONArray();
            tArray.put(DataValue); 
            tArray.put(Integer.MAX_VALUE); 

            LocalTime t = LocalTime.now();
            int TimeValue = t.toSecondOfDay();
            tArray.put(TimeValue); 
            tempValue.put(KeyValue, tArray);
            try (FileWriter fw = new FileWriter(DataPath,false)) //Values are Updated to the file
            {
                fw.write(tempValue.toString());
                fw.close();
            } catch (IOException e) {
                System.out.println("IO Exception have been Caught");
            }

        } catch (DuplicateKey e) {
            System.out.println("Duplicate keys have been entered");
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            System.out.println("IO Exception have been Caught");
        }
    }
    
    // READ JSON Oject
    public JSONObject ReadEntrySet(String KeyValue) throws Exception
    {
        
        try (FileReader read = new FileReader(DataPath)) {
           
            
        	JSONTokener tokenValue = new JSONTokener(read);
            JSONObject tempValue = new JSONObject(tokenValue);
            if (tempValue.has(KeyValue)) //Validating key value pair
            {
                JSONArray tArray = new JSONArray();
                tArray = tempValue.getJSONArray(KeyValue);
                LocalTime t = LocalTime.now();
                int PresentTime = t.toSecondOfDay();
                if ((PresentTime - tArray.getInt(2)) < tArray.getInt(1)) //Life time of Key is checked
                    return tArray.getJSONObject(0);
                else
                    throw new TimeExceeded();

            } else
                throw new InvalidKey();

        } catch (TimeExceeded e) {
            System.out.println("Key Exceeded it's Life Time");
        } catch (InvalidKey e) {
            System.out.println("Invalid Key");
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            System.out.println("IO Exception have been Caught");
        }
		return null;
    }
    // Delete JSONObject for Key
    public void DeleteEntrySet(String KeyValue) throws Exception // Method for deleting a given < Key,JSONOBject > pair
    {
       
        try (FileReader read = new FileReader(DataPath))
        {
        	JSONTokener tokenValue= new JSONTokener(read);
            JSONObject tempValue = new JSONObject(tokenValue);
            if (tempValue.has(KeyValue)) //Validating Key value pair
            {
                JSONArray tArray = new JSONArray();
                tArray = tempValue.getJSONArray(KeyValue);
                LocalTime t = LocalTime.now();
                int PresentTime = t.toSecondOfDay();
                if ((PresentTime - tArray.getInt(2)) < tArray.getInt(1)) //Checks the condition of life time of key and removes < Key,Value > pair
                    tempValue.remove(KeyValue);
                else
                    throw new TimeExceeded();

                try (FileWriter fw = new FileWriter(DataPath,false)) // Edited JSONObject is written back to the file
                {

                    fw.write(tempValue.toString());
                    fw.close();
                }
            }
                else
                    throw new InvalidKey();

            } 
            catch (InvalidKey e) {
                System.out.println("Invalid Key");
            } catch (IOException e) {
                System.out.println("IO Exception have been Caught");
            }

         catch (TimeExceeded e) {
            System.out.println("Key Exceeded it's Life Time");
        }
    }
   
}
