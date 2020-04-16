package 版本1;

import java.io.IOException;

public class cvrp {
    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();    //获取开始时间
        ACO aco = new ACO();
        aco.init("./src/版本1/att49.txt.", 5);//城市信息文件，蚂蚁数量
        aco.run(100);
    }
}
