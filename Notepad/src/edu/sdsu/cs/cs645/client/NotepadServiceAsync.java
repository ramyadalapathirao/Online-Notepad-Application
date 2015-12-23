package edu.sdsu.cs.cs645.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface NotepadServiceAsync {
  void load(AsyncCallback<String> callback) throws IllegalArgumentException;
  void save(String contents, AsyncCallback<String> callback) throws IllegalArgumentException;
  void validateLogin(String password,AsyncCallback<String> callback) throws IllegalArgumentException;
}
