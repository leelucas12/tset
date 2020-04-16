package 版本1;

import java.util.Random;
import java.util.Vector;

public class tss {
    public static void main(String[] args) {
        double r = new Random().nextDouble();
       // System.out.println(r);

        int max=6;
        int min=0;
        for(int i=0;i<1000;i++) {

            Random random = new Random();
            int first = random.nextInt(max) % (max - min + 1) + min;
            int sceond = random.nextInt(max) % (max - min + 1) + min;
            int num = min + (int)(Math.random() * (max-min+1));
            if(num==0)
              System.out.println(num);
            //System.out.println(sceond);
        }

//        Vector v1 = new Vector();
//        Integer integer1= new Integer(1);
////加入为字符串对象
//        v1.addElement("one");
////加入的为integer的对象
//        v1.addElement(integer1);
//        v1.addElement(integer1);
//        v1.addElement("two");
//        v1.addElement(new Integer(2));
//        v1.addElement(integer1);
//        v1.addElement(integer1);
//
//
//        System.out.println(v1);
//        System.out.println(first+" "+sceond);
//
//
//
//        System.out.println(v1);


    }
}
