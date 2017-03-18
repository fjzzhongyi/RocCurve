package sml_haze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Curves {//ba'ba'wo'lai
	
	static final int T = 7;
	
	static int gasdm[][]; // from w0 to wn
	static int nphgs[][];
	static int eventtree[][];
	static int lgta[][];
	static int fss[][];
	static int real[][]; // from w0 to wn
	static int gasdmi = 0;
	static int nphgsi = 0;
	static int eventtreei = 0;
	static int lgtai = 0;
	static int fssi = 0;
	static int reali = 0;
	
	private static void init() {
		File file_gasdm = new File("data//haze//sml//states_haze_result_B=0segment_19_20161022_024238_filterFalse_threshold=0.txt");
		File file_nphgs = new File("data//haze//NPHGS-result-haze.txt");		
		File file_eventtree = new File("data//haze//EventTree-result-haze.txt");
		File file_lgta = new File("data//haze//LGTA-result-haze.txt");
		File file_real = new File("data//haze//TrueLable-haze.txt");
		BufferedReader reader_gasdm = null;
		BufferedReader reader_nphgs = null;
		BufferedReader reader_eventtree = null;
		BufferedReader reader_lgta = null;
		BufferedReader reader_real = null;
		gasdm = new int[280][34];
		nphgs = new int[280][34];
		eventtree = new int[280][34];
		lgta = new int[280][34];
		real = new int[280][34];
		try {
			reader_gasdm = new BufferedReader(new FileReader(file_gasdm));
			reader_nphgs = new BufferedReader(new FileReader(file_nphgs));
			reader_eventtree = new BufferedReader(new FileReader(file_eventtree));
			reader_lgta = new BufferedReader(new FileReader(file_lgta));
			reader_real = new BufferedReader(new FileReader(file_real));
			String str_gasdm = null;
			String str_nphgs = null;
			String str_eventtree = null;
			String str_lgta = null;
			String str_real = null;
			while ((str_gasdm = reader_gasdm.readLine()) != null && (str_nphgs = reader_nphgs.readLine()) != null
					&& (str_eventtree = reader_eventtree.readLine()) != null && (str_lgta = reader_lgta.readLine()) != null
					&& (str_real = reader_real.readLine()) != null) {
				String[] str_gasdms;
				String[] str_nphgss;
				String[] str_eventtrees;
				String[] str_lgtas;
				String[] str_reals;
				str_gasdms = str_gasdm.split("\\ ");
				str_nphgss = str_nphgs.split("\\ ");
				str_eventtrees = str_eventtree.split("\\ ");
				str_lgtas = str_lgta.split("\\ ");
				str_reals = str_real.split("\\ ");
				for (int i=0; i<34; i++) {
					gasdm[gasdmi][i] = Integer.parseInt(str_gasdms[i+2]);
					nphgs[nphgsi][i] = Integer.parseInt(str_nphgss[i+2]);
					eventtree[eventtreei][i] = Integer.parseInt(str_eventtrees[i+2]);
					lgta[lgtai][i] = Integer.parseInt(str_lgtas[i+2]);
					real[reali][i] = Integer.parseInt(str_reals[i+2]);
				}
				gasdmi++;
				nphgsi++;
				eventtreei++;
				lgtai++;
				reali++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static ArrayList<double[]> foreDete(String type) {
		ArrayList<double[]> result = new ArrayList<double[]>();
		for (int i=T; i<gasdmi-T; i++) {
			int win[] = new int[34];
			for (int j=-T; j<=T; j++) {//forecasting and detection
				for (int k=0; k<34; k++) {
					win[k] += real[i+j][k];
				}
			}
			
			double[] temp = null;
			if (type.equals("gasdm")){
				temp = compare(gasdm[i], win);//TP,TN,FP,FN
			} else if (type.equals("nphgs")) {
				temp = compare(nphgs[i], win);//TP,TN,FP,FN
			} else if (type.equals("eventtree")) {
				temp = compare(eventtree[i], win);//TP,TN,FP,FN
			} else if (type.equals("lgta")) {
				temp = compare(lgta[i], win);//TP,TN,FP,FN
			} 
			
			double TP = temp[0];
			double TN = temp[1];
			double FP = temp[2];
			double FN = temp[3];
			double FPR, TPR, precision, recall, score;
			FPR = (FP+TN==0) ? 0 : 1.0*FP/(FP+TN);
			TPR = (TP+FN==0) ? 1 : 1.0*TP/(TP+FN);
			precision = (TP+FP==0) ? 1 : 1.0*TP/(TP+FP);
			recall = (TP+FN==0) ? 1 : 1.0*TP/(TP+FN);
			score = (precision+recall==0) ? 0 : 2.0*precision*recall/(precision+recall);
			temp[4] = FPR;
			temp[5] = TPR;
			temp[6] = precision;
			temp[7] = recall;
			temp[8] = score;
			
			double lead=0, lag=0;
			int leadcnt=0, lagcnt=0;
			for (int k=0; k<34; k++) {
				if (real[i][k]==1) {
					int sign = 0;
					for (int j=-T; j<0; j++) {
						if(type.equals("gasdm")){
							if (gasdm[i+j][k]==1) {
								lead += -j;
								leadcnt++;
								sign = 1;
								break;
							}
						}else if(type.equals("nphgs")){
							if (nphgs[i+j][k]==1) {
								lead += -j;
								leadcnt++;
								sign = 1;
								break;
							}
						}else if(type.equals("eventtree")){
							if (eventtree[i+j][k]==1) {
								lead += -j;
								leadcnt++;
								sign = 1;
								break;
							}
						}else if(type.equals("lgta")){
							if (lgta[i+j][k]==1) {
								lead += -j;
								leadcnt++;
								sign = 1;
								break;
							}
						}
					}
					if (sign == 0){
						lead += 0;
						leadcnt++;
					}
				}
			}
			temp[9] = lead;
			temp[10] = leadcnt;
			for (int k=0; k<34; k++) {
				if (real[i][k]==1) {
					int sign = 0;
					for (int j=0; j<=T; j++) {
						if(type.equals("gasdm")){
							if (gasdm[i+j][k]==1) {
								lag += j;
								lagcnt++;
								sign = 1;
								break;
							}
						}else if(type.equals("nphgs")){
							if (nphgs[i+j][k]==1) {
								lag += j;
								lagcnt++;
								sign = 1;
								break;
							}
						}else if(type.equals("eventtree")){
							if (eventtree[i+j][k]==1) {
								lag += j;
								lagcnt++;
								sign = 1;
								break;
							}
						}else if(type.equals("lgta")){
							if (lgta[i+j][k]==1) {
								lag += j;
								lagcnt++;
								sign = 1;
								break;
							}
						}
					}
					if (sign == 0){
						lag += T;
						lagcnt++;
					}
				}
			}
			temp[11] = lag;
			temp[12] = lagcnt;
			result.add(temp);
		}
		return result;
	}
	
	private static void output(String title, String xlabel, String ylabel, ArrayList<Point> gasdm,
			ArrayList<Point> nphgs, ArrayList<Point> eventtree, ArrayList<Point> lgta){
		String filename = title;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write("title: "+title);
			writer.newLine();
			writer.write("x_lable: "+xlabel);
			writer.newLine();
			writer.write("y_lable: "+ylabel);
			writer.newLine();
			writer.write("GASDM: ");
			writer.newLine();
			for (int i=0; i<gasdm.size(); i++) {
				writer.write(Double.toString(gasdm.get(i).x));
				writer.write(" ");
				writer.write(Double.toString(gasdm.get(i).y));
				writer.newLine();
			}
			writer.write("NPHGS: ");
			writer.newLine();
			for (int i=0; i<nphgs.size(); i++) {
				writer.write(Double.toString(nphgs.get(i).x));
				writer.write(" ");
				writer.write(Double.toString(nphgs.get(i).y));
				writer.newLine();
			}
			writer.write("EventTree: ");
			writer.newLine();
			for (int i=0; i<eventtree.size(); i++) {
				writer.write(Double.toString(eventtree.get(i).x));
				writer.write(" ");
				writer.write(Double.toString(eventtree.get(i).y));
				writer.newLine();
			}
			writer.write("LGTA: ");
			writer.newLine();
			for (int i=0; i<lgta.size(); i++) {
				writer.write(Double.toString(lgta.get(i).x));
				writer.write(" ");
				writer.write(Double.toString(lgta.get(i).y));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void plotForeDete() {
		//[0]TP,[1]TN,[2]FP,[3]FN,[4]FPR,[5]TPR,[6]precision,[7]recall,[8]score,[9]lead,[10]leadcnt,[11]lag,[12]lagcnt
		ArrayList<double[]> resultgasdm = new ArrayList<double[]>();
		ArrayList<double[]> resultnphgs = new ArrayList<double[]>();
		ArrayList<double[]> resulteventtree = new ArrayList<double[]>();
		ArrayList<double[]> resultlgta = new ArrayList<double[]>();
		
		resultgasdm = foreDete("gasdm");
		resultnphgs = foreDete("nphgs");
		resulteventtree = foreDete("eventtree");
		resultlgta = foreDete("lgta");
		
		ArrayList<Point> plotgasdm = new ArrayList<Point>();
		ArrayList<Point> plotnphgs = new ArrayList<Point>();
		ArrayList<Point> ploteventtree = new ArrayList<Point>();
		ArrayList<Point> plotlgta = new ArrayList<Point>();
		plotgasdm = TPRCurve(resultgasdm);
		plotnphgs = TPRCurve(resultnphgs);
		ploteventtree = TPRCurve(resulteventtree);
		plotlgta = TPRCurve(resultlgta);
		DrawPlot.draw("FPR vs TPR(Forecasting and detection)", "False Positive Rate(From 0-1 FP Per-day)", 
				"True Positive Rate(Forecasting and detection)", plotgasdm, plotnphgs, ploteventtree, plotlgta);
		output("FPR vs TPR(Forecasting and detection)", "False Positive Rate(From 0-1 FP Per-day)", 
				"True Positive Rate(Forecasting and detection)", plotgasdm, plotnphgs, ploteventtree, plotlgta);
		plotgasdm.clear();
		plotnphgs.clear();
		ploteventtree.clear();
		plotlgta.clear();
		
		plotgasdm = leadCurve(resultgasdm);
		plotnphgs = leadCurve(resultnphgs);
		ploteventtree = leadCurve(resulteventtree);
		plotlgta = leadCurve(resultlgta);
		DrawPlot.draw("FPR vs Lead Time(Forecasting)", "False Positive Rate(From 0-1 FP Per-day)", 
				"Lead Time(Forecasting)", plotgasdm, plotnphgs, ploteventtree, plotlgta);
		output("FPR vs Lead Time(Forecasting)", "False Positive Rate(From 0-1 FP Per-day)", 
				"Lead Time(Forecasting)", plotgasdm, plotnphgs, ploteventtree, plotlgta);
		plotgasdm.clear();
		plotnphgs.clear();
		ploteventtree.clear();
		plotlgta.clear();
		
		plotgasdm = lagCurve(resultgasdm);
		plotnphgs = lagCurve(resultnphgs);
		ploteventtree = lagCurve(resulteventtree);
		plotlgta = lagCurve(resultlgta);
		DrawPlot.draw("FPR vs Lag Time(Detection)", "False Positive Rate(From 0-1 FP Per-day)", 
				"Lag Time(Detection)", plotgasdm, plotnphgs, ploteventtree, plotlgta);
		output("FPR vs Lag Time(Detection)", "False Positive Rate(From 0-1 FP Per-day)", 
				"Lag Time(Detection)", plotgasdm, plotnphgs, ploteventtree, plotlgta);
	}
	
	private static ArrayList<double[]> fore(String type) {
		ArrayList<double[]> result = new ArrayList<double[]>();
		
		for (int i=T; i<gasdmi-T; i++) {
			int win[] = new int[34];
			for (int j=-T; j<0; j++) {//forecasting
				for (int k=0; k<34; k++) {
					win[k] += real[i+j][k];
				}
			}
			double[] temp=null;
			if(type.equals("gasdm")) {
				temp = compare(gasdm[i], win);//TP,TN,FP,FN
			} else if(type.equals("nphgs")) {
				temp = compare(nphgs[i], win);//TP,TN,FP,FN
			} else if(type.equals("eventtree")) {
				temp = compare(eventtree[i], win);//TP,TN,FP,FN
			} else if(type.equals("lgta")) {
				temp = compare(lgta[i], win);//TP,TN,FP,FN
			}
			
			double TP = temp[0];
			double TN = temp[1];
			double FP = temp[2];
			double FN = temp[3];
			double FPR, TPR, precision, recall, score;
			FPR = (FP+TN==0) ? 0 : 1.0*FP/(FP+TN);
			TPR = (TP+FN==0) ? 1 : 1.0*TP/(TP+FN);
			precision = (TP+FP==0) ? 1 : 1.0*TP/(TP+FP);
			recall = (TP+FN==0) ? 1 : 1.0*TP/(TP+FN);
			score = (precision+recall==0) ? 0 : 2.0*precision*recall/(precision+recall);
			temp[4] = FPR;
			temp[5] = TPR;
			temp[6] = precision;
			temp[7] = recall;
			temp[8] = score;
			result.add(temp);
		}
		return result;
	}
	
	private static void plotFore() {
		//[0]TP,[1]TN,[2]FP,[3]FN,[4]FPR,[5]TPR,[6]precision,[7]recall,[8]score,[9]lead,[10]leadcnt,[11]lag,[12]lagcnt
		ArrayList<double[]> resultgasdm = new ArrayList<double[]>();
		ArrayList<double[]> resultnphgs = new ArrayList<double[]>();
		ArrayList<double[]> resulteventtree = new ArrayList<double[]>();
		ArrayList<double[]> resultlgta = new ArrayList<double[]>();
		
		resultgasdm = fore("gasdm");
		resultnphgs = fore("nphgs");
		resulteventtree = fore("eventtree");
		resultlgta = fore("lgta");
		
		ArrayList<Point> plotgasdm = new ArrayList<Point>();
		ArrayList<Point> plotnphgs = new ArrayList<Point>();
		ArrayList<Point> ploteventtree = new ArrayList<Point>();
		ArrayList<Point> plotlgta = new ArrayList<Point>();
		plotgasdm = TPRCurve(resultgasdm);
		plotnphgs = TPRCurve(resultnphgs);
		ploteventtree = TPRCurve(resulteventtree);
		plotlgta = TPRCurve(resultlgta);
		DrawPlot.draw("FPR vs TPR(Forecasting)", "False Positive Rate(From 0-1 FP Per-day)", 
				"True Positive Rate(Forecasting)", plotgasdm, plotnphgs, ploteventtree, plotlgta);
		output("FPR vs TPR(Forecasting)", "False Positive Rate(From 0-1 FP Per-day)", 
				"True Positive Rate(Forecasting)", plotgasdm, plotnphgs, ploteventtree, plotlgta);
	}
	
	private static ArrayList<Point> TPRCurve(ArrayList<double[]> result) {
		ArrayList<Point> plot = new ArrayList<Point>(); 
		Collections.sort(result, new scoreComparator());
		double T=0, F=0;
		double TP = 0, FP = 0, FN = 0;
		double pre = 0, rec = 0, score = 0, sumscore = 0;
		for (int i=0; i<result.size(); i++) {//TP,TN,FP,FN
			T = T+result.get(i)[0]+result.get(i)[3];
			F = F+result.get(i)[1]+result.get(i)[2];
		}
		double P=0;
		for (int i=7; i<result.size()-7; i++) {
			P = P + result.get(i)[0] + result.get(i)[3];
//			System.out.println("[" + i + "]" + "TP: " + result.get(i)[0] + "\tTN: " + result.get(i)[1]
//					+ "\tFP: " + result.get(i)[2] + "\tFN: " + result.get(i)[3]
//					+ "\tP: " + P);
		}
//		System.out.println("----------------------------------------------------");
		int record = 1;
		for (int i=0; i<result.size(); i++) {
			TP += result.get(i)[0];
			FP += result.get(i)[2];
			FN += result.get(i)[3];
			pre = (TP+FP==0) ? 1 : 1.0*TP/(TP+FP);
			rec = (TP+FN==0) ? 1 : 1.0*TP/(TP+FN);
			score = (pre+rec==0) ? 0 : 2.0*pre*rec/(pre+rec);
			sumscore += score;
			double x,y;
			x = FP / 400;
			y = TP / 1390;
//			System.out.println("TP: " + TP + "\tP: " + P);
			if (x > 1) {
				record = i;
				break;
			}
			plot.add(new Point(x,y));
		}
		System.out.println("sumscore: " + sumscore/record);
		return plot;
	}
	
	private static ArrayList<Point> leadCurve(ArrayList<double[]> result) {
		ArrayList<Point> plot = new ArrayList<Point>(); 
		Collections.sort(result, new scoreComparator());
		double FP=0, P=0;
		double lead=0, leadcnt=0;
		for (int i=0; i<result.size(); i++) {
			P = P + result.get(i)[0] + result.get(i)[3];
		}
		double x=0, y=0;
		for (int i=0; i<result.size(); i++) {
			FP += result.get(i)[2];
			lead += result.get(i)[9];
			leadcnt += result.get(i)[10];
			
			x = FP / 400;
			if (x > 1) {
				break;
			}
			y = (leadcnt == 0) ? 0 : lead / P;
			plot.add(new Point(x,y));
		}
		return plot;
	}
	
	private static ArrayList<Point> lagCurve(ArrayList<double[]> result) {
		ArrayList<Point> plot = new ArrayList<Point>(); 
		Collections.sort(result, new scoreComparator());
		double FP=0, P=0;
		double lag=0, lagcnt=0;
		double x=0, y=0;
		for (int i=0; i<result.size(); i++) {
			P = P + result.get(i)[0] + result.get(i)[3];
		}
		for (int i=0; i<result.size(); i++) {
			FP += result.get(i)[2];
			lag += result.get(i)[11];
			lagcnt += result.get(i)[12];
			x = FP / 400;
			if (x > 1) {
				break;
			}
			y = (lagcnt == 0) ? 0 : lag / P;
			plot.add(new Point(x,y));
		}
		return plot;
	}
	
	private static double[] compare(int[] test, int[] real) {
		double result[] = new double[20];
		int TP=0, TN=0, FP=0, FN=0;
		for (int i=0; i<34; i++) {
			if (test[i]!=0 && real[i]!=0)
				TP++;
			else if (test[i]==0 && real[i]==0)
				TN++;
			else if (test[i]!=0 && real[i]==0)
				FP++;
			else if (test[i]==0 && real[i]!=0)
				FN++;
		}
		result[0]=TP;
		result[1]=TN;
		result[2]=FP;
		result[3]=FN;
		return result;
	}	
	
	public static String formatFloatNumber(Double value) {
        if(value != null){
            if(value.doubleValue() != 0.00){
                java.text.DecimalFormat df = new java.text.DecimalFormat("###.00");
                return df.format(value.doubleValue());
            }else{
                return "0.00";
            }
        }
        return "";
    }
	
	public static void main(String[] args) {
		init();
		plotForeDete();
		plotFore();
	}
}

class Point{
	double x;
	double y;
	
	public Point(double a, double b){
		x = a;
		y = b;
	}
}

class scoreComparator implements Comparator<double[]> {
	//[0]TP,[1]TN,[2]FP,[3]FN,[4]FPR,[5]TPR,[6]precision,[7]recall,[8]score,[9]lead,[10]leadcnt,[11]lag,[12]lagcnt
    public int compare(double[] one, double[] another) {
         double i = 0;
         i = one[8] - another[8];
         if(i == 0) {
        	 double j = one[0] - another[0];
        	 if (j == 0)
        		 return 0;
        	 else if (j < 0)
        		 return 1;
        	 else
        		 return -1;
         } 
         else if (i < 0)
        	 return 1;
         else
             return -1;
    }
}

class leadComparator implements Comparator<double[]> { 
	//[0]TP,[1]TN,[2]FP,[3]FN,[4]FPR,[5]TPR,[6]precision,[7]recall,[8]score,[9]lead,[10]leadcnt,[11]lag,[12]lagcnt
    public int compare(double[] one, double[] another) {
         if (one[10]==0 || another[10]==0) {
        	 if(one[10]==0 && another[10]==0)
        		 return 0;
        	 else if(one[10]==0 && another[10]!=0)
        		 return 1;
        	 else return -1;
         } 
         else {
	         if ((one[9]/one[10] - another[9]/another[10]) < 0)
	        	 return -1;
	         else if ((one[9]/one[10] - another[9]/another[10]) > 0)
	        	 return 1;
	         else return 0;
         }
    }
}

class lagComparator implements Comparator<double[]> { 
	//[0]TP,[1]TN,[2]FP,[3]FN,[4]FPR,[5]TPR,[6]precision,[7]recall,[8]score,[9]lead,[10]leadcnt,[11]lag,[12]lagcnt
    public int compare(double[] one, double[] another) {
         double i = 0;
         if (one[12]==0)
        	 return 1;
         if (another[12]==0)
        	 return -1;
         i = one[11]/one[12] - another[11]/another[12];
         if(i == 0)
        	 return 0;
         else if (i < 0)
        	 return 1;
         else
             return -1;
    }
}
 