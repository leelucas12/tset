package opt;
import java.util.Random;
import java.util.Vector;
public class Ant {
    public Vector<Integer> tabu; // 路线路线
    Integer [] allowedCities; // 允许搜索的城市
    public double[][] distances; // 距离矩阵
    private double alpha=1;
    private double beta=2;
    public double tourLength=0.0; // 路径长度
    private int cityNum; // 城市数量
    private int firstCity; // 起始城市
    private int currentCity; // 当前城市
    private double load;//当前的车载量
    private double [] customs;
    public int start;
    public int end;

    public void selectcities(double capacity, double[][] tao) {
        double[] p= new double[cityNum];//保存下一步的几率
        double sum=0;
        //计算公式中的分母部分（为下一步计算选中概率使用）
        for (int i = 0; i < cityNum; i++) {
            if (allowedCities[i] == -1)//没走过
                sum += (Math.pow(tao[currentCity][i], alpha) *
                        Math.pow(1.0 / distances[currentCity][i], beta));
        }

        for (int i=0;i < cityNum; i++) {
            if (allowedCities[i] == -1&&load+customs[i]<=capacity)
                p[i] = (Math.pow(tao[currentCity][i], alpha) *
                    Math.pow(1.0 / distances[currentCity][i], beta)) / sum;
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
                start=end;
                end=tabu.size()-1;
                currentCity = firstCity;
                load = 0;
                exchange(start,end);//2-opt交换

            }
            allowedCities[selectcity] = 1;
        }else if(zong==0){
            //已经无法承载了，返回配送中心
            tabu.add(firstCity);
            currentCity = firstCity;
            load = 0;
            start=end;
            end=tabu.size()-1;
            exchange(start,end);//2-opt交换
            this.selectcities(capacity,tao);

        }

    }

    public void randomcities(double capacity,double[][] tao) {
        double[] p= new double[cityNum];//保存下一步的几率

        for (int i=0;i < cityNum; i++) {
            if (allowedCities[i] == -1&&load+customs[i]<=capacity)
                p[i] =0.1;
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
                load = load + customs[selectcity];
                tabu.add(firstCity);
                start=end;
                end=tabu.size()-1;
                currentCity = firstCity;
                load = 0;
                exchange(start,end);//2-opt交换
            }
            allowedCities[selectcity] = 1;
        }else if(zong==0){
            //已经无法承载了，返回配送中心
            tabu.add(firstCity);
            currentCity = firstCity;
            load = 0;
            start=end;
            end=tabu.size()-1;
            exchange(start,end);//2-opt交换
            this.selectcities(capacity,tao);

        }
    }

    public void exchange(int start, int end) {
        int max=end-1;
        int min=start+1;
        int t=max-min;
        if (max!=min){
            double original=0;//原始的路径长度
            double alter=0;//交换后的路径长度
            //循环250次
            if(t>8){
            for(int i=0;i<400;i++) {
                //复制路线
                Vector<Integer> copyTabu = new Vector<>();
                for (int j = 0; j < tabu.size(); j++) {
                    copyTabu.add(j, tabu.get(j));
                }
                //生成两个随机数在[min,max]之间
                int first = min + (int) (Math.random() * (max - min + 1));
                int sceond = min + (int) (Math.random() * (max - min + 1));
                //交换位置
                copyTabu.set(first, tabu.get(sceond));
                copyTabu.set(sceond, tabu.get(first));
                alter = jisaun(copyTabu);//计算路径长度
                original = jisaun(tabu);//计算路径长度
                if (alter < original) {
                    //如果找到合适的就交换
                    tabu.set(first, copyTabu.get(first));
                    tabu.set(sceond, copyTabu.get(sceond));
                }
            }

            }else{
                for(int i=0;i<100;i++) {
                    //复制路线
                    Vector<Integer> copyTabu = new Vector<>();
                    for (int j = 0; j < tabu.size(); j++) {
                        copyTabu.add(j, tabu.get(j));
                    }
                    //生成两个随机数在[min,max]之间
                    int first = min + (int) (Math.random() * (max - min + 1));
                    int sceond = min + (int) (Math.random() * (max - min + 1));
                    //交换位置
                    copyTabu.set(first, tabu.get(sceond));
                    copyTabu.set(sceond, tabu.get(first));
                    alter = jisaun(copyTabu);//计算路径长度
                    original = jisaun(tabu);//计算路径长度
                    if (alter < original) {
                        //如果找到合适的就交换
                        tabu.set(first, copyTabu.get(first));
                        tabu.set(sceond, copyTabu.get(sceond));
                    }
                }

            }

        }
    }

    public double jisaun(Vector<Integer> copy) {
        double length=0;
        for (int j = 0;j < copy.size()-1; j++) {
            length =length+ distances[copy.get(j)][copy.get(j + 1)];//从A经过每个城市仅一次，最后回到A的总长度。
        }
        return length;
    }

    public void CalTourLength() {
        tourLength=0;
        //判断当前的车辆是否已经返回
        if(firstCity!=currentCity)
             tabu.add(firstCity);
        for (int i = 0; i < tabu.size()-1; i++) {
            tourLength += distances[tabu.get(i)][tabu.get(i + 1)];//从A经过每个城市仅一次，最后回到A的总长度。
        }
    }


    public void init(int citycount, double[] custom, double[][] dista) {
        cityNum=citycount;
        // 初始允许搜索的城市集合
        allowedCities = new Integer[citycount];
        // 初始禁忌表
        tabu = new Vector<>();
        //选择配送中心
        firstCity = cityNum-1;
        // 允许搜索的城市集合中移除起始城市
        for(int i=0;i<cityNum;i++){
            allowedCities[i]=-1;
        }
        allowedCities[cityNum-1]=1;
        //将起始城市添加路线
        tabu.add(Integer.valueOf(firstCity));
        start=0;
        end=0;
        // 当前城市为起始城市
        currentCity = firstCity;
        //初始化
        load=custom[currentCity];
        customs=custom;
        distances=dista;

    }
}
