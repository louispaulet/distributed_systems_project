package exercise1.server;

import exercise1.IIterator;
import exercise1.IPassive;
import exercise1.Service;

import java.rmi.Remote;
import java.security.Provider;
import java.util.List;

/**
 * Created by Quentin on 27/12/2016.
 */
public class Iterator implements IIterator {

    private int value;

    private Service service;

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

    @Override
    public void plus(){
        System.out.println(this + " -> before : " + value);
        value++;
        List<Remote> copies = service.getCopies();
        if(!copies.isEmpty()){
            Iterator tmp;
            for(int i = 0; i < copies.size(); i++){
                tmp = (Iterator) copies.get(i);
                tmp.setValue(value);
            }
        }
        System.out.println(this + " -> after : " + value);
    }



}
