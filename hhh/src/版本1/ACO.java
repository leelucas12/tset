package 版本1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ACO {
    int antcount;//蚂蚁的数量
    double [][]distance;//表示城市间距离
    double [][]tao;//信息素矩阵
    int citycount;//城市数量
    double capacity;//车辆的载重能力
    double []custom;//客户需求
    int times;//循环次数
    List<Integer> besttour= new LinkedList<>();
    double bestlength=Integer.MAX_VALUE;
    private Ant[] ants;

    public void init(String filename, int antnum) throws IOException {
        int[] x;
        int[] y;
        BufferedReader tspdata = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String strbuff = tspdata.readLine();//读取第一行，城市总数（按文件格式读取）
        citycount = Integer.valueOf(strbuff);
        capacity=Double.valueOf(tspdata.readLine());
        distance = new double[citycount][citycount];
        x = new int[citycount];
        y = new int[citycount];
        custom=new double[citycount];
        for (int citys = 0; citys < citycount; citys++) {
            strbuff = tspdata.readLine();
            String[] strcol = strbuff.split(" ");
            x[citys] = Integer.valueOf(strcol[1]);//读取每排数据的第2二个数字即横坐标
            y[citys] = Integer.valueOf(strcol[2]);//读取每排数据的第3三个数字即纵坐标
            custom[citys]=Integer.valueOf(strcol[3]);
        }
        //计算两个城市之间的距离矩阵，并更新距离矩阵
        for (int city1 = 0; city1 < citycount ; city1++) {
            distance[city1][city1] = 0;//对角线为0；
            for (int city2 = city1 + 1; city2 < citycount; city2++) {
                distance[city1][city2] = (Math.sqrt((x[city1] - x[city2]) * (x[city1] - x[city2])
                        + (y[city1] - y[city2]) * (y[city1] - y[city2])));
                distance[city2][city1] = distance[city1][city2];//距离矩阵是对称矩阵
            }
        }
        //输出距离矩阵
        System.out.println("距离矩阵");
        for (int city1 = 0; city1 < citycount ; city1++) {
            for (int city2 = 0; city2 < citycount; city2++) {
                System.out.print(String.format("%.2f", distance[city1][city2]) +"  ");//距离矩阵是对称矩阵
            }
            System.out.println();
        }

        tao=new double[citycount][citycount];
        for(int i=0;i<citycount;i++)
        {
            for(int j=0;j<citycount;j++){
                tao[i][j]=0.2;
            }
        }

        antcount=antnum;
        //System.out.println(antcount);
        ants=new Ant[antnum];
        //初始化蚂蚁位置
        for(int i=0;i<antcount;i++){
            ants[i]=new Ant();
            ants[i].init(citycount,custom);
        }
        //

    }


    public void run(int hh) {
        times=hh;
        //循环次数
        for (int i=0;i<times;i++){
            //蚂蚁循环
            for(int j=0;j<antcount;j++) {
                for (int k = 0; k < citycount-1; k++) {
                    ants[j].selectcities(custom, capacity, distance, tao);
                    }
                ants[j].CalTourLength(distance);
                if( ants[j].tourLength<bestlength){
                    //更新旅游路线
                    //besttour=ants[j].
                    besttour=ants[j].tabu;
                     System.out.println("第 " +i+"次循环  第" +j+"只蚂蚁 " +"找到最优路径 "+ants[j].tourLength);
                    System.out.println(besttour);
                    bestlength=ants[j].tourLength;

                }
            }
            UpdateTao();
            for(int k=0;k<antcount;k++){
                ants[k].init(citycount,custom);
            }
        }
    }

    private void UpdateTao(){
        double rou=0.5;
        //信息素挥发
        for(int i=0;i<citycount;i++)
            for(int j=0;j<citycount;j++)
                tao[i][j]=tao[i][j]*(1-rou);
        //信息素更新
        for(int i=0;i<antcount;i++){
            for(int j=0;j<citycount;j++){
                //tao[ants[i].tour[j]][ants[i].tour[j+1]]+=1.0/ ants[i].tourlength;
                tao[ants[i].tabu.get(j)][ants[i].tabu.get(j+1)]+=1.0/ants[i].tourLength;
            }
        }
    }
}
