package edu.sdsu.cs.cs645.client;

import edu.sdsu.cs.cs645.shared.FieldVerifier;
import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Notepad implements EntryPoint 
{

  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  private final NotepadServiceAsync notepadService = GWT.create(NotepadService.class);
  private RichTextArea textPad;
  private HTML status;
  private HTML loginStatus;
  public void onModuleLoad() {
    
    status = new HTML();
    loginStatus = new HTML();
    buildLoginPanel();
    //buildMainPanel();
  }

  private void buildLoginPanel()
  {
   RootPanel.get().clear();
   FlowPanel loginPanel = new FlowPanel();
   FlowPanel headerPanel = getHeader();
   loginPanel.getElement().setId("login_panel");
   final PasswordTextBox password = new PasswordTextBox();
   loginPanel.add(headerPanel);
   FlowPanel container = new FlowPanel();
   container.setStyleName("container");
   FlowPanel passwordPanel = new FlowPanel();
   passwordPanel.setStyleName("password_panel");
   passwordPanel.add(new HTML("<h1>Please enter your password</h1>"));
   //passwordPanel.add(new Label("Password"));
   passwordPanel.add(password);
   FlowPanel buttonPanel = new FlowPanel();
   buttonPanel.setStyleName("login_button_panel");
   Button loginButton = new Button("Login");
   Button clearButton = new Button("Clear");
   loginButton.setStyleName("login_button_style");
   clearButton.setStyleName("clear_button_style");
   clearButton.addClickHandler(new ClickHandler(){
       public void onClick(ClickEvent e)
       {
        password.setText("");
        loginStatus.setText("");
       }
   });
    loginButton.addClickHandler(new ClickHandler(){
       public void onClick(ClickEvent e)
       {
        validateLogin(password.getText());
       }
   });
    buttonPanel.add(clearButton);
    buttonPanel.add(loginButton);
    passwordPanel.add(buttonPanel);
    container.add(passwordPanel);
    loginPanel.add(container);
    loginStatus.setStyleName("login_error");
    loginPanel.add(loginStatus);
    RootPanel.get().setStyleName("root_panel_style");
    RootPanel.get().add(loginPanel);
    password.setFocus(true);
   }
    
  private FlowPanel getHeader()
  {
   FlowPanel header = new FlowPanel();
   header.setStyleName("header_style");
   String url = GWT.getModuleBaseURL() + "notepad_icon.png";
   Image icon = new Image(url);
   icon.setStyleName("icon_style");
   header.add(icon);
   header.add(new HTML("<h1>ONLINE NOTEPAD</h1>"));
   header.add(new HTML("<h3>Notes on the Go..</h3>"));
   return header;
  }
    
  private void validateLogin(String password)
  {
     if(password.trim().equals(""))
     {
      loginStatus.setText("Please enter your Password");   
     }
     else
     {
     AsyncCallback callback = new AsyncCallback()
     {
       public void onSuccess(Object results)
       {       
        String answer = (String)results;  
        if(answer.equals("OK"))
        {
         loginStatus.setText("");  
         buildMainPanel();
        }
        else
        {
         loginStatus.setText("Invalid Login");   
        }
       }
       public void onFailure(Throwable err)
       {
           loginStatus.setText("Failed "+err.getMessage());
       }
          
      };
      notepadService.validateLogin(password,callback); 
     }
 }
  private void buildMainPanel()
  {
   RootPanel.get().clear();
   FlowPanel main = new FlowPanel();
   main.add(getHeader());
   textPad = new RichTextArea();
  
   FlowPanel logoutPanel = new FlowPanel();
   logoutPanel.setStyleName("logout_style");
   Anchor anchor = new Anchor("Logout");
   anchor.addClickHandler(new ClickHandler() {
     @Override
     public void onClick(ClickEvent event) {
           buildLoginPanel();
     }

});
   logoutPanel.add(anchor);
   main.add(logoutPanel);
   main.add(getToolBar());  
   main.add(getButtonPanel());  
   main.add(textPad);
   status.setStyleName("status_style");
   main.add(status);
   RootPanel.get().add(main);
   loadPanel();
      
  }
private FlowPanel getToolBar()
{
  FlowPanel toolBarPanel = new FlowPanel();
  toolBarPanel.setStyleName("toolbar_style");
  RichTextToolbar toolBar = new RichTextToolbar(textPad);
  toolBarPanel.add(toolBar);
  return toolBarPanel;
}
  private FlowPanel getButtonPanel()
  {
   FlowPanel panel = new FlowPanel();
   Button clear = new Button("Clear");
   Button save = new Button("Save");
   Button load = new Button("Load");
   clear.setStyleName("button_style");
   save.setStyleName("button_style");
   load.setStyleName("button_style");
   clear.addClickHandler(new ClickHandler(){
       public void onClick(ClickEvent e)
       {
        textPad.setText("");
        status.setText("Content Cleared");
       }
   });
   save.addClickHandler(new ClickHandler(){
       public void onClick(ClickEvent e)
       {
        savePanel();
       }
   });
   load.addClickHandler(new ClickHandler(){
       public void onClick(ClickEvent e)
       {
        loadPanel();
       }
   });
   
   panel.setStyleName("button_panel_style");
   panel.add(clear);
   panel.add(save);
   panel.add(load);
   return panel;
      
  }
  private void savePanel()
  {
      AsyncCallback callback = new AsyncCallback(){
       public void onSuccess(Object results)
       {
        String contents = textPad.getHTML();
        status.setText((String)results);   
       }
       public void onFailure(Throwable err)
       {
           
       }
          
      };
      notepadService.save(textPad.getHTML(),callback);
      
  }
  private void loadPanel()
  {
     AsyncCallback callback = new AsyncCallback(){
       public void onSuccess(Object results)
       {
        String response = (String)results;
        String newString = response.substring(1, response.length() - 1);
        int index = newString.indexOf("=");
        if(index >=1)
        {
         String key = newString.substring(0,index);
         String value = newString.substring(index+1,(newString.length()-1));
         //String[] keyAndValue = newString.split("=");
         //String key = keyAndValue[0];
         //String value = keyAndValue[1];
         if(value == null)
         {
            value="";
         }
         textPad.setHTML(value);
         status.setText("Last saved at "+key);
        }
       }
       public void onFailure(Throwable err)
       {
           
       }
          
      };
      notepadService.load(callback); 
  }
}
