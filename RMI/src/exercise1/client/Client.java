package exercise1.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

import exercise1.GlobalRegistry;
import exercise1.IIterator;
import exercise1.LocateGlobalRegistry;
import exercise1.Sorter;
import exercise1.server.Iterator;


public class Client {

  //
  // CONSTANTS
  //
  private static String SERVICE_NAME = "Sorter";
  private static String SERVICE_HOST = "localhost";
  private static String SERVICE_ITERATOR = "Iterator";

  //
  // MAIN
  //
  public static void main(String[] args) throws Exception {

    // locate the registry that runs on the remote object's server
    //Registry registry = LocateRegistry.getRegistry(SERVICE_HOST);
    Registry globalRegistry = LocateGlobalRegistry.getRegistry(1098);
    System.out.println("client: retrieved registry");

    // retrieve the stub of the remote object by its name
    //Sorter sorter = (Sorter) registry.lookup(SERVICE_NAME);
    /*Sorter sorter = (Sorter) globalRegistry.lookup(SERVICE_NAME);
    System.out.println("client: retrieved Sorter stub");

    // call the remote object to perform sorts and reverse sorts
    List<String> list = Arrays.asList("3", "5", "1", "2", "4");
    System.out.println("client: sending " + list);

    list = sorter.sort(list);
    System.out.println("client: received " + list);

    list = Arrays.asList("mars", "saturne", "neptune", "jupiter");
    System.out.println("client: sending " + list);

    list = sorter.reverseSort(list);
    System.out.println("client: received " + list);*/

    IIterator iterator = (IIterator) globalRegistry.lookup(SERVICE_ITERATOR);
    System.out.println("client: retrieved Sorter stub");

    iterator.plus();
    System.out.println("client: increment value");


    // main terminates here
    System.out.println("client: exiting");

  }

}
