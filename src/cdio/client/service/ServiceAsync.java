package cdio.client.service;

import java.sql.ResultSet;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cdio.shared.RaavareDTO;
import cdio.shared.UserDTO;



public interface ServiceAsync {

	
	void getPersons(AsyncCallback<List<UserDTO>> callback);
	void getRaavare(int id, AsyncCallback<RaavareDTO> callback);
	void number(String s, AsyncCallback<String> callback);
	void checkLogin(int id, String password, AsyncCallback<String> callback);
}
