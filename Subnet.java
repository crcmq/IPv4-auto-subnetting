import java.util.Arrays;
/**
 * This object contains the properties of a network
 */

public class Subnet implements Comparable<Subnet>{
    private String name;
    private int[] ipAddress;
    private int hosts;
    private int prefix;
    private int[] mask;
    private int allocatedAddresses;

    public Subnet () {
    }

    public Subnet (String _name, int _hosts) {
        this.name = _name;
        this.hosts = _hosts;
        this.prefix = getPrefix(_hosts);
        this.mask = prefixToMask(this.prefix);
    }
    
    /**
     * Set IP address for this subnet
     * @param IP
     */
    public void setIP (String IP) {
        this.ipAddress = stringToArr(IP);
    }

    /**
     * Set IP address for this subnet
     * @param IP
     */
    public void setIP (int[] IP) {
        this.ipAddress = IP;
    }
    
    /**
     * convert String to an int[]
     * s is IP address
     * @param s
     * @return arr
     */
    public int[] stringToArr (String s) {
        int [] arr = new int [4];
        String [] sArr = s.split ("\\.");
        for (int i = 0; i < 4; i ++) {
            arr[i] = Integer.parseInt(sArr[i]);
        }
        return arr;
    }

    /**
     * Calculate required addresses for the hosts
     * Also return prefix for this network
     * @param host
     * @return allocatedAddresses
     */
    public int getPrefix (int host) {
        int i;
        for (i = 0; i < 32 && this.allocatedAddresses == 0; i ++) {
            int addresses = (int)Math.pow(2, i);
            // found the suitable number of addresses
            if (hosts + 2 <= addresses) {
                this.allocatedAddresses = addresses;
            }
        }
        return 32 - i + 1;
    }

    /**
     * get network name
     * @return name
     */
    public String getName () {
        return this.name;
    }

    /**
     * Get IP address of this network
     * @return ipAddress
     */
    public int[] getIPAdd () {
        return this.ipAddress;
    }

    /**
     * Get the number of hosts in this network
     * @return hosts
     */
    public int getHosts() {
        return hosts;
    }

    /**
     * Get the prefix of this network
     * @return prefix
     */
    public int getPrefix() {
        return prefix;
    }

    /**
     * Allocate the number of addresses to this network
     * @param address
     */
    public void setAllocatedAddress (int address) {
        this.allocatedAddresses = address;
    }

    /**
     * Get the number of addresses of this network
     * @return allocatedAddresses
     */
    public int getAllocatedAddresses () {
        return this.allocatedAddresses;
    }

    /**
     * Convert prefix to subnet mask
     * IPv4 address has 4 sections. Each section has 8 bits:
     *      00000000.00000000.00000000.00000000
     * Prefix is the number of bits with value of 1:
     *      prefix = 24, network is:
     *      11111111.11111111.11111111.00000000
     * @param prefix
     * @return
     */
    public int[] prefixToMask (int prefix) {
        int[] mask = new int[4];

        // find out which section should be calculated
        int sectionNum = prefix / 8; 
        // find out the left bits
        int bitOfSection = prefix % 8;

        // calculate the decimal representation of a section
        // For example, bitOfSection = 4
        // section = 2^7 + 2^6 + 2^5 + 2^4
        int section = 0;
        for (int i = 1; i <= bitOfSection; i ++) {
            section += (int)Math.pow(2, 8 - i);
        }

        for (int i = 0; i < 4; i ++) {
            if (i < sectionNum) {
                mask [i] = 255;
            }
            else if (i == sectionNum && bitOfSection != 0) {
                mask [i] = section;
            }           
        }
        return mask;
    }

    /**
     * This commpare two subnets according to the number of their hosts
     * @param s
     * @return
     */
    @Override
    public int compareTo(Subnet s) {
        return this.hosts - s.hosts;
    }

    /**
     * Convert int[] to String
     * @param arr
     * @return String
     */
    public String arrToString (int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i ++) {
            sb.append(arr[i]);
            if (i != 3) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    public String toString () {
        String stringMask = arrToString(mask);
        String stringIP = arrToString(ipAddress);
        return name + ": " + hosts 
        + " | Allocate: " + allocatedAddresses 
        + " | IP address: " + stringIP
        + " | prefix " + prefix
        + " | mask: " + stringMask
        + "\n";
    }
}
