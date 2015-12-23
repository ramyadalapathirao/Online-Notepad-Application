package edu.sdsu.cs.cs645.server;

import edu.sdsu.cs.cs645.client.NotepadService;
import edu.sdsu.cs.cs645.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class NotepadServiceImpl extends RemoteServiceServlet implements
    NotepadService {
    
  public String load() throws IllegalArgumentException
  {
    String path = getServletContext().getRealPath("/");
    String fileName = path + "/data.txt";
    String answer = "";
    String line;
    try
    {
     BufferedReader br = new BufferedReader(new FileReader(fileName));   
     while((line = br.readLine()) != null) 
         answer += line;
     br.close();
    }
    catch(Exception e)
    {
     return "Failed to read file";   
    }
      return answer;
  }
    
  public String save(String contents) throws IllegalArgumentException
  {
   DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
   Date date = new Date();
   String key = dateFormat.format(date);
   String value = contents;
   String path = getServletContext().getRealPath("/");
   String fileName = path + "/data.txt";
   String answer = "";
   String line;
   try
   {
    PrintWriter out = new PrintWriter(new FileWriter(fileName));
    contents = contents.replace("\r\n|\n","<br />");
    HashMap<String,String> map = new HashMap<String,String>();
    map.put(key,contents);
    out.print(map);
    //out.print(contents);
    out.close();
       
   }
   catch(Exception e)
   {
       return "ERROR, failed to save";
   }
    return "Notes Saved";
  }
    
  public String validateLogin(String password) throws IllegalArgumentException
  {
      
      if(password.trim().equals("sp2015"))
          return "OK";
      return "INVALID";
  }
    
}
