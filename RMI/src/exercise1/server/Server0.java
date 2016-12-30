package exercise1.server;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import exercise1.GlobalRegistry;
import exercise1.IIterator;
import exercise1.LocateGlobalRegistry;
import exercise1.Sorter;

/**
 * Server0 program.
 *
 * Note: After the main method exits, the JVM will still run. This is because
 * the skeleton implements a non-daemon listening thread, which waits for
 * incoming requests forever.
 *
 */
public class Server0 {

  //
  // CONSTANTS
  //
  private static final String SERVICE_TAG = "0";
  private static final String SERVICE_NAME = "Sorter:" + SERVICE_TAG;
  private static final String SERVICE_ITERATOR = "Iterator:" + SERVICE_TAG + ":Passive";

  //
  // MAIN
  //
  public static void main(String[] args) throws Exception {

    // check the name of the local machine (two methods)
    System.out.println("server: running on host " + InetAddress.getLocalHost());
    System.out.println("server: hostname property "
            + System.getProperty("java.rmi.server.hostname"));

    // instantiate the remote object
    Sorter sorter = new SimpleSorter();
    Iterator iterator = new Iterator();
    System.out.println("server: instanciated Objects");

    // create a skeleton and a stub for that remote object
    Sorter stub = (Sorter) UnicastRemoteObject.exportObject(sorter, 0);
    IIterator stubI = (IIterator) UnicastRemoteObject.exportObject(iterator, 0);
    System.out.println("server: generated skeleton and stub");

    // register the remote object's stub in the registry
    Registry globalRegistry = LocateGlobalRegistry.getRegistry(1098);
    //globalRegistry.rebind(SERVICE_NAME, stub);
    globalRegistry.rebind(SERVICE_ITERATOR, stubI);
    System.out.println("server: registered remote object's stub");

    // main terminates here, but the JVM still runs because of the skeleton
    System.out.println("server: ready");

  }

}
