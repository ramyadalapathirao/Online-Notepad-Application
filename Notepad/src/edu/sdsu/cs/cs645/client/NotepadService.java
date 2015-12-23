package edu.sdsu.cs.cs645.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("notepad")
public interface NotepadService extends RemoteService {
  String save(String s) throws IllegalArgumentException;
  String load() throws IllegalArgumentException;
  String validateLogin(String password) throws IllegalArgumentException;
}
