package exercise1;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Quentin on 28/12/2016.
 */
public interface IPassive extends Remote{

    Service service = null;

    public void setService(Service service) throws RemoteException;

}
