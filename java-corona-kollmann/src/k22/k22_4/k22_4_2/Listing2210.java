/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22.k22_4.k22_4_2;

/**
 *
 * @author olive
 */
public class Listing2210 extends Thread {
    static int cnt = 0;
    public static void main(String[] args) {
        Thread t1 = new Listing2210();
        Thread t2 = new Listing2210();
        t1.start();
        t2.start();
    }
    
    public void run() {
        while(true) {
            synchronized (getClass()) {
                System.out.println(cnt++);
            }
        }
    }
}