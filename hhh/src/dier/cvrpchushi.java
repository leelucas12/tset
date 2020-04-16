package dier;

import java.io.IOException;

public class cvrpchushi {
    public static void main(String[] args) throws IOException {
        for (int i=0;i<10;i++) {
            long startTime = System.currentTimeMillis();    //获取开始时间
            ACO aco = new ACO();
            //aco.init("./src/dier/att49.txt", 51);//城市信息文件，蚂蚁数量
            aco.init("22.txt",22);
            aco.run(100);
            System.out.println("基础蚁群 第"+(i+1)+"次蚁群计算结果");
           //aco.printBest();
           aco.printCapacity();
            long end = System.currentTimeMillis();    //获取开始时间
            System.out.println("耗时"+(end-startTime)+"ms");
            for(int j=0;j<aco.besttour.size();j++ ){
                int t=aco.besttour.get(j);
                System.out.print(aco.x[t]+" ");
                System.out.println(aco.y[t]);
            }
        }
    }
}
