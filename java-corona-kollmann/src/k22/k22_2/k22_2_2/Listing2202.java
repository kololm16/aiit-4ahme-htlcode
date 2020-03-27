/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22.k22_2.k22_2_2;

/**
 *
 * @author olive
 */
public class Listing2202 {
    public static void main(String[] args) {
        MyThread2202 t = new MyThread2202();
        t.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //nichts
        }
        t.stop(); //deprecated - nicht mehr verwenden
    }
}
