package dier;

import java.util.Random;
import java.util.Vector;

public class Ant {
    public Vector<Integer> tabu; // 路线路线
    Integer [] allowedCities; // 允许搜索的城市

    private double alpha=1;
    private double beta=2;
    public double tourLength; // 路径长度
    private int cityNum; // 城市数量
    private int firstCity; // 起始城市
    private int currentCity; // 当前城市
    private double load;//当前的车载量
    private double [] customs;
    public void init(int citycount, double[] custom) {
        cityNum=citycount;
        // 初始允许搜索的城市集合
        allowedCities = new Integer[citycount];
        // 初始禁忌表
        tabu = new Vector<Integer>();
        //选择配送中心
        firstCity = cityNum-1;
       // 允许搜索的城市集合中移除起始城市
        for(int i=0;i<cityNum;i++){
            allowedCities[i]=-1;
        }
        allowedCities[cityNum-1]=1;
         //将起始城市添加路线
        tabu.add(Integer.valueOf(firstCity));
        // 当前城市为起始城市
        currentCity = firstCity;
        //初始化
        load=custom[currentCity];
        customs=custom;
    }

    public void selectcities( double capacity, double[][] distance, double[][] tao) {
        double[] p= new double[cityNum];//保存下一步的几率
        double sum=0;
        //计算公式中的分母部分（为下一步计算选中概率使用）
        for (int i = 0; i < cityNum; i++) {
            if (allowedCities[i] == -1)//没走过
                sum += (Math.pow(tao[currentCity][i], alpha) *
                        Math.pow(1.0 / distance[currentCity][i], beta));
        }

        for (int i=0;i < cityNum; i++) {
            if (allowedCities[i] == -1&&load+customs[i]<=capacity)
                p[i] = (Math.pow(tao[currentCity][i], alpha) *
                    Math.pow(1.0 / distance[currentCity][i], beta)) / sum;
            else {
                p[i] = 0.0;//城市走过了或者是无法为该城市服务，选中概率就是0
            }
        }
        //轮盘赌法
        Random r = new Random();
        double sumselect = 0;
        double zong=0;
        int selectcity = -1;
        for (int i = 0; i < cityNum; i++) {//每次都是顺序走。。。。。
            zong+=p[i];
            }
        if(zong!=0) {
            double selectp = r.nextDouble() * zong;
            //System.out.println("随机数"+selectp+" 总概率 "+zong);
            for (int i = 0; i < cityNum; i++) {
                sumselect += p[i];
                if (sumselect >= selectp) {
                    selectcity = i;
                    break;
                }
            }
            if (load + customs[selectcity] < capacity) {
                //判断是否可以进行载重；如果可以就更新当前城市和路线
                tabu.add(selectcity);
                currentCity = selectcity;
                load = load + customs[selectcity];
            } else if (load + customs[selectcity] == capacity) {
                //如果刚刚好 那么就回配送中心，并当前城市为出发地点
                tabu.add(selectcity);
                tabu.add(firstCity);
                currentCity = firstCity;
                load = 0;
            }
            allowedCities[selectcity] = 1;
        }else if(zong==0){
            //已经无法承载了，返回配送中心
            tabu.add(firstCity);
            currentCity = firstCity;
            load = 0;
            this.selectcities(capacity,distance,tao);

        }

    }

    public void CalTourLength(double[][] distance) {
        tourLength = 0;
        //判断当前的车辆是否已经返回
        if(firstCity!=currentCity)
             tabu.add(firstCity);
        for (int i = 0; i < tabu.size()-1; i++) {
            tourLength += distance[tabu.get(i)][tabu.get(i + 1)];//从A经过每个城市仅一次，最后回到A的总长度。
        }
    }




}
