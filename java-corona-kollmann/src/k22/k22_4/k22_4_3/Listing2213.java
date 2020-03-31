package k22.k22_4.k22_4_3;

import java.util.*;

class Producer2213
extends Thread
{
  private Vector v;

  public Producer2213(Vector v)
  {
    this.v = v;
  }

  public void run()
  {
    String s;

    while (true) {
      synchronized (v) {
        s = "Wert "+Math.random();
        v.addElement(s);
        System.out.println("Produzent erzeugte "+s);
        v.notify();
      }
      try {
        Thread.sleep((int)(100*Math.random()));
      } catch (InterruptedException e) {
        //nichts
      }
    }
  }
}

class Consumer2213
extends Thread
{
   private Vector v;

   public Consumer2213(Vector v)
   {
      this.v = v;
   }

   public void run()
   {
      while (true) {
         synchronized (v) {
            if (v.size() < 1) {
               try {
                  v.wait();
               } catch (InterruptedException e) {
                  //nichts
               }
            }
            System.out.print(
              " Konsument fand "+(String)v.elementAt(0)
            );
            v.removeElementAt(0);
            System.out.println(" (verbleiben: "+v.size()+")");
         }
         try {
            Thread.sleep((int)(100*Math.random()));
         } catch (InterruptedException e) {
            //nichts
         }
      }
   }
}

public class Listing2213
{
   public static void main(String[] args)
   {
      Vector v = new Vector();

      Producer2213 p = new Producer2213(v);
      Consumer2213 c = new Consumer2213(v);
      p.start();
      c.start();
   }
}
