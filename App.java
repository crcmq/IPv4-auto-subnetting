import java.util.*;
/**
 * This program is to subnet an IPv4 address automatically
 * This is only a prototype. No error handling. All the input must follow the format. 
 * 
 * 
 */
public class App {
    public static void main(String[] args) throws Exception {
        App a = new App();
        Subnet s = new Subnet();
        Scanner in = new Scanner (System.in);

        // read and store IP 
        System.out.print ("Enter the IPv4 address of a network or host: ");
        String IP = in.nextLine();
        int[] parentIP = s.stringToArr(IP);

        // get mask for the IP
        System.out.print ("Enter the prefix or mask: ");
        String input = in.nextLine();
        int[] mask = new int[4];
        
        // if it is a mask address
        if (input.length() > 2) {
            mask = s.stringToArr(input);
        }
        // else, we need to convert it to mask address
        else {
            int preFix = Integer.parseInt(input);
            mask = s.prefixToMask (preFix);
        }
        
        // calculate network address
        int[] networkAddress = a.getNetwork (parentIP, mask);
        System.out.println ("The IP address of the parent network is: " + s.arrToString(networkAddress));

        // store network in an ArrayList
        ArrayList<Subnet> netlist = a.netList(in);

        // as the the list is sorted ascendingly, we need to assign the ip address from the last position
        for (int i = netlist.size() - 1; i >= 0; i --) {
            Subnet sub = netlist.get(i);
            // assign network address
            sub.setIP(networkAddress);

            // if it is the last subnet, exit the loop
            if (i == 0) {
                break;
            }

            // int num_netAddress = sub.getAllocatedAddresses();
            // calculate the ip address for next subnet
            long nextIP = a.ipToLong(sub.getIPAdd()) + sub.getAllocatedAddresses();
            networkAddress = a.longToArr(nextIP);
        }
        System.out.println (netlist);
    }

    /**
     * Get network address according to IP and mask
     * Use logical AND to calculate each bit of the network
     * @param IP
     * @param mask
     * @return networkAddress
     */
    public int[] getNetwork (int[] IP, int[] mask) {
        int [] networkAddress = new int [4];
        for (int i = 0; i < 4; i ++) {
            networkAddress[i] = IP[i] & mask [i];
        }
        return networkAddress;
    }

    /**
     * Store different subnets into ArrayList<Subnet>
     * And sort the list according to the number of hosts
     * @param map
     */
    public ArrayList<Subnet> netList (Scanner in) {
        ArrayList<Subnet> list = new ArrayList<>();
        String name = "";
        String hosts = "";
        System.out.println ("\nEnter the name of subnet and required hosts for this subnet and End with \"!\": \n " 
        + "  __________________\n"
        + "  | Format example:  |\n" 
        + "  |      network1 10 |\n"
        + "  |      network2 2  |\n"
        + "  |      network3 20 |\n"
        + "  |      !           |\n"
        + "  |__________________|\n");

        while (!name.equals("!")) {
            name = in.next();
            // if ! is detected, terminate immediately
            if (name.equals("!")) {
                break;
            }
            hosts = in.next();
            Subnet s = new Subnet(name, Integer.parseInt(hosts));
            list.add(s);
        }
        Collections.sort(list);
        return list;
    }

    /**
     * This will convert ip address to a long number (e.g., 192.168.1.32 = 3232235808)
     * @param ip
     * @return
     */
    public long ipToLong (int[] ip) {
        long dec_address = 0;
        
        for (int i = 0; i < 4; i ++) {
           dec_address += ip[i] * (long)Math.pow(2, (3-i)*8);
        }  
        return dec_address;
    }

    /**
     * This converts long number back to ip address int[]
     * @param number
     * @return
     */
    public int[] longToArr (long number) {
        int[] ip = new int[4];

        for (int i = 0; i < 4; i ++) {
            ip[i] = (int) (number / ((long)Math.pow(2, 24 - i * 8)));
            number %= (long)Math.pow(2, 24 - i * 8);
        }
        return ip;
    }
}
