package exercise1.server;

import exercise1.IIterator;
import exercise1.IPassive;
import exercise1.Service;

import java.rmi.Remote;
import java.security.Provider;
import java.util.List;

/**
 * Contains the usual methods and Plus() which increments the value of all copies
 */
public class Iterator implements IIterator {

    private int value;

    private Service service;

    //constructeur
    public Iterator() {
        value = 0;
    }
    //setters
    @Override
    public void setService(Service _service){
        service = _service;
    }

    @Override
    public void setValue(int _value) {
        value = _value;
    }
    //iteration of the service copies 
    @Override
    public void plus(){
        //debug
    	System.out.println(this + " -> before : " + value);
        //increment value
        value++;
        //retrieve the copies
        List<Remote> copies = service.getCopies();
        //if there are any :
        if(!copies.isEmpty()){
            Iterator tmp;
            //foreach copy : set the new value
            for(int i = 0; i < copies.size(); i++){
                tmp = (Iterator) copies.get(i);
                tmp.setValue(value);
            }
        }
        System.out.println(this + " -> after : " + value);
    }



}
