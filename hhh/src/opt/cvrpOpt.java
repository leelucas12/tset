package opt;

import java.io.IOException;

public class cvrpOpt {
    public static void main(String[] args) throws IOException {
        for (int i=0;i<10;i++) {
            long startTime = System.currentTimeMillis();    //获取开始时间
            ACO aco = new ACO();
            //aco.init("./src/opt/att49.txt", 30);//城市信息文件，蚂蚁数量
            aco.init("22.txt",22);

            //aco.init("./src/opt/eil51.vrp", 51);//城市信息文件，蚂蚁数量
            aco.run(300);
            System.out.println("OPT 第"+(i+1)+"次蚁群计算结果");
           // aco.printBest();
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
