package exercise1;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Quentin on 28/12/2016.
 */
public interface IIterator extends IPassive {

    public void setValue(int _value) throws RemoteException;

    public void plus() throws RemoteException;

}
