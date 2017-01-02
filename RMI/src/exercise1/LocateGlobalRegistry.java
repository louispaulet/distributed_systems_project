package exercise1;

import sun.rmi.registry.RegistryImpl;
import sun.rmi.server.UnicastRef;
import sun.rmi.server.UnicastRef2;
import sun.rmi.server.Util;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;


public final class LocateGlobalRegistry {

    private static String GLOBAL_REGISTRY = "Global";

    public static Registry createRegistry(int port) throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(port);

        GlobalRegistry globalRegistry = new GlobalRegistry();
        Registry stub = (Registry) UnicastRemoteObject.exportObject(globalRegistry, 0);

        registry.bind(GLOBAL_REGISTRY, stub);

        return registry;
    }

    public static Registry getRegistry(int port)
            throws RemoteException, NotBoundException {
        return (Registry) LocateRegistry.getRegistry(port).lookup(GLOBAL_REGISTRY);
    }

}
